package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.AuthServiceClient;
import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.ApplicationActionDTO;
import com.dungnguyen.registration_service.dto.StudentApplicationDTO;
import com.dungnguyen.registration_service.dto.StudentDTO;
import com.dungnguyen.registration_service.entity.*;
import com.dungnguyen.registration_service.exception.InternshipApplicationNotFoundException;
import com.dungnguyen.registration_service.exception.InternshipPeriodNotFoundException;
import com.dungnguyen.registration_service.exception.UnauthorizedAccessException;
import com.dungnguyen.registration_service.repository.InternshipApplicationDetailRepository;
import com.dungnguyen.registration_service.repository.InternshipApplicationRepository;
import com.dungnguyen.registration_service.repository.InternshipPositionRepository;
import com.dungnguyen.registration_service.repository.InternshipProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyApplicationService {

    private final InternshipApplicationRepository applicationRepository;
    private final InternshipApplicationDetailRepository applicationDetailRepository;
    private final InternshipPositionRepository positionRepository;
    private final InternshipProgressRepository progressRepository;
    private final AuthServiceClient authServiceClient;
    private final UserServiceClient userServiceClient;
    private final InternshipPeriodService periodService;

    /**
     * Get pending applications for a company where the application is the highest pending preference
     *
     * @param token Authorization token
     * @return List of StudentApplicationDTO with student details
     */
    public List<StudentApplicationDTO> getPendingApplicationsForCompany(String token) {
        // Get current company ID from token
        Integer companyId = authServiceClient.getUserCompanyId(token);
        if (companyId == null) {
            throw new UnauthorizedAccessException("Could not determine company from authorization token");
        }

        // Get upcoming period - applications for current period only
        InternshipPeriod upcomingPeriod = periodService.getUpcomingPeriod();

        // Get all positions for this company in the upcoming period
        List<InternshipPosition> companyPositions = positionRepository.findByCompanyIdAndPeriodId(companyId, upcomingPeriod.getId());

        if (companyPositions.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> positionIds = companyPositions.stream()
                .map(InternshipPosition::getId)
                .collect(Collectors.toList());

        // Get all application details for these positions
        List<InternshipApplicationDetail> allPositionApplicationDetails = applicationDetailRepository
                .findByPositionIdInAndStatus(positionIds, InternshipApplicationDetail.Status.PENDING);

        // Filter for highest pending preference for each student
        List<InternshipApplicationDetail> highestPendingPreferences = new ArrayList<>();

        // Group by application ID and find the highest pending preference for each
        allPositionApplicationDetails.stream()
                .collect(Collectors.groupingBy(detail -> detail.getApplication().getId()))
                .forEach((applicationId, details) -> {
                    Optional<InternshipApplicationDetail> highestPending = details.stream()
                            .filter(detail -> detail.getStatus() == InternshipApplicationDetail.Status.PENDING)
                            .min((d1, d2) -> Integer.compare(d1.getPreferenceOrder(), d2.getPreferenceOrder()));

                    highestPending.ifPresent(highestPendingPreferences::add);
                });

        // Now filter out applications where the student has a higher preference pending at another company
        List<InternshipApplicationDetail> filteredDetails = new ArrayList<>();

        for (InternshipApplicationDetail detail : highestPendingPreferences) {
            InternshipApplication application = detail.getApplication();

            // Check if this is the highest pending preference for this application
            boolean isHighestPending = true;
            for (InternshipApplicationDetail otherDetail : application.getDetails()) {
                if (otherDetail.getStatus() == InternshipApplicationDetail.Status.PENDING &&
                        otherDetail.getPreferenceOrder() < detail.getPreferenceOrder()) {
                    isHighestPending = false;
                    break;
                }
            }

            if (isHighestPending) {
                filteredDetails.add(detail);
            }
        }

        // Convert to DTOs with student details
        return filteredDetails.stream()
                .map(detail -> convertToStudentApplicationDTO(detail, token))
                .collect(Collectors.toList());
    }

    /**
     * Process an application action (approve or reject)
     *
     * @param actionDTO Action data
     * @param token Authorization token
     * @return Updated StudentApplicationDTO
     */
    @Transactional
    public StudentApplicationDTO processApplicationAction(ApplicationActionDTO actionDTO, String token) {
        // Get current company ID from token
        Integer companyId = authServiceClient.getUserCompanyId(token);
        if (companyId == null) {
            throw new UnauthorizedAccessException("Could not determine company from authorization token");
        }

        // Get application detail
        InternshipApplicationDetail applicationDetail = applicationDetailRepository.findById(actionDTO.getApplicationDetailId())
                .orElseThrow(() -> new InternshipApplicationNotFoundException("Application detail not found with ID: " + actionDTO.getApplicationDetailId()));

        // Verify this application is for a position belonging to the current company
        InternshipPosition position = applicationDetail.getPosition();
        if (!position.getCompanyId().equals(companyId)) {
            throw new UnauthorizedAccessException("You are not authorized to access this application");
        }

        // Verify status is PENDING
        if (applicationDetail.getStatus() != InternshipApplicationDetail.Status.PENDING) {
            throw new IllegalStateException("This application is already " + applicationDetail.getStatus());
        }

        // Handle action
        if ("APPROVE".equals(actionDTO.getAction())) {
            // Update status to APPROVED
            applicationDetail.setStatus(InternshipApplicationDetail.Status.APPROVED);
            // Note không được thay đổi vì đây là note của sinh viên

            // Cancel other pending applications for this student
            InternshipApplication application = applicationDetail.getApplication();
            for (InternshipApplicationDetail detail : application.getDetails()) {
                if (!detail.getId().equals(applicationDetail.getId()) &&
                        detail.getStatus() == InternshipApplicationDetail.Status.PENDING) {
                    detail.setStatus(InternshipApplicationDetail.Status.CANCELLED);
                    detail.setNote(detail.getNote() + "\n[Tự động hủy: Đã được duyệt vào vị trí khác]");
                    applicationDetailRepository.save(detail);
                }
            }

            // Create internship_progress record
            createInternshipProgress(applicationDetail);
        } else if ("REJECT".equals(actionDTO.getAction())) {
            // Update status to REJECTED
            applicationDetail.setStatus(InternshipApplicationDetail.Status.REJECTED);
            // Note không được thay đổi vì đây là note của sinh viên

            // Find next preference and activate it
            activateNextPreference(applicationDetail);
        } else {
            throw new IllegalArgumentException("Invalid action: " + actionDTO.getAction());
        }

        // Save changes
        InternshipApplicationDetail updatedDetail = applicationDetailRepository.save(applicationDetail);

        // Return updated DTO
        return convertToStudentApplicationDTO(updatedDetail, token);
    }

    /**
     * Get applications history for a company
     *
     * @param status Optional status filter (APPROVED, REJECTED, CANCELLED)
     * @param token Authorization token
     * @return List of StudentApplicationDTO
     */
    public List<StudentApplicationDTO> getApplicationsHistoryForCompany(
            String periodId, String status, String token) {
        // Get current company ID from token
        Integer companyId = authServiceClient.getUserCompanyId(token);
        if (companyId == null) {
            throw new UnauthorizedAccessException("Could not determine company from authorization token");
        }

        // Get all positions for this company
        List<InternshipPosition> companyPositions;

        if (periodId != null && !periodId.isEmpty()) {
            // Validate period exists
            if (!periodService.periodExists(periodId)) {
                throw new InternshipPeriodNotFoundException("Internship period not found with ID: " + periodId);
            }
            // Get positions for specific period
            companyPositions = positionRepository.findByCompanyIdAndPeriodId(companyId, periodId);
        } else {
            // Get all positions
            companyPositions = positionRepository.findByCompanyId(companyId);
        }

        if (companyPositions.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> positionIds = companyPositions.stream()
                .map(InternshipPosition::getId)
                .collect(Collectors.toList());

        // Get application details based on filter
        List<InternshipApplicationDetail> applicationDetails;

        if (status != null && !status.isEmpty()) {
            try {
                InternshipApplicationDetail.Status statusEnum = InternshipApplicationDetail.Status.valueOf(status);
                applicationDetails = applicationDetailRepository.findByPositionIdInAndStatus(positionIds, statusEnum);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + status);
            }
        } else {
            applicationDetails = applicationDetailRepository.findByPositionIdIn(positionIds);
        }

        // Convert to DTOs with student details
        return applicationDetails.stream()
                .map(detail -> convertToStudentApplicationDTO(detail, token))
                .collect(Collectors.toList());
    }


    /**
     * Create an internship progress record from an approved application
     */
    private void createInternshipProgress(InternshipApplicationDetail approvedDetail) {
        InternshipApplication application = approvedDetail.getApplication();
        InternshipPosition position = approvedDetail.getPosition();

        InternshipProgress progress = new InternshipProgress();
        progress.setStudentId(application.getStudentId());
        progress.setPosition(position);
        progress.setPeriod(position.getPeriod());
        progress.setStartDate(position.getPeriod().getStartDate());
        progress.setEndDate(position.getPeriod().getEndDate());
        progress.setIsExternal(false);
        progress.setStatus(InternshipProgress.Status.IN_PROGRESS);

        progressRepository.save(progress);
    }

    /**
     * Activate next preference after rejection
     */
    private void activateNextPreference(InternshipApplicationDetail rejectedDetail) {
        InternshipApplication application = rejectedDetail.getApplication();
        Integer rejectedPreferenceOrder = rejectedDetail.getPreferenceOrder();

        // Find the next preference (if any)
        Optional<InternshipApplicationDetail> nextPreference = application.getDetails().stream()
                .filter(detail -> detail.getPreferenceOrder() > rejectedPreferenceOrder &&
                        detail.getStatus() == InternshipApplicationDetail.Status.PENDING)
                .min((d1, d2) -> Integer.compare(d1.getPreferenceOrder(), d2.getPreferenceOrder()));

        // No action needed if there's no next preference
        // The next preference is already in PENDING status
    }

    /**
     * Convert InternshipApplicationDetail to StudentApplicationDTO with student details
     */
    private StudentApplicationDTO convertToStudentApplicationDTO(InternshipApplicationDetail detail, String token) {
        InternshipApplication application = detail.getApplication();
        InternshipPosition position = detail.getPosition();

        StudentApplicationDTO dto = new StudentApplicationDTO();

        // Set application and position info
        dto.setApplicationId(application.getId());
        dto.setApplicationDetailId(detail.getId());
        dto.setPositionId(position.getId());
        dto.setPositionTitle(position.getTitle());
        dto.setPreferenceOrder(detail.getPreferenceOrder());
        dto.setStatus(detail.getStatus().name());
        dto.setNote(detail.getNote());
        dto.setCvFilePath(application.getCvFilePath());
        dto.setAppliedAt(application.getCreatedAt());

        // Get student info from User Service
        StudentDTO studentDTO = userServiceClient.getStudentById(application.getStudentId(), token);
        if (studentDTO != null) {
            dto.setStudentId(studentDTO.getId());
            dto.setStudentCode(studentDTO.getStudentCode());
            dto.setName(studentDTO.getName());
            dto.setEmail(studentDTO.getEmail());
            dto.setPhone(studentDTO.getPhone());
            dto.setClassName(studentDTO.getClassName());
            dto.setMajor(studentDTO.getMajor());
            dto.setGender(studentDTO.getGender());
            dto.setBirthday(studentDTO.getBirthday());
            dto.setAddress(studentDTO.getAddress());
            dto.setCpa(studentDTO.getCpa());
            dto.setEnglishLevel(studentDTO.getEnglishLevel());
            dto.setSkills(studentDTO.getSkills());
        }

        return dto;
    }
}
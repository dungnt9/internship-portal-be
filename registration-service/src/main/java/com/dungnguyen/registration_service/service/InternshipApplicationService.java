package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.AuthServiceClient;
import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.*;
import com.dungnguyen.registration_service.entity.InternshipApplication;
import com.dungnguyen.registration_service.entity.InternshipApplicationDetail;
import com.dungnguyen.registration_service.entity.InternshipPeriod;
import com.dungnguyen.registration_service.entity.InternshipPosition;
import com.dungnguyen.registration_service.exception.*;
import com.dungnguyen.registration_service.repository.InternshipApplicationDetailRepository;
import com.dungnguyen.registration_service.repository.InternshipApplicationRepository;
import com.dungnguyen.registration_service.repository.InternshipPeriodRepository;
import com.dungnguyen.registration_service.repository.InternshipPositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternshipApplicationService {

    private final InternshipApplicationRepository applicationRepository;
    private final InternshipApplicationDetailRepository applicationDetailRepository;
    private final InternshipPeriodRepository periodRepository;
    private final InternshipPositionRepository positionRepository;
    private final AuthServiceClient authServiceClient;
    private final UserServiceClient userServiceClient;
    private final FileUploadService fileUploadService;

    /**
     * Get all internship applications for current student
     *
     * @param token Authorization token
     * @return List of InternshipApplicationDTO
     */
    public List<InternshipApplicationDTO> getMyApplications(String token) {
        // Get current student ID from token
        Integer studentId = authServiceClient.getUserStudentId(token);
        if (studentId == null) {
            throw new RuntimeException("Could not determine student from authorization token");
        }

        // Get all applications for current student
        List<InternshipApplication> applications = applicationRepository.findByStudentId(studentId);

        // Convert to DTOs with details
        return applications.stream()
                .map(app -> convertToDTO(app, token))
                .collect(Collectors.toList());
    }

    /**
     * Create new internship application (upload CV)
     *
     * @param createDTO Application creation data
     * @param cvFile CV file
     * @param token Authorization token
     * @return Created InternshipApplicationDTO
     */
    @Transactional
    public InternshipApplicationDTO createApplication(
            InternshipApplicationCreateDTO createDTO,
            MultipartFile cvFile,
            String token) {

        // Get current student ID from token
        Integer studentId = authServiceClient.getUserStudentId(token);
        if (studentId == null) {
            throw new RuntimeException("Could not determine student from authorization token");
        }

        // Get student code for file path
        String studentCode = getStudentCode(studentId, token);

        // Get period
        InternshipPeriod period = periodRepository.findById(createDTO.getPeriodId())
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + createDTO.getPeriodId()));

        // Validate if registration is currently open
        validateRegistrationPeriod(period);

        // Check if student already has an application for this period
        Optional<InternshipApplication> existingApplication =
                applicationRepository.findByStudentIdAndPeriodId(studentId, createDTO.getPeriodId());

        if (existingApplication.isPresent()) {
            // If already exists, update the CV file
            InternshipApplication application = existingApplication.get();

            // Upload new CV file
            String filePath = fileUploadService.uploadFile(cvFile, studentCode, "cv");
            application.setCvFilePath(filePath);

            // Save to database
            InternshipApplication updatedApplication = applicationRepository.save(application);

            // Return as DTO
            return convertToDTO(updatedApplication, token);
        } else {
            // If new, create a new application

            // Upload CV file
            String filePath = fileUploadService.uploadFile(cvFile, studentCode, "cv");

            // Create new application
            InternshipApplication application = new InternshipApplication();
            application.setStudentId(studentId);
            application.setPeriod(period);
            application.setCvFilePath(filePath);
            application.setDetails(new ArrayList<>());

            // Save to database
            InternshipApplication savedApplication = applicationRepository.save(application);

            // Return as DTO
            return convertToDTO(savedApplication, token);
        }
    }

    /**
     * Register internship preferences (must have exactly 3 preferences)
     *
     * @param registerDTO Preferences data
     * @param token Authorization token
     * @return Updated InternshipApplicationDTO
     */
    @Transactional
    public InternshipApplicationDTO registerPreferences(
            InternshipPreferencesRegisterDTO registerDTO,
            String token) {

        // Get current student ID from token
        Integer studentId = authServiceClient.getUserStudentId(token);
        if (studentId == null) {
            throw new RuntimeException("Could not determine student from authorization token");
        }

        // Get application
        InternshipApplication application = applicationRepository.findById(registerDTO.getApplicationId())
                .orElseThrow(() -> new InternshipApplicationNotFoundException(
                        "Internship application not found with ID: " + registerDTO.getApplicationId()));

        // Validate if application belongs to current student
        if (!application.getStudentId().equals(studentId)) {
            throw new UnauthorizedAccessException("You are not authorized to modify this application");
        }

        // Validate if registration is currently open
        validateRegistrationPeriod(application.getPeriod());

        // Validate preferences
        validatePreferences(registerDTO.getPreferences());

        // Create new application details
        List<InternshipApplicationDetail> details = new ArrayList<>();
        for (InternshipPreferenceDTO preferenceDTO : registerDTO.getPreferences()) {
            // Get position
            InternshipPosition position = positionRepository.findById(preferenceDTO.getPositionId())
                    .orElseThrow(() -> new InternshipPositionNotFoundException(
                            "Internship position not found with ID: " + preferenceDTO.getPositionId()));

            // Validate if position belongs to the same period as application
            if (!position.getPeriod().getId().equals(application.getPeriod().getId())) {
                throw new ValidationException(
                        "Position with ID " + preferenceDTO.getPositionId() +
                                " does not belong to the same period as the application");
            }

            InternshipApplicationDetail detail = new InternshipApplicationDetail();
            detail.setApplication(application);
            detail.setPosition(position);
            detail.setPreferenceOrder(preferenceDTO.getPreferenceOrder());
            detail.setNote(preferenceDTO.getNote());
            detail.setStatus(InternshipApplicationDetail.Status.PENDING);

            details.add(detail);
        }

        // Clear existing details (if any) and add new ones
        application.getDetails().clear();
        application.getDetails().addAll(details);

        // Save to database
        InternshipApplication savedApplication = applicationRepository.save(application);

        // Return as DTO
        return convertToDTO(savedApplication, token);
    }

    /**
     * Convert InternshipApplication to DTO
     *
     * @param application InternshipApplication entity
     * @param token Authorization token for fetching related data
     * @return InternshipApplicationDTO
     */
    private InternshipApplicationDTO convertToDTO(InternshipApplication application, String token) {
        InternshipApplicationDTO dto = new InternshipApplicationDTO();
        dto.setId(application.getId());
        dto.setStudentId(application.getStudentId());
        dto.setPeriodId(application.getPeriod().getId());
        dto.setCvFilePath(application.getCvFilePath());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setUpdatedAt(application.getUpdatedAt());

        // Convert details
        List<InternshipApplicationDetailDTO> detailDTOs = application.getDetails().stream()
                .map(this::convertDetailToDTO)
                .collect(Collectors.toList());

        dto.setDetails(detailDTOs);
        return dto;
    }

    /**
     * Convert InternshipApplicationDetail to DTO
     *
     * @param detail InternshipApplicationDetail entity
     * @return InternshipApplicationDetailDTO
     */
    private InternshipApplicationDetailDTO convertDetailToDTO(InternshipApplicationDetail detail) {
        InternshipApplicationDetailDTO dto = new InternshipApplicationDetailDTO();
        dto.setId(detail.getId());
        dto.setPositionId(detail.getPosition().getId());
        dto.setPositionTitle(detail.getPosition().getTitle());

        // Get company name using company ID from position
        CompanyDTO company = userServiceClient.getCompanyById(detail.getPosition().getCompanyId(), null);
        dto.setCompanyName(company != null ? company.getName() : "Unknown Company");

        dto.setPreferenceOrder(detail.getPreferenceOrder());
        dto.setStatus(detail.getStatus().name());
        dto.setNote(detail.getNote());
        dto.setCreatedAt(detail.getCreatedAt());
        dto.setUpdatedAt(detail.getUpdatedAt());
        return dto;
    }

    /**
     * Validate if registration is currently open for the given period
     *
     * @param period InternshipPeriod
     * @throws ValidationException if registration is not open
     */
    private void validateRegistrationPeriod(InternshipPeriod period) {

        LocalDate today = LocalDate.now();

        if (today.isBefore(period.getRegistrationStartDate())) {
            throw new ValidationException("Registration for this period has not started yet");
        }

        if (today.isAfter(period.getRegistrationEndDate())) {
            throw new ValidationException("Registration for this period has already ended");
        }
    }

    /**
     * Validate preferences
     *
     * @param preferences List of preferences
     * @throws ValidationException if preferences are invalid
     */
    private void validatePreferences(List<InternshipPreferenceDTO> preferences) {
        // Check if there are exactly 3 preferences
        if (preferences.size() != 3) {
            throw new ValidationException("Exactly 3 preferences are required");
        }

        // Check if preference orders are 1, 2, and 3
        Set<Integer> orders = preferences.stream()
                .map(InternshipPreferenceDTO::getPreferenceOrder)
                .collect(Collectors.toSet());

        if (!orders.contains(1) || !orders.contains(2) || !orders.contains(3) || orders.size() != 3) {
            throw new ValidationException("Preferences must have orders 1, 2, and 3");
        }

        // Check if position IDs are unique
        Set<Integer> positionIds = preferences.stream()
                .map(InternshipPreferenceDTO::getPositionId)
                .collect(Collectors.toSet());

        if (positionIds.size() != preferences.size()) {
            throw new ValidationException("Duplicate position IDs are not allowed");
        }
    }

    /**
     * Get student code from student ID
     *
     * @param studentId Student ID
     * @param token Authorization token
     * @return Student code
     */
    private String getStudentCode(Integer studentId, String token) {
        // This method would call UserServiceClient to get student details
        // For now, returning a placeholder - you'll need to implement this
        // based on your UserServiceClient capabilities
        return "student_" + studentId;
    }
}
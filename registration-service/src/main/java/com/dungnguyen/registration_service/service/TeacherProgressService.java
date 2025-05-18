package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.AuthServiceClient;
import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.*;
import com.dungnguyen.registration_service.entity.InternshipPosition;
import com.dungnguyen.registration_service.entity.InternshipProgress;
import com.dungnguyen.registration_service.exception.InternshipProgressNotFoundException;
import com.dungnguyen.registration_service.exception.UnauthorizedAccessException;
import com.dungnguyen.registration_service.repository.InternshipProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherProgressService {

    private final InternshipProgressRepository progressRepository;
    private final AuthServiceClient authServiceClient;
    private final UserServiceClient userServiceClient;

    /**
     * Get all internship progress records assigned to a teacher
     *
     * @param periodId Optional period ID filter
     * @param status Optional status filter (IN_PROGRESS, COMPLETED, CANCELLED)
     * @param token Authorization token
     * @return List of InternshipProgressDetailDTO with student details
     */
    public List<InternshipProgressDetailDTO> getProgressForTeacher(String periodId, String status, String token) {
        // Get current teacher ID from token
        Integer teacherId = authServiceClient.getUserTeacherId(token);
        if (teacherId == null) {
            throw new UnauthorizedAccessException("Could not determine teacher from authorization token");
        }

        // Get all progress records for this teacher
        List<InternshipProgress> progressList = progressRepository.findByTeacherId(teacherId);

        // Apply period filter if specified
        if (periodId != null && !periodId.isEmpty()) {
            progressList = progressList.stream()
                    .filter(p -> p.getPeriod().getId().equals(periodId))
                    .collect(Collectors.toList());
        }

        // Apply status filter if specified
        if (status != null && !status.isEmpty()) {
            try {
                InternshipProgress.Status statusEnum = InternshipProgress.Status.valueOf(status);
                progressList = progressList.stream()
                        .filter(p -> p.getStatus() == statusEnum)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + status);
            }
        }

        // Convert to DTOs with student details
        List<InternshipProgressDetailDTO> resultList = new ArrayList<>();

        for (InternshipProgress progress : progressList) {
            InternshipProgressDetailDTO detailDTO = new InternshipProgressDetailDTO();

            // Set progress data
            InternshipProgressDTO progressDTO = new InternshipProgressDTO(progress);
            detailDTO.setProgress(progressDTO);

            // Set student data
            StudentDTO studentDTO = userServiceClient.getStudentById(progress.getStudentId(), token);
            detailDTO.setStudent(studentDTO);

            // Add to result list
            resultList.add(detailDTO);
        }

        return resultList;
    }

    /**
     * Get detailed information about a specific internship progress
     *
     * @param progressId Internship progress ID
     * @param token Authorization token
     * @return InternshipProgressDetailDTO with student and company details
     */
    public InternshipProgressDetailDTO getProgressDetail(Integer progressId, String token) {
        // Get current teacher ID from token
        Integer teacherId = authServiceClient.getUserTeacherId(token);
        if (teacherId == null) {
            throw new UnauthorizedAccessException("Could not determine teacher from authorization token");
        }

        // Get the progress record
        InternshipProgress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new InternshipProgressNotFoundException("Internship progress not found with ID: " + progressId));

        // Verify that this progress is assigned to the current teacher
        if (!progress.getTeacherId().equals(teacherId)) {
            throw new UnauthorizedAccessException("You are not authorized to access this internship progress");
        }

        // Create the detail DTO
        InternshipProgressDetailDTO detailDTO = new InternshipProgressDetailDTO();

        // Set the base progress data
        InternshipProgressDTO progressDTO = new InternshipProgressDTO(progress);
        detailDTO.setProgress(progressDTO);

        // Get and set student details
        StudentDTO studentDTO = userServiceClient.getStudentById(progress.getStudentId(), token);
        detailDTO.setStudent(studentDTO);

        // Get and set company and position details if this is not an external internship
        if (!progress.getIsExternal() && progress.getPosition() != null) {
            // Get company details from the position
            InternshipPosition position = progress.getPosition();
            CompanyDTO companyDTO = userServiceClient.getCompanyById(position.getCompanyId(), token);
            detailDTO.setCompany(companyDTO);

            // Set position details
            InternshipPositionDTO positionDTO = new InternshipPositionDTO(position);
            if (companyDTO != null) {
                positionDTO.setCompanyName(companyDTO.getName());
                positionDTO.setCompanyShortName(companyDTO.getShortName());
                positionDTO.setWebsite(companyDTO.getWebsite());
                positionDTO.setLogoPath(companyDTO.getLogoPath());
                positionDTO.setAddress(companyDTO.getAddress());
                positionDTO.setBusinessType(companyDTO.getBusinessType());
            }
            detailDTO.setPosition(positionDTO);
        }

        return detailDTO;
    }

    /**
     * Confirm a student's internship progress
     *
     * @param progressId Internship progress ID
     * @param confirmDTO Confirmation data (optional)
     * @param token Authorization token
     * @return Updated InternshipProgressDTO
     */
    @Transactional
    public InternshipProgressDTO confirmProgress(Integer progressId, InternshipProgressConfirmDTO confirmDTO, String token) {
        // Get current teacher ID from token
        Integer teacherId = authServiceClient.getUserTeacherId(token);
        if (teacherId == null) {
            throw new UnauthorizedAccessException("Could not determine teacher from authorization token");
        }

        // Get the progress record
        InternshipProgress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new InternshipProgressNotFoundException("Internship progress not found with ID: " + progressId));

        // Verify that this progress is assigned to the current teacher
        if (!progress.getTeacherId().equals(teacherId)) {
            throw new UnauthorizedAccessException("You are not authorized to confirm this internship progress");
        }

        // Update confirmation status
        boolean confirm = confirmDTO != null && confirmDTO.getConfirm() != null ? confirmDTO.getConfirm() : true;
        progress.setTeacherConfirmed(confirm);

        // Set confirmation timestamp if confirming
        if (confirm) {
            progress.setTeacherConfirmedAt(LocalDateTime.now());
        } else {
            progress.setTeacherConfirmedAt(null);
        }

        // Save the updated progress
        InternshipProgress updatedProgress = progressRepository.save(progress);

        // Convert to DTO
        return new InternshipProgressDTO(updatedProgress);
    }
}
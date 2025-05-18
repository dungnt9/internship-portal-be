// StudentProgressService.java
package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.AuthServiceClient;
import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.*;
import com.dungnguyen.registration_service.entity.InternshipProgress;
import com.dungnguyen.registration_service.entity.InternshipPeriod;
import com.dungnguyen.registration_service.exception.InternshipProgressNotFoundException;
import com.dungnguyen.registration_service.exception.UnauthorizedAccessException;
import com.dungnguyen.registration_service.repository.InternshipPeriodRepository;
import com.dungnguyen.registration_service.repository.InternshipProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentProgressService {

    private final InternshipProgressRepository progressRepository;
    private final InternshipPeriodRepository periodRepository;
    private final AuthServiceClient authServiceClient;
    private final UserServiceClient userServiceClient;

    /**
     * Get current period internship progress for the logged-in student
     *
     * @param token Authorization token
     * @return InternshipProgressDetailDTO with progress details
     */
    public InternshipProgressDetailDTO getCurrentProgress(String token) {
        // Get current student ID from token
        Integer studentId = authServiceClient.getUserStudentId(token);
        if (studentId == null) {
            throw new UnauthorizedAccessException("Could not determine student from authorization token");
        }

        // Get current active period
        InternshipPeriod currentPeriod = periodRepository.findByStatus(InternshipPeriod.Status.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("No active internship period found"));

        // Find progress for this student in the current period
        InternshipProgress progress = progressRepository.findByStudentIdAndPeriodId(studentId, currentPeriod.getId())
                .orElseThrow(() -> new InternshipProgressNotFoundException(
                        "No internship progress found for the current period. Please contact your academic advisor."));

        // Create detail DTO with available information
        return createProgressDetailDTO(progress, token);
    }

    /**
     * Update internship progress information for the current student
     *
     * @param updateDTO Updated information
     * @param token Authorization token
     * @return Updated InternshipProgressDetailDTO
     */
    @Transactional
    public InternshipProgressDetailDTO updateProgress(InternshipProgressUpdateDTO updateDTO, String token) {
        // Get current student ID from token
        Integer studentId = authServiceClient.getUserStudentId(token);
        if (studentId == null) {
            throw new UnauthorizedAccessException("Could not determine student from authorization token");
        }

        // Get current active period
        InternshipPeriod currentPeriod = periodRepository.findByStatus(InternshipPeriod.Status.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("No active internship period found"));

        // Find progress for this student in the current period
        InternshipProgress progress = progressRepository.findByStudentIdAndPeriodId(studentId, currentPeriod.getId())
                .orElseThrow(() -> new InternshipProgressNotFoundException(
                        "No internship progress found for the current period. Please contact your academic advisor."));

        // Update fields based on internship type
        if (progress.getIsExternal()) {
            // For external internships, update company name and position title if provided
            if (updateDTO.getCompanyName() != null && !updateDTO.getCompanyName().isEmpty()) {
                progress.setCompanyName(updateDTO.getCompanyName());
            }
            if (updateDTO.getPositionTitle() != null && !updateDTO.getPositionTitle().isEmpty()) {
                progress.setPositionTitle(updateDTO.getPositionTitle());
            }
        }

        // Update common fields for both internal and external internships
        if (updateDTO.getSupervisorName() != null && !updateDTO.getSupervisorName().isEmpty()) {
            progress.setSupervisorName(updateDTO.getSupervisorName());
        }
        if (updateDTO.getSupervisorPosition() != null && !updateDTO.getSupervisorPosition().isEmpty()) {
            progress.setSupervisorPosition(updateDTO.getSupervisorPosition());
        }
        if (updateDTO.getSupervisorEmail() != null && !updateDTO.getSupervisorEmail().isEmpty()) {
            progress.setSupervisorEmail(updateDTO.getSupervisorEmail());
        }
        if (updateDTO.getSupervisorPhone() != null && !updateDTO.getSupervisorPhone().isEmpty()) {
            progress.setSupervisorPhone(updateDTO.getSupervisorPhone());
        }

        // Save updated progress
        InternshipProgress updatedProgress = progressRepository.save(progress);

        // Return updated detail DTO
        return createProgressDetailDTO(updatedProgress, token);
    }

    /**
     * Helper method to create a detailed DTO from a progress entity
     */
    private InternshipProgressDetailDTO createProgressDetailDTO(InternshipProgress progress, String token) {
        InternshipProgressDetailDTO detailDTO = new InternshipProgressDetailDTO();

        // Set base progress data
        detailDTO.setProgress(new InternshipProgressDTO(progress));

        // Set student data
        detailDTO.setStudent(userServiceClient.getStudentById(progress.getStudentId(), token));

        // Set company and position data based on internship type
        if (!progress.getIsExternal() && progress.getPosition() != null) {
            // For internal internships, get company from position
            CompanyDTO company = userServiceClient.getCompanyById(progress.getPosition().getCompanyId(), token);
            detailDTO.setCompany(company);

            // Set position details
            InternshipPositionDTO positionDTO = new InternshipPositionDTO(progress.getPosition());
            detailDTO.setPosition(positionDTO);
        } else if (progress.getIsExternal()) {
            // For external internships, set company and position to null as requested
            detailDTO.setCompany(null);
            detailDTO.setPosition(null);

            // The companyName and positionTitle will still be included in the progress DTO
        }

        return detailDTO;
    }
}
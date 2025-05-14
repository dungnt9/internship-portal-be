package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.AuthServiceClient;
import com.dungnguyen.registration_service.dto.ExternalInternshipCreateDTO;
import com.dungnguyen.registration_service.dto.ExternalInternshipDTO;
import com.dungnguyen.registration_service.dto.ExternalInternshipUpdateDTO;
import com.dungnguyen.registration_service.entity.ExternalInternship;
import com.dungnguyen.registration_service.entity.InternshipPeriod;
import com.dungnguyen.registration_service.exception.DuplicateExternalInternshipException;
import com.dungnguyen.registration_service.exception.ExternalInternshipNotFoundException;
import com.dungnguyen.registration_service.exception.InternshipPeriodNotFoundException;
import com.dungnguyen.registration_service.repository.ExternalInternshipRepository;
import com.dungnguyen.registration_service.repository.InternshipPeriodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalInternshipService {

    private final ExternalInternshipRepository externalInternshipRepository;
    private final InternshipPeriodRepository periodRepository;
    private final AuthServiceClient authServiceClient;

    /**
     * Get all external internships for the current student
     *
     * @param token Authorization token
     * @return List of ExternalInternshipDTO
     */
    public List<ExternalInternshipDTO> getMyExternalInternships(String token) {
        Integer studentId = getCurrentUserAuthId(token);
        if (studentId == null) {
            throw new RuntimeException("Could not determine student from authorization token");
        }

        List<ExternalInternship> externalInternships = externalInternshipRepository.findByStudentId(studentId);
        return externalInternships.stream()
                .map(ExternalInternshipDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get a specific external internship for the current student
     *
     * @param id    External internship ID
     * @param token Authorization token
     * @return ExternalInternshipDTO
     */
    public ExternalInternshipDTO getMyExternalInternshipById(Integer id, String token) {
        Integer studentId = getCurrentUserAuthId(token);
        if (studentId == null) {
            throw new RuntimeException("Could not determine student from authorization token");
        }

        ExternalInternship externalInternship = externalInternshipRepository.findByIdAndStudentId(id, studentId)
                .orElseThrow(() -> new ExternalInternshipNotFoundException("External internship not found with ID: " + id));

        return new ExternalInternshipDTO(externalInternship);
    }

    /**
     * Create a new external internship for the current student
     *
     * @param createDTO External internship creation data
     * @param token     Authorization token
     * @return Created ExternalInternshipDTO
     */
    @Transactional
    public ExternalInternshipDTO createExternalInternship(ExternalInternshipCreateDTO createDTO, String token) {
        Integer studentId = getCurrentUserAuthId(token);
        if (studentId == null) {
            throw new RuntimeException("Could not determine student from authorization token");
        }

        // Get the period
        InternshipPeriod period = periodRepository.findById(createDTO.getPeriodId())
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + createDTO.getPeriodId()));

        // Check if student already has an external internship for this period
        if (externalInternshipRepository.existsByPeriodIdAndStudentId(createDTO.getPeriodId(), studentId)) {
            throw new DuplicateExternalInternshipException("Student already has an external internship for period: " + createDTO.getPeriodId());
        }

        // Create new external internship
        ExternalInternship externalInternship = new ExternalInternship();
        externalInternship.setStudentId(studentId);
        externalInternship.setPeriod(period);
        externalInternship.setConfirmationFilePath(createDTO.getConfirmationFilePath());
        externalInternship.setStatus(ExternalInternship.Status.PENDING);
        externalInternship.setDeletedAt(null);

        // Save external internship
        ExternalInternship savedExternalInternship = externalInternshipRepository.save(externalInternship);

        // Return as DTO
        return new ExternalInternshipDTO(savedExternalInternship);
    }

    /**
     * Update an existing external internship for the current student
     *
     * @param id        External internship ID
     * @param updateDTO External internship update data
     * @param token     Authorization token
     * @return Updated ExternalInternshipDTO
     */
    @Transactional
    public ExternalInternshipDTO updateExternalInternship(Integer id, ExternalInternshipUpdateDTO updateDTO, String token) {
        Integer studentId = getCurrentUserAuthId(token);
        if (studentId == null) {
            throw new RuntimeException("Could not determine student from authorization token");
        }

        // Get the external internship, ensuring it belongs to the student
        ExternalInternship externalInternship = externalInternshipRepository.findByIdAndStudentId(id, studentId)
                .orElseThrow(() -> new ExternalInternshipNotFoundException("External internship not found with ID: " + id));

        // Only allow updates if status is PENDING or REJECTED
        if (externalInternship.getStatus() != ExternalInternship.Status.PENDING &&
                externalInternship.getStatus() != ExternalInternship.Status.REJECTED) {
            throw new RuntimeException("Cannot update external internship with status: " + externalInternship.getStatus());
        }

        // Update external internship fields
        if (updateDTO.getConfirmationFilePath() != null) {
            externalInternship.setConfirmationFilePath(updateDTO.getConfirmationFilePath());
        }

        // Allow students to only change status from PENDING to CANCELLED
        if (updateDTO.getStatus() != null) {
            if (ExternalInternship.Status.CANCELLED.name().equals(updateDTO.getStatus()) &&
                    externalInternship.getStatus() == ExternalInternship.Status.PENDING) {
                externalInternship.setStatus(ExternalInternship.Status.CANCELLED);
            } else {
                throw new RuntimeException("Students can only change status from PENDING to CANCELLED");
            }
        }

        // Save updated external internship
        ExternalInternship updatedExternalInternship = externalInternshipRepository.save(externalInternship);

        // Return as DTO
        return new ExternalInternshipDTO(updatedExternalInternship);
    }

    /**
     * Get the current user's auth ID from token
     *
     * @param token Authorization token
     * @return Auth user ID
     */
    public Integer getCurrentUserAuthId(String token) {
        return authServiceClient.getUserIdFromToken(token);
    }
}
package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.StudentDTO;
import com.dungnguyen.registration_service.dto.cms.CMSExternalInternshipCreateDTO;
import com.dungnguyen.registration_service.dto.cms.CMSExternalInternshipDTO;
import com.dungnguyen.registration_service.dto.cms.CMSExternalInternshipUpdateDTO;
import com.dungnguyen.registration_service.entity.ExternalInternship;
import com.dungnguyen.registration_service.entity.InternshipPeriod;
import com.dungnguyen.registration_service.entity.InternshipProgress;
import com.dungnguyen.registration_service.exception.ExternalInternshipNotFoundException;
import com.dungnguyen.registration_service.exception.InternshipPeriodNotFoundException;
import com.dungnguyen.registration_service.repository.ExternalInternshipRepository;
import com.dungnguyen.registration_service.repository.InternshipPeriodRepository;
import com.dungnguyen.registration_service.repository.InternshipProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CMSExternalInternshipService {

    private final ExternalInternshipRepository externalInternshipRepository;
    private final InternshipPeriodRepository periodRepository;
    private final InternshipProgressRepository progressRepository;
    private final UserServiceClient userServiceClient;
    private final FileUploadService fileUploadService;

    /**
     * Get all external internships
     *
     * @param token Authorization token
     * @return List of CMSExternalInternshipDTO
     */
    public List<CMSExternalInternshipDTO> getAllExternalInternships(String token) {
        List<ExternalInternship> externalInternships = externalInternshipRepository.findAll();

        return externalInternships.stream()
                .filter(ei -> ei.getDeletedAt() == null)
                .map(ei -> convertToDTO(ei, token))
                .collect(Collectors.toList());
    }

    /**
     * Create new external internship
     *
     * @param createDTO External internship creation data
     * @param confirmationFile Confirmation file
     * @param token Authorization token
     * @return Created CMSExternalInternshipDTO
     */
    @Transactional
    public CMSExternalInternshipDTO createExternalInternship(
            CMSExternalInternshipCreateDTO createDTO,
            MultipartFile confirmationFile,
            String token) {

        // Get period
        InternshipPeriod period = periodRepository.findById(createDTO.getPeriodId())
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + createDTO.getPeriodId()));

        // Get student info to generate proper file path
        StudentDTO student = userServiceClient.getStudentById(createDTO.getStudentId(), token);
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + createDTO.getStudentId());
        }

        // Upload file
        String filePath = fileUploadService.uploadFile(confirmationFile, student.getStudentCode(), "confirmations");

        // Create new external internship
        ExternalInternship externalInternship = new ExternalInternship();
        externalInternship.setStudentId(createDTO.getStudentId());
        externalInternship.setPeriod(period);
        externalInternship.setConfirmationFilePath(filePath);
        externalInternship.setStatus(ExternalInternship.Status.PENDING);

        // Save to database
        ExternalInternship savedExternalInternship = externalInternshipRepository.save(externalInternship);

        // Return as DTO
        return convertToDTO(savedExternalInternship, token);
    }

    /**
     * Update external internship status
     *
     * @param id External internship ID
     * @param updateDTO Update data
     * @param token Authorization token
     * @return Updated CMSExternalInternshipDTO
     */
    @Transactional
    public CMSExternalInternshipDTO updateExternalInternship(
            Integer id,
            CMSExternalInternshipUpdateDTO updateDTO,
            String token) {

        ExternalInternship externalInternship = externalInternshipRepository.findById(id)
                .orElseThrow(() -> new ExternalInternshipNotFoundException("External internship not found with ID: " + id));

        if (externalInternship.getDeletedAt() != null) {
            throw new ExternalInternshipNotFoundException("External internship has been deleted");
        }

        // Update status
        if (updateDTO.getStatus() != null) {
            ExternalInternship.Status newStatus = ExternalInternship.Status.valueOf(updateDTO.getStatus());
            externalInternship.setStatus(newStatus);

            // If approved, create internship progress record
            if (newStatus == ExternalInternship.Status.APPROVED) {
                createInternshipProgress(externalInternship);
            }
        }

        // Save changes
        ExternalInternship updatedExternalInternship = externalInternshipRepository.save(externalInternship);

        // Return as DTO
        return convertToDTO(updatedExternalInternship, token);
    }

    /**
     * Update confirmation file
     *
     * @param id External internship ID
     * @param confirmationFile New confirmation file
     * @param token Authorization token
     * @return Updated CMSExternalInternshipDTO
     */
    @Transactional
    public CMSExternalInternshipDTO updateConfirmationFile(
            Integer id,
            MultipartFile confirmationFile,
            String token) {

        ExternalInternship externalInternship = externalInternshipRepository.findById(id)
                .orElseThrow(() -> new ExternalInternshipNotFoundException("External internship not found with ID: " + id));

        if (externalInternship.getDeletedAt() != null) {
            throw new ExternalInternshipNotFoundException("External internship has been deleted");
        }

        // Get student info to generate proper file path
        StudentDTO student = userServiceClient.getStudentById(externalInternship.getStudentId(), token);
        if (student == null) {
            throw new RuntimeException("Student not found with ID: " + externalInternship.getStudentId());
        }

        // Upload new file
        String filePath = fileUploadService.uploadFile(confirmationFile, student.getStudentCode(), "confirmations");

        // Update file path
        externalInternship.setConfirmationFilePath(filePath);

        // Save changes
        ExternalInternship updatedExternalInternship = externalInternshipRepository.save(externalInternship);

        // Return as DTO
        return convertToDTO(updatedExternalInternship, token);
    }

    /**
     * Delete external internship (soft delete)
     *
     * @param id External internship ID
     */
    @Transactional
    public void deleteExternalInternship(Integer id) {
        ExternalInternship externalInternship = externalInternshipRepository.findById(id)
                .orElseThrow(() -> new ExternalInternshipNotFoundException("External internship not found with ID: " + id));

        if (externalInternship.getDeletedAt() != null) {
            throw new ExternalInternshipNotFoundException("External internship has already been deleted");
        }

        // Soft delete
        externalInternship.setDeletedAt(LocalDateTime.now());
        externalInternshipRepository.save(externalInternship);
    }

    /**
     * Create internship progress when external internship is approved
     *
     * @param externalInternship Approved external internship
     */
    private void createInternshipProgress(ExternalInternship externalInternship) {
        // Check if progress already exists
        progressRepository.findByStudentIdAndPeriodId(
                externalInternship.getStudentId(),
                externalInternship.getPeriod().getId()
        ).ifPresentOrElse(
                progress -> log.info("Internship progress already exists for student {} in period {}",
                        externalInternship.getStudentId(), externalInternship.getPeriod().getId()),
                () -> {
                    // Create new progress record
                    InternshipProgress progress = new InternshipProgress();
                    progress.setStudentId(externalInternship.getStudentId());
                    progress.setPeriod(externalInternship.getPeriod());
                    progress.setIsExternal(true);
                    progress.setExternal(externalInternship);
                    progress.setStartDate(externalInternship.getPeriod().getStartDate());
                    progress.setEndDate(externalInternship.getPeriod().getEndDate());
                    progress.setStatus(InternshipProgress.Status.IN_PROGRESS);

                    progressRepository.save(progress);
                    log.info("Created internship progress for approved external internship ID: {}", externalInternship.getId());
                }
        );
    }

    /**
     * Convert ExternalInternship to DTO
     *
     * @param externalInternship ExternalInternship entity
     * @param token Authorization token
     * @return CMSExternalInternshipDTO
     */
    private CMSExternalInternshipDTO convertToDTO(ExternalInternship externalInternship, String token) {
        CMSExternalInternshipDTO dto = new CMSExternalInternshipDTO();
        dto.setId(externalInternship.getId());
        dto.setStudentId(externalInternship.getStudentId());
        dto.setPeriodId(externalInternship.getPeriod().getId());
        dto.setConfirmationFilePath(externalInternship.getConfirmationFilePath());
        dto.setStatus(externalInternship.getStatus().name());
        dto.setCreatedAt(externalInternship.getCreatedAt());
        dto.setUpdatedAt(externalInternship.getUpdatedAt());

        // Get student details
        try {
            StudentDTO student = userServiceClient.getStudentById(externalInternship.getStudentId(), token);
            if (student != null) {
                dto.setStudentCode(student.getStudentCode());
                dto.setStudentName(student.getName());
                dto.setStudentEmail(student.getEmail());
                dto.setStudentPhone(student.getPhone());
            }
        } catch (Exception e) {
            log.error("Error fetching student details for ID {}: {}", externalInternship.getStudentId(), e.getMessage());
        }

        return dto;
    }
}
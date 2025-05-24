package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.CompanyDTO;
import com.dungnguyen.registration_service.dto.StudentDTO;
import com.dungnguyen.registration_service.dto.TeacherDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipProgressCreateDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipProgressDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipProgressUpdateDTO;
import com.dungnguyen.registration_service.entity.*;
import com.dungnguyen.registration_service.exception.*;
import com.dungnguyen.registration_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CMSInternshipProgressService {

    private final InternshipProgressRepository progressRepository;
    private final InternshipPeriodRepository periodRepository;
    private final InternshipPositionRepository positionRepository;
    private final ExternalInternshipRepository externalInternshipRepository;
    private final UserServiceClient userServiceClient;

    /**
     * Get all internship progress records
     *
     * @param token Authorization token
     * @return List of CMSInternshipProgressDTO
     */
    public List<CMSInternshipProgressDTO> getAllProgress(String token) {
        List<InternshipProgress> progressList = progressRepository.findAll();

        return progressList.stream()
                .filter(progress -> progress.getDeletedAt() == null)
                .map(progress -> convertToDTO(progress, token))
                .collect(Collectors.toList());
    }

    /**
     * Get progress by ID
     *
     * @param id Progress ID
     * @param token Authorization token
     * @return CMSInternshipProgressDTO
     */
    public CMSInternshipProgressDTO getProgressById(Integer id, String token) {
        InternshipProgress progress = progressRepository.findById(id)
                .orElseThrow(() -> new InternshipProgressNotFoundException("Internship progress not found with ID: " + id));

        if (progress.getDeletedAt() != null) {
            throw new InternshipProgressNotFoundException("Internship progress has been deleted");
        }

        return convertToDTO(progress, token);
    }

    /**
     * Create new internship progress
     *
     * @param createDTO Progress creation data
     * @param token Authorization token
     * @return Created CMSInternshipProgressDTO
     */
    @Transactional
    public CMSInternshipProgressDTO createProgress(CMSInternshipProgressCreateDTO createDTO, String token) {
        // Validate period exists
        InternshipPeriod period = periodRepository.findById(createDTO.getPeriodId())
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + createDTO.getPeriodId()));

        // Check if student already has progress for this period
        progressRepository.findByStudentIdAndPeriodId(createDTO.getStudentId(), createDTO.getPeriodId())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Student already has internship progress for this period");
                });

        // Create new progress
        InternshipProgress progress = new InternshipProgress();
        progress.setStudentId(createDTO.getStudentId());
        progress.setPeriod(period);
        progress.setTeacherId(createDTO.getTeacherId());
        progress.setStartDate(createDTO.getStartDate());
        progress.setEndDate(createDTO.getEndDate());
        progress.setIsExternal(createDTO.getIsExternal());

        // Set status
        if (createDTO.getStatus() != null) {
            progress.setStatus(InternshipProgress.Status.valueOf(createDTO.getStatus()));
        } else {
            progress.setStatus(InternshipProgress.Status.IN_PROGRESS);
        }

        // Handle internal vs external internship
        if (Boolean.TRUE.equals(createDTO.getIsExternal())) {
            // External internship
            if (createDTO.getExternalId() != null) {
                ExternalInternship external = externalInternshipRepository.findById(createDTO.getExternalId())
                        .orElseThrow(() -> new ExternalInternshipNotFoundException("External internship not found with ID: " + createDTO.getExternalId()));
                progress.setExternal(external);
            }
            progress.setCompanyName(createDTO.getCompanyName());
            progress.setPositionTitle(createDTO.getPositionTitle());
        } else {
            // Internal internship
            if (createDTO.getPositionId() == null) {
                throw new IllegalArgumentException("Position ID is required for internal internships");
            }
            InternshipPosition position = positionRepository.findById(createDTO.getPositionId())
                    .orElseThrow(() -> new InternshipPositionNotFoundException("Internship position not found with ID: " + createDTO.getPositionId()));
            progress.setPosition(position);
        }

        // Set supervisor details
        progress.setSupervisorName(createDTO.getSupervisorName());
        progress.setSupervisorPosition(createDTO.getSupervisorPosition());
        progress.setSupervisorEmail(createDTO.getSupervisorEmail());
        progress.setSupervisorPhone(createDTO.getSupervisorPhone());

        // Save progress
        InternshipProgress savedProgress = progressRepository.save(progress);

        return convertToDTO(savedProgress, token);
    }

    /**
     * Update internship progress
     * NOTE: Some fields cannot be updated for data integrity:
     * - studentId, periodId, isExternal, positionId, externalId
     *
     * @param id Progress ID
     * @param updateDTO Update data
     * @param token Authorization token
     * @return Updated CMSInternshipProgressDTO
     */
    @Transactional
    public CMSInternshipProgressDTO updateProgress(Integer id, CMSInternshipProgressUpdateDTO updateDTO, String token) {
        InternshipProgress progress = progressRepository.findById(id)
                .orElseThrow(() -> new InternshipProgressNotFoundException("Internship progress not found with ID: " + id));

        if (progress.getDeletedAt() != null) {
            throw new InternshipProgressNotFoundException("Internship progress has been deleted");
        }

        // Update fields if provided
        if (updateDTO.getTeacherId() != null) {
            progress.setTeacherId(updateDTO.getTeacherId());
        }

        if (updateDTO.getStartDate() != null) {
            progress.setStartDate(updateDTO.getStartDate());
        }

        if (updateDTO.getEndDate() != null) {
            progress.setEndDate(updateDTO.getEndDate());
        }

        if (updateDTO.getStatus() != null) {
            progress.setStatus(InternshipProgress.Status.valueOf(updateDTO.getStatus()));
        }

        // Update external internship fields ONLY if this is an external internship
        if (Boolean.TRUE.equals(progress.getIsExternal())) {
            if (updateDTO.getCompanyName() != null) {
                progress.setCompanyName(updateDTO.getCompanyName());
            }
            if (updateDTO.getPositionTitle() != null) {
                progress.setPositionTitle(updateDTO.getPositionTitle());
            }
        } else {
            // For internal internships, ignore company name and position title updates
            log.warn("Attempted to update company name/position title for internal internship ID: {}", id);
        }

        // Update supervisor details
        if (updateDTO.getSupervisorName() != null) {
            progress.setSupervisorName(updateDTO.getSupervisorName());
        }

        if (updateDTO.getSupervisorPosition() != null) {
            progress.setSupervisorPosition(updateDTO.getSupervisorPosition());
        }

        if (updateDTO.getSupervisorEmail() != null) {
            progress.setSupervisorEmail(updateDTO.getSupervisorEmail());
        }

        if (updateDTO.getSupervisorPhone() != null) {
            progress.setSupervisorPhone(updateDTO.getSupervisorPhone());
        }

        // Update teacher confirmation
        if (updateDTO.getTeacherConfirmed() != null) {
            progress.setTeacherConfirmed(updateDTO.getTeacherConfirmed());
            if (Boolean.TRUE.equals(updateDTO.getTeacherConfirmed())) {
                progress.setTeacherConfirmedAt(LocalDateTime.now());
            } else {
                progress.setTeacherConfirmedAt(null);
            }
        }

        // Save updated progress
        InternshipProgress updatedProgress = progressRepository.save(progress);

        return convertToDTO(updatedProgress, token);
    }

    /**
     * Delete internship progress (soft delete)
     *
     * @param id Progress ID
     */
    @Transactional
    public void deleteProgress(Integer id) {
        InternshipProgress progress = progressRepository.findById(id)
                .orElseThrow(() -> new InternshipProgressNotFoundException("Internship progress not found with ID: " + id));

        if (progress.getDeletedAt() != null) {
            throw new InternshipProgressNotFoundException("Internship progress has already been deleted");
        }

        // Soft delete
        progress.setDeletedAt(LocalDateTime.now());
        progressRepository.save(progress);
    }

    /**
     * Get progress by period ID
     *
     * @param periodId Period ID
     * @param token Authorization token
     * @return List of CMSInternshipProgressDTO
     */
    public List<CMSInternshipProgressDTO> getProgressByPeriod(String periodId, String token) {
        // Validate period exists
        periodRepository.findById(periodId)
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + periodId));

        List<InternshipProgress> progressList = progressRepository.findAll()
                .stream()
                .filter(progress -> progress.getDeletedAt() == null &&
                        progress.getPeriod().getId().equals(periodId))
                .collect(Collectors.toList());

        return progressList.stream()
                .map(progress -> convertToDTO(progress, token))
                .collect(Collectors.toList());
    }

    /**
     * Convert InternshipProgress to DTO
     *
     * @param progress InternshipProgress entity
     * @param token Authorization token
     * @return CMSInternshipProgressDTO
     */
    private CMSInternshipProgressDTO convertToDTO(InternshipProgress progress, String token) {
        CMSInternshipProgressDTO dto = new CMSInternshipProgressDTO();

        // Basic progress info
        dto.setId(progress.getId());
        dto.setStudentId(progress.getStudentId());
        dto.setPositionId(progress.getPosition() != null ? progress.getPosition().getId() : null);
        dto.setPeriodId(progress.getPeriod().getId());
        dto.setTeacherId(progress.getTeacherId());
        dto.setStartDate(progress.getStartDate());
        dto.setEndDate(progress.getEndDate());
        dto.setIsExternal(progress.getIsExternal());
        dto.setExternalId(progress.getExternal() != null ? progress.getExternal().getId() : null);
        dto.setStatus(progress.getStatus().name());
        dto.setSupervisorName(progress.getSupervisorName());
        dto.setSupervisorPosition(progress.getSupervisorPosition());
        dto.setSupervisorEmail(progress.getSupervisorEmail());
        dto.setSupervisorPhone(progress.getSupervisorPhone());
        dto.setTeacherConfirmed(progress.getTeacherConfirmed());
        dto.setTeacherConfirmedAt(progress.getTeacherConfirmedAt());
        dto.setCreatedAt(progress.getCreatedAt());
        dto.setUpdatedAt(progress.getUpdatedAt());

        // Handle external vs internal internship
        if (Boolean.TRUE.equals(progress.getIsExternal())) {
            dto.setCompanyName(progress.getCompanyName());
            dto.setExternalPositionTitle(progress.getPositionTitle());
            dto.setPositionTitle(progress.getPositionTitle()); // For external, use the custom title
        } else {
            // For internal internships, get position and company details
            if (progress.getPosition() != null) {
                dto.setPositionTitle(progress.getPosition().getTitle());

                // Get company details
                try {
                    CompanyDTO company = userServiceClient.getCompanyById(progress.getPosition().getCompanyId(), token);
                    if (company != null) {
                        dto.setInternalCompanyName(company.getName());
                    }
                } catch (Exception e) {
                    log.error("Error fetching company details for position {}: {}", progress.getPosition().getId(), e.getMessage());
                }
            }
        }

        // Get student details
        try {
            StudentDTO student = userServiceClient.getStudentById(progress.getStudentId(), token);
            if (student != null) {
                dto.setStudentCode(student.getStudentCode());
                dto.setStudentName(student.getName());
                dto.setStudentEmail(student.getEmail());
                dto.setStudentPhone(student.getPhone());
            }
        } catch (Exception e) {
            log.error("Error fetching student details for ID {}: {}", progress.getStudentId(), e.getMessage());
        }

        // Get teacher details
        if (progress.getTeacherId() != null) {
            try {
                TeacherDTO teacher = userServiceClient.getTeacherById(progress.getTeacherId(), token);
                if (teacher != null) {
                    dto.setTeacherName(teacher.getName());
                    dto.setTeacherEmail(teacher.getEmail());
                }
            } catch (Exception e) {
                log.error("Error fetching teacher details for ID {}: {}", progress.getTeacherId(), e.getMessage());
            }
        }

        return dto;
    }
}
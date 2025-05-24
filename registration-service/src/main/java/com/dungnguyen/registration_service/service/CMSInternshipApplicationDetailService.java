package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.CompanyDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipApplicationDetailCreateDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipApplicationDetailDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipApplicationDetailUpdateDTO;
import com.dungnguyen.registration_service.entity.InternshipApplication;
import com.dungnguyen.registration_service.entity.InternshipApplicationDetail;
import com.dungnguyen.registration_service.entity.InternshipPosition;
import com.dungnguyen.registration_service.exception.InternshipApplicationDetailNotFoundException;
import com.dungnguyen.registration_service.exception.InternshipApplicationNotFoundException;
import com.dungnguyen.registration_service.exception.InternshipPositionNotFoundException;
import com.dungnguyen.registration_service.repository.InternshipApplicationDetailRepository;
import com.dungnguyen.registration_service.repository.InternshipApplicationRepository;
import com.dungnguyen.registration_service.repository.InternshipPositionRepository;
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
public class CMSInternshipApplicationDetailService {

    private final InternshipApplicationDetailRepository applicationDetailRepository;
    private final InternshipApplicationRepository applicationRepository;
    private final InternshipPositionRepository positionRepository;
    private final UserServiceClient userServiceClient;

    public List<CMSInternshipApplicationDetailDTO> getAllApplicationDetails() {
        List<InternshipApplicationDetail> applicationDetails = applicationDetailRepository.findAll();
        return applicationDetails.stream()
                .filter(detail -> detail.getDeletedAt() == null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CMSInternshipApplicationDetailDTO getApplicationDetailById(Integer id) {
        InternshipApplicationDetail applicationDetail = applicationDetailRepository.findById(id)
                .orElseThrow(() -> new InternshipApplicationDetailNotFoundException("Application detail not found with ID: " + id));

        if (applicationDetail.getDeletedAt() != null) {
            throw new InternshipApplicationDetailNotFoundException("Application detail not found with ID: " + id);
        }

        return convertToDTO(applicationDetail);
    }

    public List<CMSInternshipApplicationDetailDTO> getApplicationDetailsByApplicationId(Integer applicationId) {
        // Verify application exists
        InternshipApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new InternshipApplicationNotFoundException("Internship application not found with ID: " + applicationId));

        return application.getDetails().stream()
                .filter(detail -> detail.getDeletedAt() == null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CMSInternshipApplicationDetailDTO createApplicationDetail(CMSInternshipApplicationDetailCreateDTO createDTO) {
        // Validate if application exists
        InternshipApplication application = applicationRepository.findById(createDTO.getApplicationId())
                .orElseThrow(() -> new InternshipApplicationNotFoundException("Internship application not found with ID: " + createDTO.getApplicationId()));

        // Check if application already has 3 details (maximum allowed)
        long activeDetailsCount = application.getDetails().stream()
                .filter(detail -> detail.getDeletedAt() == null)
                .count();

        if (activeDetailsCount >= 3) {
            throw new IllegalArgumentException("Each application can have maximum 3 preferences");
        }

        // Validate if position exists
        InternshipPosition position = positionRepository.findById(createDTO.getPositionId())
                .orElseThrow(() -> new InternshipPositionNotFoundException("Internship position not found with ID: " + createDTO.getPositionId()));

        // Validate if position belongs to the same period as application
        if (!position.getPeriod().getId().equals(application.getPeriod().getId())) {
            throw new IllegalArgumentException("Position does not belong to the same period as the application");
        }

        // Check if preference order already exists for this application
        boolean orderExists = application.getDetails().stream()
                .anyMatch(detail -> detail.getPreferenceOrder().equals(createDTO.getPreferenceOrder())
                        && detail.getDeletedAt() == null);

        if (orderExists) {
            throw new IllegalArgumentException("Preference order " + createDTO.getPreferenceOrder() + " already exists for this application");
        }

        // Check if position already exists for this application
        boolean positionExists = application.getDetails().stream()
                .anyMatch(detail -> detail.getPosition().getId().equals(createDTO.getPositionId())
                        && detail.getDeletedAt() == null);

        if (positionExists) {
            throw new IllegalArgumentException("This position has already been selected for this application");
        }

        // Validate preference order is within allowed range (1-3)
        if (createDTO.getPreferenceOrder() < 1 || createDTO.getPreferenceOrder() > 3) {
            throw new IllegalArgumentException("Preference order must be between 1 and 3");
        }

        // Create new application detail
        InternshipApplicationDetail applicationDetail = new InternshipApplicationDetail();
        applicationDetail.setApplication(application);
        applicationDetail.setPosition(position);
        applicationDetail.setPreferenceOrder(createDTO.getPreferenceOrder());
        applicationDetail.setNote(createDTO.getNote());

        // Set status
        if (createDTO.getStatus() != null) {
            applicationDetail.setStatus(InternshipApplicationDetail.Status.valueOf(createDTO.getStatus()));
        } else {
            applicationDetail.setStatus(InternshipApplicationDetail.Status.PENDING);
        }

        InternshipApplicationDetail savedApplicationDetail = applicationDetailRepository.save(applicationDetail);
        return convertToDTO(savedApplicationDetail);
    }

    @Transactional
    public CMSInternshipApplicationDetailDTO updateApplicationDetail(Integer id, CMSInternshipApplicationDetailUpdateDTO updateDTO) {
        InternshipApplicationDetail applicationDetail = applicationDetailRepository.findById(id)
                .orElseThrow(() -> new InternshipApplicationDetailNotFoundException("Application detail not found with ID: " + id));

        if (applicationDetail.getDeletedAt() != null) {
            throw new InternshipApplicationDetailNotFoundException("Application detail not found with ID: " + id);
        }

        // Update fields if provided
        if (updateDTO.getPreferenceOrder() != null) {
            // Validate preference order is within allowed range (1-3)
            if (updateDTO.getPreferenceOrder() < 1 || updateDTO.getPreferenceOrder() > 3) {
                throw new IllegalArgumentException("Preference order must be between 1 and 3");
            }

            // Check if new preference order conflicts with existing ones
            boolean orderExists = applicationDetail.getApplication().getDetails().stream()
                    .anyMatch(detail -> !detail.getId().equals(id)
                            && detail.getPreferenceOrder().equals(updateDTO.getPreferenceOrder())
                            && detail.getDeletedAt() == null);

            if (orderExists) {
                throw new IllegalArgumentException("Preference order " + updateDTO.getPreferenceOrder() + " already exists for this application");
            }

            applicationDetail.setPreferenceOrder(updateDTO.getPreferenceOrder());
        }

        // Note: We don't allow updating position to avoid duplicate position validation complexity
        // If position needs to be changed, delete this detail and create a new one

        if (updateDTO.getStatus() != null) {
            applicationDetail.setStatus(InternshipApplicationDetail.Status.valueOf(updateDTO.getStatus()));
        }

        if (updateDTO.getNote() != null) {
            applicationDetail.setNote(updateDTO.getNote());
        }

        InternshipApplicationDetail updatedApplicationDetail = applicationDetailRepository.save(applicationDetail);
        return convertToDTO(updatedApplicationDetail);
    }

    @Transactional
    public void deleteApplicationDetail(Integer id) {
        InternshipApplicationDetail applicationDetail = applicationDetailRepository.findById(id)
                .orElseThrow(() -> new InternshipApplicationDetailNotFoundException("Application detail not found with ID: " + id));

        if (applicationDetail.getDeletedAt() != null) {
            throw new InternshipApplicationDetailNotFoundException("Application detail not found with ID: " + id);
        }

        // Soft delete
        applicationDetail.setDeletedAt(LocalDateTime.now());
        applicationDetailRepository.save(applicationDetail);
    }

    private CMSInternshipApplicationDetailDTO convertToDTO(InternshipApplicationDetail detail) {
        CMSInternshipApplicationDetailDTO dto = new CMSInternshipApplicationDetailDTO();
        dto.setId(detail.getId());
        dto.setApplicationId(detail.getApplication().getId());
        dto.setPositionId(detail.getPosition().getId());
        dto.setPositionTitle(detail.getPosition().getTitle());
        dto.setPreferenceOrder(detail.getPreferenceOrder());
        dto.setStatus(detail.getStatus().name());
        dto.setNote(detail.getNote());
        dto.setCreatedAt(detail.getCreatedAt());
        dto.setUpdatedAt(detail.getUpdatedAt());

        // Get company name from position
        try {
            CompanyDTO company = userServiceClient.getCompanyById(detail.getPosition().getCompanyId(), null);
            dto.setCompanyName(company != null ? company.getName() : "Unknown Company");
        } catch (Exception e) {
            log.warn("Could not fetch company details for ID: {}", detail.getPosition().getCompanyId());
            dto.setCompanyName("Unknown Company");
        }

        return dto;
    }
}
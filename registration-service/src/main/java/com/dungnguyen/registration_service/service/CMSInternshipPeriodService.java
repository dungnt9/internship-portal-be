package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.dto.cms.CMSInternshipPeriodCreateDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipPeriodDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipPeriodUpdateDTO;
import com.dungnguyen.registration_service.entity.InternshipPeriod;
import com.dungnguyen.registration_service.exception.InternshipPeriodNotFoundException;
import com.dungnguyen.registration_service.repository.InternshipPeriodRepository;
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
public class CMSInternshipPeriodService {

    private final InternshipPeriodRepository internshipPeriodRepository;

    public List<CMSInternshipPeriodDTO> getAllPeriods() {
        List<InternshipPeriod> periods = internshipPeriodRepository.findAll();
        return periods.stream()
                .filter(period -> period.getDeletedAt() == null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CMSInternshipPeriodDTO getPeriodById(String id) {
        InternshipPeriod period = internshipPeriodRepository.findById(id)
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + id));

        if (period.getDeletedAt() != null) {
            throw new InternshipPeriodNotFoundException("Internship period not found with ID: " + id);
        }

        return convertToDTO(period);
    }

    @Transactional
    public CMSInternshipPeriodDTO createPeriod(CMSInternshipPeriodCreateDTO createDTO) {
        // Validate period ID format (YYYY.S)
        validatePeriodIdFormat(createDTO.getId());

        // Check if period with this ID already exists
        if (internshipPeriodRepository.existsById(createDTO.getId())) {
            throw new IllegalArgumentException("Internship period with ID " + createDTO.getId() + " already exists");
        }

        // Validate dates
        validateDates(createDTO);

        // Create new period
        InternshipPeriod period = new InternshipPeriod();
        period.setId(createDTO.getId());
        period.setStartDate(createDTO.getStartDate());
        period.setEndDate(createDTO.getEndDate());
        period.setRegistrationStartDate(createDTO.getRegistrationStartDate());
        period.setRegistrationEndDate(createDTO.getRegistrationEndDate());
        period.setDescription(createDTO.getDescription());

        // Set status
        if (createDTO.getStatus() != null) {
            period.setStatus(InternshipPeriod.Status.valueOf(createDTO.getStatus()));
        } else {
            period.setStatus(InternshipPeriod.Status.UPCOMING);
        }

        InternshipPeriod savedPeriod = internshipPeriodRepository.save(period);
        return convertToDTO(savedPeriod);
    }

    @Transactional
    public CMSInternshipPeriodDTO updatePeriod(String id, CMSInternshipPeriodUpdateDTO updateDTO) {
        InternshipPeriod period = internshipPeriodRepository.findById(id)
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + id));

        if (period.getDeletedAt() != null) {
            throw new InternshipPeriodNotFoundException("Internship period not found with ID: " + id);
        }

        // Update fields if provided
        if (updateDTO.getStartDate() != null) {
            period.setStartDate(updateDTO.getStartDate());
        }

        if (updateDTO.getEndDate() != null) {
            period.setEndDate(updateDTO.getEndDate());
        }

        if (updateDTO.getRegistrationStartDate() != null) {
            period.setRegistrationStartDate(updateDTO.getRegistrationStartDate());
        }

        if (updateDTO.getRegistrationEndDate() != null) {
            period.setRegistrationEndDate(updateDTO.getRegistrationEndDate());
        }

        if (updateDTO.getDescription() != null) {
            period.setDescription(updateDTO.getDescription());
        }

        if (updateDTO.getStatus() != null) {
            period.setStatus(InternshipPeriod.Status.valueOf(updateDTO.getStatus()));
        }

        // Validate dates after update
        validateDatesForUpdate(period);

        InternshipPeriod updatedPeriod = internshipPeriodRepository.save(period);
        return convertToDTO(updatedPeriod);
    }

    @Transactional
    public void deletePeriod(String id) {
        InternshipPeriod period = internshipPeriodRepository.findById(id)
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + id));

        if (period.getDeletedAt() != null) {
            throw new InternshipPeriodNotFoundException("Internship period not found with ID: " + id);
        }

        // Soft delete
        period.setDeletedAt(LocalDateTime.now());
        internshipPeriodRepository.save(period);
    }

    private CMSInternshipPeriodDTO convertToDTO(InternshipPeriod period) {
        return new CMSInternshipPeriodDTO(
                period.getId(),
                period.getStartDate(),
                period.getEndDate(),
                period.getRegistrationStartDate(),
                period.getRegistrationEndDate(),
                period.getStatus().name(),
                period.getDescription(),
                period.getCreatedAt(),
                period.getUpdatedAt()
        );
    }

    private void validatePeriodIdFormat(String id) {
        if (id == null || !id.matches("\\d{4}\\.\\d+")) {
            throw new IllegalArgumentException("Period ID must be in format YYYY.S (e.g., 2024.1, 2024.2)");
        }
    }

    private void validateDates(CMSInternshipPeriodCreateDTO createDTO) {
        // Validate that start date is before end date
        if (createDTO.getStartDate().isAfter(createDTO.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        // Validate that registration start date is before registration end date
        if (createDTO.getRegistrationStartDate().isAfter(createDTO.getRegistrationEndDate())) {
            throw new IllegalArgumentException("Registration start date must be before registration end date");
        }

        // Validate that registration end date is before or equal to start date
        if (createDTO.getRegistrationEndDate().isAfter(createDTO.getStartDate())) {
            throw new IllegalArgumentException("Registration end date must be before or equal to internship start date");
        }
    }

    private void validateDatesForUpdate(InternshipPeriod period) {
        // Validate that start date is before end date
        if (period.getStartDate().isAfter(period.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        // Validate that registration start date is before registration end date
        if (period.getRegistrationStartDate().isAfter(period.getRegistrationEndDate())) {
            throw new IllegalArgumentException("Registration start date must be before registration end date");
        }

        // Validate that registration end date is before or equal to start date
        if (period.getRegistrationEndDate().isAfter(period.getStartDate())) {
            throw new IllegalArgumentException("Registration end date must be before or equal to internship start date");
        }
    }
}
package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.AuthServiceClient;
import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.*;
import com.dungnguyen.registration_service.entity.InternshipPeriod;
import com.dungnguyen.registration_service.entity.InternshipPosition;
import com.dungnguyen.registration_service.exception.InternshipPeriodNotFoundException;
import com.dungnguyen.registration_service.exception.InternshipPositionNotFoundException;
import com.dungnguyen.registration_service.repository.InternshipPeriodRepository;
import com.dungnguyen.registration_service.repository.InternshipPositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternshipPositionService {

    private final InternshipPositionRepository positionRepository;
    private final InternshipPeriodRepository periodRepository;
    private final UserServiceClient userServiceClient;
    private final AuthServiceClient authServiceClient;

    /**
     * Get all positions with company details (for admin only)
     *
     * @param token Authorization token
     * @return List of InternshipPositionDTO with company details
     */
    public List<InternshipPositionDTO> getAllPositionsWithCompanyDetails(String token) {
        // Get all active positions
        List<InternshipPosition> positions = positionRepository.findAllActivePositions();

        // Return empty list if no positions found
        if (positions.isEmpty()) {
            return new ArrayList<>();
        }

        // Extract unique company IDs from positions
        List<Integer> companyIds = positions.stream()
                .map(InternshipPosition::getCompanyId)
                .distinct()
                .collect(Collectors.toList());

        // Fetch all companies
        List<CompanyDTO> companies = userServiceClient.getAllCompanies(token);

        // Create map of company ID to CompanyDTO for faster lookups
        Map<Integer, CompanyDTO> companyMap = companies.stream()
                .collect(Collectors.toMap(CompanyDTO::getId, Function.identity()));

        // Convert positions to DTOs and enrich with company details
        return positions.stream()
                .map(position -> {
                    InternshipPositionDTO dto = new InternshipPositionDTO(position);

                    // Add company details if available
                    CompanyDTO company = companyMap.get(position.getCompanyId());
                    if (company != null) {
                        dto.setCompanyName(company.getName());
                        dto.setCompanyShortName(company.getShortName());
                        dto.setWebsite(company.getWebsite());
                        dto.setLogoPath(company.getLogoPath());
                        dto.setAddress(company.getAddress());
                        dto.setBusinessType(company.getBusinessType());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get positions by company ID (excluding positions from periods with END status)
     *
     * @param companyId Company ID
     * @param token Authorization token
     * @return List of InternshipPositionDTO with company details
     */
    public List<InternshipPositionDTO> getPositionsByCompany(Integer companyId, String token) {
        // Get positions by company ID (excluding positions from periods with END status)
        List<InternshipPosition> positions = positionRepository.findActivePositionsByCompanyId(companyId);

        // Return empty list if no positions found
        if (positions.isEmpty()) {
            return new ArrayList<>();
        }

        // Get company details
        CompanyDTO company = userServiceClient.getCompanyById(companyId, token);

        // Convert positions to DTOs and enrich with company details
        return positions.stream()
                .map(position -> {
                    InternshipPositionDTO dto = new InternshipPositionDTO(position);

                    // Add company details if available
                    if (company != null) {
                        dto.setCompanyName(company.getName());
                        dto.setCompanyShortName(company.getShortName());
                        dto.setWebsite(company.getWebsite());
                        dto.setLogoPath(company.getLogoPath());
                        dto.setAddress(company.getAddress());
                        dto.setBusinessType(company.getBusinessType());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get open positions by company ID for the current upcoming period
     * This is for all users to view available open positions
     *
     * @param companyId Company ID
     * @param token Authorization token
     * @return List of InternshipPositionDTO with company details
     */
    public List<InternshipPositionDTO> getOpenPositionsByCompanyForUpcomingPeriod(Integer companyId, String token) {
        // Get open positions by company ID for upcoming period
        List<InternshipPosition> positions = positionRepository.findOpenPositionsByCompanyIdForUpcomingPeriod(companyId);

        // Return empty list if no positions found
        if (positions.isEmpty()) {
            return new ArrayList<>();
        }

        // Get company details
        CompanyDTO company = userServiceClient.getCompanyById(companyId, token);

        // Convert positions to DTOs and enrich with company details
        return positions.stream()
                .map(position -> {
                    InternshipPositionDTO dto = new InternshipPositionDTO(position);

                    // Add company details if available
                    if (company != null) {
                        dto.setCompanyName(company.getName());
                        dto.setCompanyShortName(company.getShortName());
                        dto.setWebsite(company.getWebsite());
                        dto.setLogoPath(company.getLogoPath());
                        dto.setAddress(company.getAddress());
                        dto.setBusinessType(company.getBusinessType());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get positions by company ID for a specific period (for company contact's own positions)
     *
     * @param companyId Company ID
     * @param periodId Period ID
     * @param token Authorization token
     * @return List of InternshipPositionDTO with company details
     */
    public List<InternshipPositionDTO> getPositionsByCompanyAndPeriod(Integer companyId, String periodId, String token) {
        // Get positions by company ID and period ID
        List<InternshipPosition> positions = positionRepository.findByCompanyIdAndPeriodId(companyId, periodId);

        // Return empty list if no positions found
        if (positions.isEmpty()) {
            return new ArrayList<>();
        }

        // Get company details
        CompanyDTO company = userServiceClient.getCompanyById(companyId, token);

        // Convert positions to DTOs and enrich with company details
        return positions.stream()
                .map(position -> {
                    InternshipPositionDTO dto = new InternshipPositionDTO(position);

                    // Add company details if available
                    if (company != null) {
                        dto.setCompanyName(company.getName());
                        dto.setCompanyShortName(company.getShortName());
                        dto.setWebsite(company.getWebsite());
                        dto.setLogoPath(company.getLogoPath());
                        dto.setAddress(company.getAddress());
                        dto.setBusinessType(company.getBusinessType());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Create a new internship position for a company
     *
     * @param companyId Company ID
     * @param createDTO Position creation data
     * @return Created InternshipPositionDTO
     */
    @Transactional
    public InternshipPositionDTO createPosition(Integer companyId, InternshipPositionCreateDTO createDTO) {
        // Get the period
        InternshipPeriod period = periodRepository.findById(createDTO.getPeriodId())
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + createDTO.getPeriodId()));

        // Create new position
        InternshipPosition position = new InternshipPosition();
        position.setCompanyId(companyId);
        position.setPeriod(period);
        position.setTitle(createDTO.getTitle());
        position.setDescription(createDTO.getDescription());
        position.setRequirements(createDTO.getRequirements());
        position.setBenefits(createDTO.getBenefits());
        position.setAvailableSlots(createDTO.getAvailableSlots());
        position.setRegisteredCount(0);

        // Set work type
        if (createDTO.getWorkType() != null) {
            position.setWorkType(InternshipPosition.WorkType.valueOf(createDTO.getWorkType()));
        }

        // Set status to OPEN by default for new positions
        position.setStatus(InternshipPosition.Status.OPEN);

        position.setDueDate(createDTO.getDueDate());
        position.setDeletedAt(null);

        // Save position
        InternshipPosition savedPosition = positionRepository.save(position);

        // Return as DTO
        return new InternshipPositionDTO(savedPosition);
    }

    /**
     * Update an existing internship position for a company
     *
     * @param companyId Company ID
     * @param positionId Position ID
     * @param updateDTO Position update data
     * @return Updated InternshipPositionDTO
     */
    @Transactional
    public InternshipPositionDTO updatePosition(Integer companyId, Integer positionId, InternshipPositionUpdateDTO updateDTO) {
        // Get the position, ensuring it belongs to the company
        InternshipPosition position = positionRepository.findByIdAndCompanyId(positionId, companyId)
                .orElseThrow(() -> new InternshipPositionNotFoundException("Internship position not found with ID: " + positionId + " for company ID: " + companyId));

        // Update position fields
        if (updateDTO.getTitle() != null) {
            position.setTitle(updateDTO.getTitle());
        }
        if (updateDTO.getDescription() != null) {
            position.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getRequirements() != null) {
            position.setRequirements(updateDTO.getRequirements());
        }
        if (updateDTO.getBenefits() != null) {
            position.setBenefits(updateDTO.getBenefits());
        }
        if (updateDTO.getAvailableSlots() != null) {
            position.setAvailableSlots(updateDTO.getAvailableSlots());
        }
        if (updateDTO.getWorkType() != null) {
            position.setWorkType(InternshipPosition.WorkType.valueOf(updateDTO.getWorkType()));
        }
        if (updateDTO.getStatus() != null) {
            position.setStatus(InternshipPosition.Status.valueOf(updateDTO.getStatus()));
        }
        if (updateDTO.getDueDate() != null) {
            position.setDueDate(updateDTO.getDueDate());
        }

        // Save updated position
        InternshipPosition updatedPosition = positionRepository.save(position);

        // Return as DTO
        return new InternshipPositionDTO(updatedPosition);
    }

    /**
     * Get the current user's company ID from token
     *
     * @param token Authorization token
     * @return Company ID
     */
    public Integer getCurrentUserCompanyId(String token) {
        // This would need to be implemented based on your auth service
        // It should extract the company ID from the token or get it from the user profile
        return authServiceClient.getUserCompanyId(token);
    }
}

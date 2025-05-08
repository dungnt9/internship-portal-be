package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.CompanyDTO;
import com.dungnguyen.registration_service.dto.InternshipPositionDTO;
import com.dungnguyen.registration_service.entity.InternshipPosition;
import com.dungnguyen.registration_service.repository.InternshipPositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private final UserServiceClient userServiceClient;

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
}
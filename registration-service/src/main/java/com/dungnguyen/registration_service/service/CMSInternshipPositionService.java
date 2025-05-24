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
public class CMSInternshipPositionService {

    private final InternshipPositionRepository positionRepository;
    private final UserServiceClient userServiceClient;

    /**
     * Get positions by period ID with company details
     *
     * @param periodId Period ID
     * @param token    Authorization token
     * @return List of InternshipPositionDTO with company details for the specified period
     */
    public List<InternshipPositionDTO> getPositionsByPeriod(String periodId, String token) {
        // Get positions by period ID
        List<InternshipPosition> positions = positionRepository.findByPeriodId(periodId);

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
}
package com.dungnguyen.user_service.service;

import com.dungnguyen.user_service.client.AuthServiceClient;
import com.dungnguyen.user_service.dto.CompanyDTO;
import com.dungnguyen.user_service.dto.CompanyUpdateDTO;
import com.dungnguyen.user_service.entity.Company;
import com.dungnguyen.user_service.entity.CompanyContact;
import com.dungnguyen.user_service.exception.CompanyNotFoundException;
import com.dungnguyen.user_service.repository.CompanyContactRepository;
import com.dungnguyen.user_service.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyContactRepository companyContactRepository;
    private final AuthServiceClient authServiceClient;

    public CompanyDTO getCompanyById(Integer id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with ID: " + id));

        return new CompanyDTO(company);
    }

    public List<CompanyDTO> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(CompanyDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CompanyDTO updateCompany(Integer id, CompanyUpdateDTO updateDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with ID: " + id));

        updateCompanyFields(company, updateDTO);

        Company updatedCompany = companyRepository.save(company);
        return new CompanyDTO(updatedCompany);
    }

    // Get company of the current company contact user
    public CompanyDTO getMyCompany(Integer authUserId) {
        CompanyContact contact = companyContactRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new IllegalArgumentException("You are not a company contact"));

        return new CompanyDTO(contact.getCompany());
    }

    // Update company of the current company contact user
    @Transactional
    public CompanyDTO updateMyCompany(Integer authUserId, CompanyUpdateDTO updateDTO) {
        CompanyContact contact = companyContactRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new IllegalArgumentException("You are not a company contact"));

        Company company = contact.getCompany();
        updateCompanyFields(company, updateDTO);

        Company updatedCompany = companyRepository.save(company);
        return new CompanyDTO(updatedCompany);
    }

    public Integer getCurrentUserAuthId(String token) {
        return authServiceClient.getUserIdFromToken(token);
    }

    // Common method to update company fields
    private void updateCompanyFields(Company company, CompanyUpdateDTO updateDTO) {
        if (updateDTO.getName() != null) {
            company.setName(updateDTO.getName());
        }
        if (updateDTO.getShortName() != null) {
            company.setShortName(updateDTO.getShortName());
        }
        if (updateDTO.getIsForeignCompany() != null) {
            company.setIsForeignCompany(updateDTO.getIsForeignCompany());
        }
        if (updateDTO.getTaxCode() != null) {
            company.setTaxCode(updateDTO.getTaxCode());
        }
        if (updateDTO.getWebsite() != null) {
            company.setWebsite(updateDTO.getWebsite());
        }
        if (updateDTO.getAddress() != null) {
            company.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getBusinessType() != null) {
            company.setBusinessType(updateDTO.getBusinessType());
        }
        if (updateDTO.getDescription() != null) {
            company.setDescription(updateDTO.getDescription());
        }
        if (updateDTO.getFoundingYear() != null) {
            company.setFoundingYear(updateDTO.getFoundingYear());
        }
        if (updateDTO.getEmployeeCount() != null) {
            company.setEmployeeCount(updateDTO.getEmployeeCount());
        }
        if (updateDTO.getCapital() != null) {
            company.setCapital(updateDTO.getCapital());
        }
    }
}
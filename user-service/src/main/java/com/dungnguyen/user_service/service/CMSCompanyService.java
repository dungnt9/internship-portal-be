package com.dungnguyen.user_service.service;

import com.dungnguyen.user_service.dto.cms.CMSCompanyCreateDTO;
import com.dungnguyen.user_service.dto.cms.CMSCompanyDTO;
import com.dungnguyen.user_service.dto.cms.CMSCompanyUpdateDTO;
import com.dungnguyen.user_service.entity.Company;
import com.dungnguyen.user_service.exception.CompanyNotFoundException;
import com.dungnguyen.user_service.repository.CMSCompanyRepository ;
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
public class CMSCompanyService {

    private final CMSCompanyRepository cmsCompanyRepository;

    public List<CMSCompanyDTO> getAllCompanies() {
        return cmsCompanyRepository.findAll().stream()
                .map(CMSCompanyDTO::new)
                .collect(Collectors.toList());
    }

    public CMSCompanyDTO getCompanyById(Integer id) {
        Company company = cmsCompanyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with ID: " + id));

        return new CMSCompanyDTO(company);
    }

    @Transactional
    public CMSCompanyDTO createCompany(CMSCompanyCreateDTO createDTO) {
        Company company = new Company();

        // Set company properties from DTO
        updateCompanyFromDTO(company, createDTO);

        // Set default values for new companies
        if (company.getIsVerified() == null) {
            company.setIsVerified(false);
        }

        if (company.getIsLinked() == null) {
            company.setIsLinked(false);
        }

        // If company is verified, set verification date
        if (Boolean.TRUE.equals(company.getIsVerified())) {
            company.setVerificationDate(LocalDateTime.now());
        }

        Company savedCompany = cmsCompanyRepository.save(company);
        return new CMSCompanyDTO(savedCompany);
    }

    @Transactional
    public CMSCompanyDTO updateCompany(Integer id, CMSCompanyUpdateDTO updateDTO) {
        Company company = cmsCompanyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found with ID: " + id));

        // Update company properties from DTO
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

        // Check if verification status is changing
        if (updateDTO.getIsVerified() != null && !updateDTO.getIsVerified().equals(company.getIsVerified())) {
            company.setIsVerified(updateDTO.getIsVerified());

            // If becoming verified, set verification date
            if (Boolean.TRUE.equals(updateDTO.getIsVerified())) {
                company.setVerificationDate(LocalDateTime.now());
            } else {
                company.setVerificationDate(null);
            }
        }

        // Allow explicit setting of verification date (for admin/migration purposes)
        if (updateDTO.getVerificationDate() != null) {
            company.setVerificationDate(updateDTO.getVerificationDate());
        }

        if (updateDTO.getIsLinked() != null) {
            company.setIsLinked(updateDTO.getIsLinked());
        }

        Company updatedCompany = cmsCompanyRepository.save(company);
        return new CMSCompanyDTO(updatedCompany);
    }

    private void updateCompanyFromDTO(Company company, CMSCompanyCreateDTO dto) {
        company.setName(dto.getName());
        company.setShortName(dto.getShortName());
        company.setIsForeignCompany(dto.getIsForeignCompany());
        company.setTaxCode(dto.getTaxCode());
        company.setWebsite(dto.getWebsite());
        company.setAddress(dto.getAddress());
        company.setBusinessType(dto.getBusinessType());
        company.setDescription(dto.getDescription());
        company.setFoundingYear(dto.getFoundingYear());
        company.setEmployeeCount(dto.getEmployeeCount());
        company.setCapital(dto.getCapital());
        company.setIsVerified(dto.getIsVerified());
        company.setIsLinked(dto.getIsLinked());
    }
}
package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.*;
import com.dungnguyen.registration_service.entity.InternshipProgress;
import com.dungnguyen.registration_service.exception.InternshipProgressNotFoundException;
import com.dungnguyen.registration_service.repository.InternshipProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CMSProgressService {

    private final InternshipProgressRepository progressRepository;
    private final UserServiceClient userServiceClient;

    /**
     * Get progress detail by ID for CMS
     */
    public InternshipProgressDetailDTO getProgressDetail(Integer progressId) {
        InternshipProgress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new InternshipProgressNotFoundException("Progress not found with ID: " + progressId));

        InternshipProgressDetailDTO detailDTO = new InternshipProgressDetailDTO();

        // Set progress data
        InternshipProgressDTO progressDTO = new InternshipProgressDTO(progress);
        detailDTO.setProgress(progressDTO);

        // Set student data (no token needed for CMS)
        StudentDTO studentDTO = userServiceClient.getStudentById(progress.getStudentId(), null);
        detailDTO.setStudent(studentDTO);

        // Set company and position data based on internship type
        if (!progress.getIsExternal() && progress.getPosition() != null) {
            // For internal internships, get company from position
            CompanyDTO company = userServiceClient.getCompanyById(progress.getPosition().getCompanyId(), null);
            detailDTO.setCompany(company);

            // Set position details
            InternshipPositionDTO positionDTO = new InternshipPositionDTO(progress.getPosition());
            detailDTO.setPosition(positionDTO);
        } else if (progress.getIsExternal()) {
            // For external internships, create a custom CompanyDTO
            CompanyDTO externalCompanyDTO = new CompanyDTO();
            externalCompanyDTO.setName(progress.getCompanyName());
            detailDTO.setCompany(externalCompanyDTO);

            // Create a custom position DTO
            InternshipPositionDTO externalPositionDTO = new InternshipPositionDTO();
            externalPositionDTO.setTitle(progress.getPositionTitle());
            externalPositionDTO.setCompanyName(progress.getCompanyName());
            detailDTO.setPosition(externalPositionDTO);
        }

        return detailDTO;
    }
}
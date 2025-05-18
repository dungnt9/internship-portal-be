package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.AuthServiceClient;
import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.InternshipPositionDTO;
import com.dungnguyen.registration_service.dto.InternshipProgressDTO;
import com.dungnguyen.registration_service.dto.InternshipProgressDetailDTO;
import com.dungnguyen.registration_service.dto.StudentDTO;
import com.dungnguyen.registration_service.entity.InternshipPosition;
import com.dungnguyen.registration_service.entity.InternshipProgress;
import com.dungnguyen.registration_service.exception.UnauthorizedAccessException;
import com.dungnguyen.registration_service.repository.InternshipProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyProgressService {

    private final InternshipProgressRepository progressRepository;
    private final AuthServiceClient authServiceClient;
    private final UserServiceClient userServiceClient;

    /**
     * Get internship progress for a company with optional period filtering
     *
     * @param periodId Optional period ID filter
     * @param token Authorization token
     * @return List of InternshipProgressDetailDTO
     */
    public List<InternshipProgressDetailDTO> getCompanyProgress(String periodId, String token) {
        // Get current company ID from token
        Integer companyId = authServiceClient.getUserCompanyId(token);
        if (companyId == null) {
            throw new UnauthorizedAccessException("Could not determine company from authorization token");
        }

        // Get progress based on filter
        List<InternshipProgress> progressList;
        if (periodId != null && !periodId.isEmpty()) {
            progressList = progressRepository.findByCompanyIdAndPeriodId(companyId, periodId);
        } else {
            progressList = progressRepository.findByCompanyId(companyId);
        }

        // Convert to DTOs with student details
        List<InternshipProgressDetailDTO> resultList = new ArrayList<>();

        for (InternshipProgress progress : progressList) {
            InternshipProgressDetailDTO detailDTO = new InternshipProgressDetailDTO();

            // Set progress data
            InternshipProgressDTO progressDTO = new InternshipProgressDTO(progress);
            detailDTO.setProgress(progressDTO);

            // Set student data
            StudentDTO studentDTO = userServiceClient.getStudentById(progress.getStudentId(), token);
            detailDTO.setStudent(studentDTO);

            // Add position and company details if needed
            if (!progress.getIsExternal() && progress.getPosition() != null) {
                InternshipPosition position = progress.getPosition();
                detailDTO.setPosition(new InternshipPositionDTO(position));

                // Company details could be added here if needed
                // CompanyDTO companyDTO = userServiceClient.getCompanyById(position.getCompanyId(), token);
                // detailDTO.setCompany(companyDTO);
            }

            // Add to result list
            resultList.add(detailDTO);
        }

        return resultList;
    }
}
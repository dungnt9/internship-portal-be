package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.AuthServiceClient;
import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.InternshipProgressDTO;
import com.dungnguyen.registration_service.dto.StudentDTO;
import com.dungnguyen.registration_service.entity.InternshipProgress;
import com.dungnguyen.registration_service.exception.InternshipProgressNotFoundException;
import com.dungnguyen.registration_service.exception.UnauthorizedAccessException;
import com.dungnguyen.registration_service.repository.InternshipProgressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
     * @return List of InternshipProgressDTO
     */
    public List<InternshipProgressDTO> getCompanyProgress(String periodId, String token) {
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

        // Convert to DTOs and enrich with student details
        return progressList.stream()
                .map(progress -> {
                    InternshipProgressDTO dto = new InternshipProgressDTO(progress);

                    // Get student details
                    StudentDTO studentDTO = userServiceClient.getStudentById(progress.getStudentId(), token);
                    if (studentDTO != null) {
                        dto.setStudentCode(studentDTO.getStudentCode());
                        dto.setStudentName(studentDTO.getName());
                        dto.setEmail(studentDTO.getEmail());
                        dto.setPhone(studentDTO.getPhone());
                        dto.setClassName(studentDTO.getClassName());
                        dto.setMajor(studentDTO.getMajor());
                        dto.setGender(studentDTO.getGender());
                        dto.setBirthday(studentDTO.getBirthday());
                        dto.setCpa(studentDTO.getCpa());
                        dto.setEnglishLevel(studentDTO.getEnglishLevel());
                        dto.setSkills(studentDTO.getSkills());
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }
}
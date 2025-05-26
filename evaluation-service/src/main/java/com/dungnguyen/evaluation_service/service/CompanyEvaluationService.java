package com.dungnguyen.evaluation_service.service;

import com.dungnguyen.evaluation_service.client.AuthServiceClient;
import com.dungnguyen.evaluation_service.client.RegistrationServiceClient;
import com.dungnguyen.evaluation_service.dto.*;
import com.dungnguyen.evaluation_service.entity.*;
import com.dungnguyen.evaluation_service.exception.InternshipEvaluationNotFoundException;
import com.dungnguyen.evaluation_service.exception.UnauthorizedAccessException;
import com.dungnguyen.evaluation_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyEvaluationService {

    private final CompanyEvaluationRepository companyEvaluationRepository;
    private final CompanyEvaluationDetailRepository evaluationDetailRepository;
    private final EvaluationCriteriaRepository evaluationCriteriaRepository;
    private final AuthServiceClient authServiceClient;
    private final RestTemplate restTemplate;

    @Value("${services.registration.url:http://localhost:8003}")
    private String registrationServiceUrl;

    /**
     * Get all internships for the company with evaluation status
     */
    public List<CompanyInternshipEvaluationDTO> getCompanyInternships(String periodId, String token) {
        // Get current company ID from token
        Integer companyId = authServiceClient.getUserCompanyId(token);
        if (companyId == null) {
            throw new UnauthorizedAccessException("Could not determine company from authorization token");
        }

        // Call registration service to get company progress
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HttpHeaders.AUTHORIZATION, token);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            String url = registrationServiceUrl + "/company-progress";
            if (periodId != null && !periodId.isEmpty()) {
                url += "?periodId=" + periodId;
            }

            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Object.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> progressList = (List<Map<String, Object>>) responseBody.get("data");

                return progressList.stream().map(progressData -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> progress = (Map<String, Object>) progressData.get("progress");
                    @SuppressWarnings("unchecked")
                    Map<String, Object> student = (Map<String, Object>) progressData.get("student");

                    Integer progressId = (Integer) progress.get("id");

                    // Check if evaluation exists and get status
                    CompanyEvaluation evaluation = companyEvaluationRepository.findByProgressId(progressId).orElse(null);

                    CompanyInternshipEvaluationDTO dto = new CompanyInternshipEvaluationDTO();
                    dto.setProgressId(progressId);
                    dto.setStudentCode((String) student.get("studentCode"));
                    dto.setStudentName((String) student.get("name"));
                    dto.setPositionTitle((String) progress.get("positionTitle"));
                    dto.setStartDate((String) progress.get("startDate"));
                    dto.setEndDate((String) progress.get("endDate"));
                    dto.setStatus((String) progress.get("status"));

                    // Set evaluation status
                    if (evaluation != null && evaluation.getEvaluationDate() != null) {
                        dto.setEvaluationStatus("COMPLETED");
                        dto.setScore(evaluation.getScore());
                        dto.setEvaluationDate(evaluation.getEvaluationDate());
                    } else {
                        dto.setEvaluationStatus("PENDING");
                    }

                    return dto;
                }).collect(Collectors.toList());
            }

            return List.of();
        } catch (Exception e) {
            log.error("Error getting company internships: {}", e.getMessage());
            throw new RuntimeException("Failed to get company internships", e);
        }
    }

    /**
     * Get detailed evaluation for a specific progress
     */
    public CompanyEvaluationDetailResponseDTO getEvaluationDetail(Integer progressId, String token) {
        // Verify company access to this progress
        verifyCompanyAccessToProgress(progressId, token);

        // Get evaluation
        CompanyEvaluation evaluation = companyEvaluationRepository.findByProgressId(progressId)
                .orElseThrow(() -> new InternshipEvaluationNotFoundException("Evaluation not found for progress ID: " + progressId));

        // Get evaluation details with criteria
        List<CompanyEvaluationDetail> details = evaluationDetailRepository.findByEvaluationId(evaluation.getId());

        // Convert to response DTO
        CompanyEvaluationDetailResponseDTO responseDTO = new CompanyEvaluationDetailResponseDTO();
        responseDTO.setId(evaluation.getId());
        responseDTO.setProgressId(evaluation.getProgressId());
        responseDTO.setEvaluationDate(evaluation.getEvaluationDate());
        responseDTO.setScore(evaluation.getScore());
        responseDTO.setComments(evaluation.getComments());
        responseDTO.setCreatedAt(evaluation.getCreatedAt());
        responseDTO.setUpdatedAt(evaluation.getUpdatedAt());

        // Map criteria details
        List<EvaluationCriteriaDetailDTO> criteriaDetails = details.stream()
                .map(detail -> {
                    EvaluationCriteriaDetailDTO dto = new EvaluationCriteriaDetailDTO();
                    dto.setId(detail.getId());
                    dto.setCriteriaId(detail.getCriteria().getId());
                    dto.setCriteriaName(detail.getCriteria().getName());
                    dto.setCriteriaDescription(detail.getCriteria().getDescription());
                    dto.setComments(detail.getComments());
                    return dto;
                })
                .collect(Collectors.toList());

        responseDTO.setCriteriaDetails(criteriaDetails);

        return responseDTO;
    }

    /**
     * Update evaluation for a student
     */
    @Transactional
    public CompanyEvaluationDetailResponseDTO updateEvaluation(Integer progressId, CompanyEvaluationUpdateDTO updateDTO, String token) {
        // Verify company access to this progress
        verifyCompanyAccessToProgress(progressId, token);

        // Validate input
        if (updateDTO.getScore() != null && (updateDTO.getScore().compareTo(BigDecimal.ZERO) < 0 || updateDTO.getScore().compareTo(BigDecimal.TEN) > 0)) {
            throw new IllegalArgumentException("Score must be between 0 and 10");
        }

        // Get evaluation
        CompanyEvaluation evaluation = companyEvaluationRepository.findByProgressId(progressId)
                .orElseThrow(() -> new InternshipEvaluationNotFoundException("Evaluation not found for progress ID: " + progressId));

        // Update main evaluation fields
        if (updateDTO.getScore() != null) {
            evaluation.setScore(updateDTO.getScore());
        }
        if (updateDTO.getComments() != null) {
            evaluation.setComments(updateDTO.getComments());
        }
        evaluation.setEvaluationDate(LocalDateTime.now());

        CompanyEvaluation savedEvaluation = companyEvaluationRepository.save(evaluation);

        // Update criteria details if provided
        if (updateDTO.getCriteriaDetails() != null && !updateDTO.getCriteriaDetails().isEmpty()) {
            List<CompanyEvaluationDetail> existingDetails = evaluationDetailRepository.findByEvaluationId(evaluation.getId());

            for (EvaluationCriteriaUpdateDTO criteriaUpdate : updateDTO.getCriteriaDetails()) {
                CompanyEvaluationDetail detail = existingDetails.stream()
                        .filter(d -> d.getCriteria().getId().equals(criteriaUpdate.getCriteriaId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid criteria ID: " + criteriaUpdate.getCriteriaId()));

                if (criteriaUpdate.getComments() != null) {
                    detail.setComments(criteriaUpdate.getComments());
                    evaluationDetailRepository.save(detail);
                }
            }
        }

        // Return updated evaluation detail
        return getEvaluationDetail(progressId, token);
    }

    /**
     * Verify that the company has access to this progress
     */
    private void verifyCompanyAccessToProgress(Integer progressId, String token) {
        // Get current company ID from token
        Integer companyId = authServiceClient.getUserCompanyId(token);
        if (companyId == null) {
            throw new UnauthorizedAccessException("Could not determine company from authorization token");
        }

        // Call registration service to verify access
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(HttpHeaders.AUTHORIZATION, token);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            String url = registrationServiceUrl + "/company-progress";

            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Object.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> progressList = (List<Map<String, Object>>) responseBody.get("data");

                boolean hasAccess = progressList.stream().anyMatch(progressData -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> progress = (Map<String, Object>) progressData.get("progress");
                    return progressId.equals(progress.get("id"));
                });

                if (!hasAccess) {
                    throw new UnauthorizedAccessException("Company does not have access to this progress");
                }
            } else {
                throw new UnauthorizedAccessException("Cannot verify company access to progress");
            }
        } catch (Exception e) {
            log.error("Error verifying company access to progress {}: {}", progressId, e.getMessage());
            throw new UnauthorizedAccessException("Cannot verify company access to progress");
        }
    }
}
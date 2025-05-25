// Backend: Cập nhật StudentEvaluationService.java
package com.dungnguyen.evaluation_service.service;

import com.dungnguyen.evaluation_service.client.AuthServiceClient;
import com.dungnguyen.evaluation_service.client.RegistrationServiceClient;
import com.dungnguyen.evaluation_service.dto.*;
import com.dungnguyen.evaluation_service.entity.*;
import com.dungnguyen.evaluation_service.exception.InternshipReportNotFoundException;
import com.dungnguyen.evaluation_service.exception.UnauthorizedAccessException;
import com.dungnguyen.evaluation_service.exception.ReportAlreadySubmittedException;
import com.dungnguyen.evaluation_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentEvaluationService {

    private final InternshipReportRepository reportRepository;
    private final CompanyEvaluationRepository companyEvaluationRepository;
    private final CompanyEvaluationDetailRepository evaluationDetailRepository;
    private final AuthServiceClient authServiceClient;
    private final RegistrationServiceClient registrationServiceClient;

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    /**
     * Get internship report for current student
     */
    public InternshipReportDTO getMyReport(String token) {
        Integer studentId = authServiceClient.getUserStudentId(token);
        if (studentId == null) {
            throw new UnauthorizedAccessException("Could not determine student from authorization token");
        }

        // Get current progress ID from registration service
        Integer progressId = registrationServiceClient.getCurrentProgressIdForStudent(studentId, token);
        if (progressId == null) {
            throw new IllegalStateException("No active internship progress found for student");
        }

        InternshipReport report = reportRepository.findByProgressIdAndDeletedAtIsNull(progressId)
                .orElseThrow(() -> new InternshipReportNotFoundException("Report not found for current internship"));

        return new InternshipReportDTO(report);
    }

    /**
     * Update internship report for current student
     * UPDATED: Check if report is already submitted
     */
    @Transactional
    public InternshipReportDTO updateMyReport(String token, InternshipReportUpdateDTO updateDTO, MultipartFile file) {
        Integer studentId = authServiceClient.getUserStudentId(token);
        if (studentId == null) {
            throw new UnauthorizedAccessException("Could not determine student from authorization token");
        }

        // Get current progress ID from registration service
        Integer progressId = registrationServiceClient.getCurrentProgressIdForStudent(studentId, token);
        if (progressId == null) {
            throw new IllegalStateException("No active internship progress found for student");
        }

        InternshipReport report = reportRepository.findByProgressIdAndDeletedAtIsNull(progressId)
                .orElseThrow(() -> new InternshipReportNotFoundException("Report not found for current internship"));

        // NEW: Check if report is already submitted
        if (isReportSubmitted(report)) {
            throw new ReportAlreadySubmittedException("Báo cáo đã được nộp và không thể chỉnh sửa. Vui lòng liên hệ giảng viên hướng dẫn nếu cần hỗ trợ.");
        }

        // Update text fields
        if (updateDTO.getTitle() != null && !updateDTO.getTitle().trim().isEmpty()) {
            report.setTitle(updateDTO.getTitle().trim());
        }

        if (updateDTO.getContent() != null && !updateDTO.getContent().trim().isEmpty()) {
            report.setContent(updateDTO.getContent().trim());
        }

        // Handle file upload if provided
        if (file != null && !file.isEmpty()) {
            try {
                String filePath = saveReportFile(file, studentId, progressId);
                report.setFilePath(filePath);
            } catch (IOException e) {
                log.error("Error saving report file: {}", e.getMessage());
                throw new RuntimeException("Failed to save report file", e);
            }
        }

        // Set submission date if content or file is provided
        if ((updateDTO.getTitle() != null && !updateDTO.getTitle().trim().isEmpty()) ||
                (updateDTO.getContent() != null && !updateDTO.getContent().trim().isEmpty()) ||
                (file != null && !file.isEmpty())) {
            report.setSubmissionDate(LocalDateTime.now());
        }

        InternshipReport updatedReport = reportRepository.save(report);
        return new InternshipReportDTO(updatedReport);
    }

    /**
     * Get company evaluation for current student
     */
    public CompanyEvaluationResponseDTO getMyEvaluation(String token) {
        Integer studentId = authServiceClient.getUserStudentId(token);
        if (studentId == null) {
            throw new UnauthorizedAccessException("Could not determine student from authorization token");
        }

        // Get current progress ID from registration service
        Integer progressId = registrationServiceClient.getCurrentProgressIdForStudent(studentId, token);
        if (progressId == null) {
            throw new IllegalStateException("No active internship progress found for student");
        }

        CompanyEvaluation evaluation = companyEvaluationRepository.findByProgressId(progressId)
                .orElse(null);

        if (evaluation == null) {
            // Return null - evaluation might not exist yet
            return null;
        }

        // Get evaluation details with criteria information
        List<CompanyEvaluationDetail> details = evaluationDetailRepository.findByEvaluationId(evaluation.getId());

        List<EvaluationDetailDTO> detailDTOs = details.stream()
                .map(detail -> new EvaluationDetailDTO(
                        detail.getId(),
                        detail.getCriteria().getName(),
                        detail.getCriteria().getDescription(),
                        detail.getComments()
                ))
                .collect(Collectors.toList());

        CompanyEvaluationResponseDTO responseDTO = new CompanyEvaluationResponseDTO();
        responseDTO.setId(evaluation.getId());
        responseDTO.setProgressId(evaluation.getProgressId());
        responseDTO.setEvaluationDate(evaluation.getEvaluationDate());
        responseDTO.setScore(evaluation.getScore());
        responseDTO.setComments(evaluation.getComments());
        responseDTO.setDetails(detailDTOs);
        responseDTO.setCreatedAt(evaluation.getCreatedAt());
        responseDTO.setUpdatedAt(evaluation.getUpdatedAt());

        return responseDTO;
    }

    /**
     * NEW: Check if report is considered submitted
     * A report is considered submitted if it has:
     * 1. Both title and content filled
     * 2. AND has a submission date
     */
    private boolean isReportSubmitted(InternshipReport report) {
        return report.getSubmissionDate() != null &&
                report.getTitle() != null && !report.getTitle().trim().isEmpty() &&
                report.getContent() != null && !report.getContent().trim().isEmpty();
    }

    /**
     * Helper method to save uploaded report file
     */
    private String saveReportFile(MultipartFile file, Integer studentId, Integer progressId) throws IOException {
        // Create directory structure
        String studentDir = "reports/student_" + studentId;
        Path uploadPath = Paths.get(uploadDir, studentDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String filename = "report_progress_" + progressId + "_" + System.currentTimeMillis() + extension;
        Path filePath = uploadPath.resolve(filename);

        // Save file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return relative path for storage in database
        return studentDir + "/" + filename;
    }
}

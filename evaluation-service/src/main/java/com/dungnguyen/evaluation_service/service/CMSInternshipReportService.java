package com.dungnguyen.evaluation_service.service;

import com.dungnguyen.evaluation_service.dto.*;
import com.dungnguyen.evaluation_service.entity.InternshipReport;
import com.dungnguyen.evaluation_service.exception.InternshipReportNotFoundException;
import com.dungnguyen.evaluation_service.repository.InternshipReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CMSInternshipReportService {

    private final InternshipReportRepository reportRepository;
    private final RestTemplate restTemplate;

    @Value("${file.upload.dir:uploads}")
    private String uploadDir;

    @Value("${services.registration.url:http://localhost:8003}")
    private String registrationServiceUrl;

    @Value("${services.user.url:http://localhost:8002}")
    private String userServiceUrl;

    /**
     * Get all internship reports with filtering
     */
    public List<CMSInternshipReportDTO> getAllReports(String periodId, String studentName, String companyName, Boolean submitted) {
        List<InternshipReport> allReports = reportRepository.findAll().stream()
                .filter(report -> report.getDeletedAt() == null)
                .collect(Collectors.toList());

        return allReports.stream()
                .map(this::mapToSummaryDTO)
                .filter(dto -> {
                    // Apply filters
                    if (periodId != null && !periodId.isEmpty() && !periodId.equals(dto.getPeriodId())) {
                        return false;
                    }
                    if (studentName != null && !studentName.isEmpty() &&
                            (dto.getStudentName() == null || !dto.getStudentName().toLowerCase().contains(studentName.toLowerCase()))) {
                        return false;
                    }
                    if (companyName != null && !companyName.isEmpty() &&
                            (dto.getCompanyName() == null || !dto.getCompanyName().toLowerCase().contains(companyName.toLowerCase()))) {
                        return false;
                    }
                    if (submitted != null) {
                        boolean isSubmitted = dto.getSubmissionDate() != null &&
                                dto.getTitle() != null && !dto.getTitle().trim().isEmpty();
                        if (submitted != isSubmitted) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get report by ID with full details
     */
    public CMSInternshipReportDetailDTO getReportById(Integer id) {
        InternshipReport report = reportRepository.findById(id)
                .filter(r -> r.getDeletedAt() == null)
                .orElseThrow(() -> new InternshipReportNotFoundException("Report not found with ID: " + id));

        return mapToDetailDTO(report);
    }

    /**
     * Create new internship report
     */
    @Transactional
    public CMSInternshipReportDetailDTO createReport(CMSInternshipReportCreateDTO createDTO, MultipartFile file) {
        // Validate required fields
        if (createDTO.getProgressId() == null) {
            throw new IllegalArgumentException("Progress ID is required");
        }
        if (createDTO.getTitle() == null || createDTO.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (createDTO.getContent() == null || createDTO.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content is required");
        }

        // Check if report already exists for this progress
        if (reportRepository.findByProgressIdAndDeletedAtIsNull(createDTO.getProgressId()).isPresent()) {
            throw new IllegalArgumentException("Report already exists for this progress");
        }

        // Create new report
        InternshipReport report = new InternshipReport();
        report.setProgressId(createDTO.getProgressId());
        report.setTitle(createDTO.getTitle().trim());
        report.setContent(createDTO.getContent().trim());
        report.setSubmissionDate(LocalDateTime.now());

        // Handle file upload if provided
        if (file != null && !file.isEmpty()) {
            try {
                String filePath = saveReportFile(file, createDTO.getProgressId());
                report.setFilePath(filePath);
            } catch (IOException e) {
                log.error("Error saving report file: {}", e.getMessage());
                throw new RuntimeException("Failed to save report file", e);
            }
        }

        InternshipReport savedReport = reportRepository.save(report);
        return mapToDetailDTO(savedReport);
    }

    /**
     * Update internship report
     */
    @Transactional
    public CMSInternshipReportDetailDTO updateReport(Integer id, CMSInternshipReportUpdateDTO updateDTO) {
        InternshipReport report = reportRepository.findById(id)
                .filter(r -> r.getDeletedAt() == null)
                .orElseThrow(() -> new InternshipReportNotFoundException("Report not found with ID: " + id));

        // Update fields
        if (updateDTO.getTitle() != null) {
            if (updateDTO.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty");
            }
            report.setTitle(updateDTO.getTitle().trim());
        }

        if (updateDTO.getContent() != null) {
            if (updateDTO.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("Content cannot be empty");
            }
            report.setContent(updateDTO.getContent().trim());
        }

        // Update submission date if content changed
        if (updateDTO.getTitle() != null || updateDTO.getContent() != null) {
            report.setSubmissionDate(LocalDateTime.now());
        }

        InternshipReport updatedReport = reportRepository.save(report);
        return mapToDetailDTO(updatedReport);
    }

    /**
     * Upload file for existing report
     */
    @Transactional
    public CMSInternshipReportDetailDTO uploadFile(Integer id, MultipartFile file) {
        InternshipReport report = reportRepository.findById(id)
                .filter(r -> r.getDeletedAt() == null)
                .orElseThrow(() -> new InternshipReportNotFoundException("Report not found with ID: " + id));

        try {
            String filePath = saveReportFile(file, report.getProgressId());
            report.setFilePath(filePath);

            InternshipReport updatedReport = reportRepository.save(report);
            return mapToDetailDTO(updatedReport);
        } catch (IOException e) {
            log.error("Error saving report file: {}", e.getMessage());
            throw new RuntimeException("Failed to save report file", e);
        }
    }

    /**
     * Delete report (soft delete)
     */
    @Transactional
    public void deleteReport(Integer id) {
        InternshipReport report = reportRepository.findById(id)
                .filter(r -> r.getDeletedAt() == null)
                .orElseThrow(() -> new InternshipReportNotFoundException("Report not found with ID: " + id));

        report.setDeletedAt(LocalDateTime.now());
        reportRepository.save(report);
    }

    /**
     * Helper method to save uploaded file
     */
    private String saveReportFile(MultipartFile file, Integer progressId) throws IOException {
        // Create directory structure
        String reportDir = "cms/reports/progress_" + progressId;
        Path uploadPath = Paths.get(uploadDir, reportDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String filename = "report_" + timestamp + extension;
        Path filePath = uploadPath.resolve(filename);

        // Save file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return relative path for storage in database
        return reportDir + "/" + filename;
    }

    /**
     * Helper method to map entity to summary DTO (for list view)
     */
    private CMSInternshipReportDTO mapToSummaryDTO(InternshipReport report) {
        CMSInternshipReportDTO dto = new CMSInternshipReportDTO();
        dto.setId(report.getId());
        dto.setProgressId(report.getProgressId());
        dto.setTitle(report.getTitle());
        dto.setFilePath(report.getFilePath());
        dto.setSubmissionDate(report.getSubmissionDate());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setUpdatedAt(report.getUpdatedAt());

        // Calculate if report is editable
        dto.setIsEditable(!(report.getSubmissionDate() != null &&
                report.getTitle() != null && !report.getTitle().trim().isEmpty() &&
                report.getContent() != null && !report.getContent().trim().isEmpty()));

        // Get progress details from registration service
        try {
            Map<String, Object> progressData = getProgressDetail(report.getProgressId());
            if (progressData != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> progress = (Map<String, Object>) progressData.get("progress");
                @SuppressWarnings("unchecked")
                Map<String, Object> student = (Map<String, Object>) progressData.get("student");
                @SuppressWarnings("unchecked")
                Map<String, Object> company = (Map<String, Object>) progressData.get("company");

                if (progress != null) {
                    dto.setPeriodId((String) progress.get("periodId"));
                    dto.setPositionTitle((String) progress.get("positionTitle"));
                    dto.setIsExternal((Boolean) progress.get("isExternal"));

                    if (Boolean.TRUE.equals(progress.get("isExternal"))) {
                        dto.setCompanyName((String) progress.get("companyName"));
                    }
                }

                if (student != null) {
                    dto.setStudentId((Integer) student.get("id"));
                    dto.setStudentCode((String) student.get("studentCode"));
                    dto.setStudentName((String) student.get("name"));
                }

                if (company != null && dto.getCompanyName() == null) {
                    dto.setCompanyName((String) company.get("name"));
                }
            }
        } catch (Exception e) {
            log.warn("Could not fetch progress details for report {}: {}", report.getId(), e.getMessage());
        }

        return dto;
    }

    /**
     * Helper method to map entity to detailed DTO with external data
     */
    private CMSInternshipReportDetailDTO mapToDetailDTO(InternshipReport report) {
        CMSInternshipReportDetailDTO dto = new CMSInternshipReportDetailDTO();
        dto.setId(report.getId());
        dto.setProgressId(report.getProgressId());
        dto.setTitle(report.getTitle());
        dto.setContent(report.getContent());
        dto.setFilePath(report.getFilePath());
        dto.setSubmissionDate(report.getSubmissionDate());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setUpdatedAt(report.getUpdatedAt());

        // Calculate if report is editable
        dto.setIsEditable(!(report.getSubmissionDate() != null &&
                report.getTitle() != null && !report.getTitle().trim().isEmpty() &&
                report.getContent() != null && !report.getContent().trim().isEmpty()));

        // Get progress details from registration service
        try {
            Map<String, Object> progressData = getProgressDetail(report.getProgressId());
            if (progressData != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> progress = (Map<String, Object>) progressData.get("progress");
                @SuppressWarnings("unchecked")
                Map<String, Object> student = (Map<String, Object>) progressData.get("student");
                @SuppressWarnings("unchecked")
                Map<String, Object> company = (Map<String, Object>) progressData.get("company");

                if (progress != null) {
                    dto.setPeriodId((String) progress.get("periodId"));
                    dto.setPositionTitle((String) progress.get("positionTitle"));
                    dto.setStartDate((String) progress.get("startDate"));
                    dto.setEndDate((String) progress.get("endDate"));
                    dto.setIsExternal((Boolean) progress.get("isExternal"));

                    if (Boolean.TRUE.equals(progress.get("isExternal"))) {
                        dto.setCompanyName((String) progress.get("companyName"));
                    }
                }

                if (student != null) {
                    dto.setStudentId((Integer) student.get("id"));
                    dto.setStudentCode((String) student.get("studentCode"));
                    dto.setStudentName((String) student.get("name"));
                    dto.setStudentEmail((String) student.get("email"));
                    dto.setStudentPhone((String) student.get("phone"));
                }

                if (company != null && dto.getCompanyName() == null) {
                    dto.setCompanyName((String) company.get("name"));
                }
            }
        } catch (Exception e) {
            log.warn("Could not fetch progress details for report {}: {}", report.getId(), e.getMessage());
        }

        return dto;
    }

    /**
     * Get progress detail from registration service
     */
    private Map<String, Object> getProgressDetail(Integer progressId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    registrationServiceUrl + "/cms/progress/" + progressId,
                    HttpMethod.GET,
                    entity,
                    Object.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
                return (Map<String, Object>) responseBody.get("data");
            }

            return null;
        } catch (Exception e) {
            log.error("Error fetching progress detail for progress {}: {}", progressId, e.getMessage());
            return null;
        }
    }
}
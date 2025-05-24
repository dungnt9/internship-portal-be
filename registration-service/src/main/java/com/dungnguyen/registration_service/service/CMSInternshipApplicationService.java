package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.CompanyDTO;
import com.dungnguyen.registration_service.dto.StudentDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipApplicationCreateDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipApplicationDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipApplicationDetailDTO;
import com.dungnguyen.registration_service.dto.cms.CMSInternshipApplicationUpdateDTO;
import com.dungnguyen.registration_service.entity.InternshipApplication;
import com.dungnguyen.registration_service.entity.InternshipApplicationDetail;
import com.dungnguyen.registration_service.entity.InternshipPeriod;
import com.dungnguyen.registration_service.entity.InternshipPosition;
import com.dungnguyen.registration_service.exception.InternshipApplicationNotFoundException;
import com.dungnguyen.registration_service.exception.InternshipPeriodNotFoundException;
import com.dungnguyen.registration_service.repository.InternshipApplicationRepository;
import com.dungnguyen.registration_service.repository.InternshipPeriodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CMSInternshipApplicationService {

    private final InternshipApplicationRepository applicationRepository;
    private final InternshipPeriodRepository periodRepository;
    private final UserServiceClient userServiceClient;
    private final FileUploadService fileUploadService;

    /**
     * Get all internship applications
     *
     * @return List of CMSInternshipApplicationDTO
     */
    public List<CMSInternshipApplicationDTO> getAllApplications() {
        List<InternshipApplication> applications = applicationRepository.findAll();
        return applications.stream()
                .filter(app -> app.getDeletedAt() == null)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get internship application by ID
     *
     * @param id Application ID
     * @return CMSInternshipApplicationDTO
     * @throws InternshipApplicationNotFoundException if application not found
     */
    public CMSInternshipApplicationDTO getApplicationById(Integer id) {
        InternshipApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new InternshipApplicationNotFoundException("Internship application not found with ID: " + id));

        if (application.getDeletedAt() != null) {
            throw new InternshipApplicationNotFoundException("Internship application not found with ID: " + id);
        }

        return convertToDTO(application);
    }

    /**
     * Create new internship application (without CV file)
     *
     * @param createDTO Application creation data
     * @return Created CMSInternshipApplicationDTO
     * @throws InternshipPeriodNotFoundException if period not found
     * @throws IllegalArgumentException if student already has application for this period
     */
    @Transactional
    public CMSInternshipApplicationDTO createApplication(CMSInternshipApplicationCreateDTO createDTO) {
        // Validate if period exists
        InternshipPeriod period = periodRepository.findById(createDTO.getPeriodId())
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + createDTO.getPeriodId()));

        // Check if student already has an application for this period
        if (applicationRepository.findByStudentIdAndPeriodId(createDTO.getStudentId(), createDTO.getPeriodId()).isPresent()) {
            throw new IllegalArgumentException("Student already has an application for this period");
        }

        // Create new application
        InternshipApplication application = new InternshipApplication();
        application.setStudentId(createDTO.getStudentId());
        application.setPeriod(period);
        application.setCvFilePath(createDTO.getCvFilePath());
        application.setDetails(new ArrayList<>());

        InternshipApplication savedApplication = applicationRepository.save(application);
        return convertToDTO(savedApplication);
    }

    /**
     * Create new internship application with CV file upload
     *
     * @param createDTO Application creation data
     * @param cvFile CV file to upload
     * @return Created CMSInternshipApplicationDTO
     * @throws InternshipPeriodNotFoundException if period not found
     * @throws IllegalArgumentException if student already has application for this period
     */
    @Transactional
    public CMSInternshipApplicationDTO createApplicationWithCV(CMSInternshipApplicationCreateDTO createDTO, MultipartFile cvFile) {
        // Validate if period exists
        InternshipPeriod period = periodRepository.findById(createDTO.getPeriodId())
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + createDTO.getPeriodId()));

        // Check if student already has an application for this period
        if (applicationRepository.findByStudentIdAndPeriodId(createDTO.getStudentId(), createDTO.getPeriodId()).isPresent()) {
            throw new IllegalArgumentException("Student already has an application for this period");
        }

        // Get student code for file path
        String studentCode = getStudentCode(createDTO.getStudentId());

        // Upload CV file
        String filePath = fileUploadService.uploadFile(cvFile, studentCode, "cv");

        // Create new application
        InternshipApplication application = new InternshipApplication();
        application.setStudentId(createDTO.getStudentId());
        application.setPeriod(period);
        application.setCvFilePath(filePath);
        application.setDetails(new ArrayList<>());

        InternshipApplication savedApplication = applicationRepository.save(application);
        return convertToDTO(savedApplication);
    }

    /**
     * Update internship application
     *
     * @param id Application ID
     * @param updateDTO Update data
     * @return Updated CMSInternshipApplicationDTO
     * @throws InternshipApplicationNotFoundException if application not found
     */
    @Transactional
    public CMSInternshipApplicationDTO updateApplication(Integer id, CMSInternshipApplicationUpdateDTO updateDTO) {
        InternshipApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new InternshipApplicationNotFoundException("Internship application not found with ID: " + id));

        if (application.getDeletedAt() != null) {
            throw new InternshipApplicationNotFoundException("Internship application not found with ID: " + id);
        }

        // Update fields if provided
        if (updateDTO.getCvFilePath() != null) {
            application.setCvFilePath(updateDTO.getCvFilePath());
        }

        InternshipApplication updatedApplication = applicationRepository.save(application);
        return convertToDTO(updatedApplication);
    }

    /**
     * Delete internship application (soft delete)
     *
     * @param id Application ID
     * @throws InternshipApplicationNotFoundException if application not found
     */
    @Transactional
    public void deleteApplication(Integer id) {
        InternshipApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new InternshipApplicationNotFoundException("Internship application not found with ID: " + id));

        if (application.getDeletedAt() != null) {
            throw new InternshipApplicationNotFoundException("Internship application not found with ID: " + id);
        }

        // Soft delete
        application.setDeletedAt(LocalDateTime.now());

        // Also soft delete all details
        application.getDetails().forEach(detail -> detail.setDeletedAt(LocalDateTime.now()));

        applicationRepository.save(application);
    }

    /**
     * Upload new CV file for existing application
     *
     * @param applicationId Application ID
     * @param cvFile New CV file
     * @return Updated CMSInternshipApplicationDTO
     * @throws InternshipApplicationNotFoundException if application not found
     */
    @Transactional
    public CMSInternshipApplicationDTO uploadCV(Integer applicationId, MultipartFile cvFile) {
        InternshipApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new InternshipApplicationNotFoundException("Internship application not found with ID: " + applicationId));

        if (application.getDeletedAt() != null) {
            throw new InternshipApplicationNotFoundException("Internship application not found with ID: " + applicationId);
        }

        // Get student code for file path
        String studentCode = getStudentCode(application.getStudentId());

        // Upload CV file
        String filePath = fileUploadService.uploadFile(cvFile, studentCode, "cv");
        application.setCvFilePath(filePath);

        InternshipApplication updatedApplication = applicationRepository.save(application);
        return convertToDTO(updatedApplication);
    }

    /**
     * Convert InternshipApplication entity to DTO
     *
     * @param application InternshipApplication entity
     * @return CMSInternshipApplicationDTO
     */
    private CMSInternshipApplicationDTO convertToDTO(InternshipApplication application) {
        CMSInternshipApplicationDTO dto = new CMSInternshipApplicationDTO();
        dto.setId(application.getId());
        dto.setStudentId(application.getStudentId());
        dto.setPeriodId(application.getPeriod().getId());
        dto.setCvFilePath(application.getCvFilePath());
        dto.setCreatedAt(application.getCreatedAt());
        dto.setUpdatedAt(application.getUpdatedAt());

        // Get student information
        try {
            StudentDTO student = userServiceClient.getStudentById(application.getStudentId(), null);
            if (student != null) {
                dto.setStudentCode(student.getStudentCode());
                dto.setStudentName(student.getName());
                dto.setStudentEmail(student.getEmail());
                dto.setStudentPhone(student.getPhone());
            }
        } catch (Exception e) {
            log.warn("Could not fetch student details for ID: {}", application.getStudentId());
        }

        // Convert details
        List<CMSInternshipApplicationDetailDTO> detailDTOs = application.getDetails().stream()
                .filter(detail -> detail.getDeletedAt() == null)
                .map(this::convertDetailToDTO)
                .collect(Collectors.toList());

        dto.setDetails(detailDTOs);
        return dto;
    }

    /**
     * Convert InternshipApplicationDetail entity to DTO
     *
     * @param detail InternshipApplicationDetail entity
     * @return CMSInternshipApplicationDetailDTO
     */
    private CMSInternshipApplicationDetailDTO convertDetailToDTO(InternshipApplicationDetail detail) {
        CMSInternshipApplicationDetailDTO dto = new CMSInternshipApplicationDetailDTO();
        dto.setId(detail.getId());
        dto.setApplicationId(detail.getApplication().getId());
        dto.setPositionId(detail.getPosition().getId());
        dto.setPositionTitle(detail.getPosition().getTitle());

        // Get company name from UserServiceClient
        try {
            CompanyDTO company = userServiceClient.getCompanyById(detail.getPosition().getCompanyId(), null);
            dto.setCompanyName(company != null ? company.getName() : "Unknown Company");
        } catch (Exception e) {
            log.warn("Could not fetch company details for ID: {}", detail.getPosition().getCompanyId());
            dto.setCompanyName("Unknown Company");
        }

        dto.setPreferenceOrder(detail.getPreferenceOrder());
        dto.setStatus(detail.getStatus().name());
        dto.setNote(detail.getNote());
        dto.setCreatedAt(detail.getCreatedAt());
        dto.setUpdatedAt(detail.getUpdatedAt());
        return dto;
    }

    /**
     * Get student code from student ID
     *
     * @param studentId Student ID
     * @return Student code or fallback string
     */
    private String getStudentCode(Integer studentId) {
        try {
            StudentDTO student = userServiceClient.getStudentById(studentId, null);
            return student != null ? student.getStudentCode() : "student_" + studentId;
        } catch (Exception e) {
            log.warn("Could not fetch student code for ID: {}", studentId);
            return "student_" + studentId;
        }
    }
}
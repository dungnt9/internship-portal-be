package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.client.AuthServiceClient;
import com.dungnguyen.registration_service.client.UserServiceClient;
import com.dungnguyen.registration_service.dto.ExternalInternshipCreateDTO;
import com.dungnguyen.registration_service.dto.ExternalInternshipDTO;
import com.dungnguyen.registration_service.entity.ExternalInternship;
import com.dungnguyen.registration_service.entity.InternshipPeriod;
import com.dungnguyen.registration_service.exception.DuplicateExternalInternshipException;
import com.dungnguyen.registration_service.exception.ExternalInternshipNotFoundException;
import com.dungnguyen.registration_service.exception.InternshipPeriodNotFoundException;
import com.dungnguyen.registration_service.exception.UnauthorizedAccessException;
import com.dungnguyen.registration_service.repository.ExternalInternshipRepository;
import com.dungnguyen.registration_service.repository.InternshipPeriodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalInternshipService {

    private final ExternalInternshipRepository externalInternshipRepository;
    private final InternshipPeriodRepository periodRepository;
    private final AuthServiceClient authServiceClient;
    private final FileUploadService fileUploadService;

    /**
     * Get all external internships for current student in current period
     *
     * @param token Authorization token
     * @return List of ExternalInternshipDTO
     */
    public List<ExternalInternshipDTO> getMyExternalInternships(String token) {
        // Get current student ID from token
        Integer studentId = authServiceClient.getUserStudentId(token);
        if (studentId == null) {
            throw new RuntimeException("Could not determine student from authorization token");
        }

        List<ExternalInternship> externalInternships = externalInternshipRepository
                .findByStudentId(studentId);

        // Convert to DTOs
        return externalInternships.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create new external internship application
     *
     * @param createDTO External internship creation data
     * @param confirmationFile CV/Confirmation letter file
     * @param token Authorization token
     * @return Created ExternalInternshipDTO
     */
    @Transactional
    public ExternalInternshipDTO createExternalInternship(
            ExternalInternshipCreateDTO createDTO,
            MultipartFile confirmationFile,
            String token) {

        // Get current student ID from token
        Integer studentId = authServiceClient.getUserStudentId(token);
        if (studentId == null) {
            throw new RuntimeException("Could not determine student from authorization token");
        }

        if (externalInternshipRepository.existsByStudentIdAndPeriodId(studentId, createDTO.getPeriodId())) {
            throw new DuplicateExternalInternshipException("You have already registered for external internship in this period");
        }

        String studentCode = getStudentCode(studentId, token);

        // Get period
        InternshipPeriod period = periodRepository.findById(createDTO.getPeriodId())
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + createDTO.getPeriodId()));

        // Upload file
        String filePath = fileUploadService.uploadConfirmationFile(confirmationFile, studentCode, createDTO.getPeriodId());

        // Create new external internship
        ExternalInternship externalInternship = new ExternalInternship();
        externalInternship.setStudentId(studentId);
        externalInternship.setPeriod(period);
        externalInternship.setConfirmationFilePath(filePath);
        externalInternship.setStatus(ExternalInternship.Status.PENDING);

        // Save to database
        ExternalInternship savedExternalInternship = externalInternshipRepository.save(externalInternship);

        // Return as DTO
        return convertToDTO(savedExternalInternship);
    }

    /**
     * Convert ExternalInternship to DTO
     *
     * @param externalInternship ExternalInternship entity
     * @return ExternalInternshipDTO
     */
    private ExternalInternshipDTO convertToDTO(ExternalInternship externalInternship) {
        ExternalInternshipDTO dto = new ExternalInternshipDTO();
        dto.setId(externalInternship.getId());
        dto.setStudentId(externalInternship.getStudentId());
        dto.setPeriodId(externalInternship.getPeriod().getId());
        dto.setConfirmationFilePath(externalInternship.getConfirmationFilePath());
        dto.setStatus(externalInternship.getStatus().name());
        dto.setCreatedAt(externalInternship.getCreatedAt());
        dto.setUpdatedAt(externalInternship.getUpdatedAt());
        return dto;
    }

    /**
     * Get student code from student ID
     *
     * @param studentId Student ID
     * @param token Authorization token
     * @return Student code
     */
    private String getStudentCode(Integer studentId, String token) {
        // This method would call UserServiceClient to get student details
        // For now, returning a placeholder - you'll need to implement this
        // based on your UserServiceClient capabilities
        return "student_" + studentId;
    }

    @Transactional
    public ExternalInternshipDTO createExternalInternshipWithFile(
            ExternalInternshipCreateDTO createDTO,
            MultipartFile confirmationFile,
            String studentCode,
            String token) {

        // Get current student ID from token
        Integer studentId = authServiceClient.getUserStudentId(token);
        if (studentId == null) {
            throw new RuntimeException("Could not determine student from authorization token");
        }

        if (externalInternshipRepository.existsByStudentIdAndPeriodId(studentId, createDTO.getPeriodId())) {
            throw new DuplicateExternalInternshipException("You have already registered for external internship in this period");
        }

        // Get period
        InternshipPeriod period = periodRepository.findById(createDTO.getPeriodId())
                .orElseThrow(() -> new InternshipPeriodNotFoundException("Internship period not found with ID: " + createDTO.getPeriodId()));

        // Upload file
        String filePath = fileUploadService.uploadConfirmationFile(confirmationFile, studentCode, createDTO.getPeriodId());

        // Create new external internship
        ExternalInternship externalInternship = new ExternalInternship();
        externalInternship.setStudentId(studentId);
        externalInternship.setPeriod(period);
        externalInternship.setConfirmationFilePath(filePath);
        externalInternship.setStatus(ExternalInternship.Status.PENDING);

        // Save to database
        ExternalInternship savedExternalInternship = externalInternshipRepository.save(externalInternship);

        // Return as DTO
        return convertToDTO(savedExternalInternship);
    }
}
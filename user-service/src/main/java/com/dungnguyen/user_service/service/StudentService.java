package com.dungnguyen.user_service.service;

import com.dungnguyen.user_service.client.AuthServiceClient;
import com.dungnguyen.user_service.dto.StudentDTO;
import com.dungnguyen.user_service.dto.StudentUpdateDTO;
import com.dungnguyen.user_service.dto.auth.UserResponseDTO;
import com.dungnguyen.user_service.entity.Student;
import com.dungnguyen.user_service.exception.StudentNotFoundException;
import com.dungnguyen.user_service.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final AuthServiceClient authServiceClient;
    private final FileStorageService fileStorageService;

    public StudentDTO getStudentByAuthUserId(Integer authUserId, String token) {
        Student student = studentRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with auth user ID: " + authUserId));

        StudentDTO studentDTO = new StudentDTO(student);

        // Fetch user details from Auth Service
        UserResponseDTO userResponse = authServiceClient.getUserById(authUserId, token);
        if (userResponse != null) {
            studentDTO.setEmail(userResponse.getEmail());
            studentDTO.setPhone(userResponse.getPhone());
        }

        return studentDTO;
    }

    public Integer getCurrentUserAuthId(String token) {
        return authServiceClient.getUserIdFromToken(token);
    }

    @Transactional
    public StudentDTO updateStudent(Integer authUserId, StudentUpdateDTO updateDTO) {
        Student student = studentRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with auth user ID: " + authUserId));

        // Update student information
        if (updateDTO.getName() != null) {
            student.setName(updateDTO.getName());
        }
        if (updateDTO.getClassName() != null) {
            student.setClassName(updateDTO.getClassName());
        }
        if (updateDTO.getMajor() != null) {
            student.setMajor(updateDTO.getMajor());
        }
        if (updateDTO.getGender() != null) {
            student.setGender(updateDTO.getGender());
        }
        if (updateDTO.getBirthday() != null) {
            student.setBirthday(updateDTO.getBirthday());
        }
        if (updateDTO.getAddress() != null) {
            student.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getCpa() != null) {
            student.setCpa(updateDTO.getCpa());
        }
        if (updateDTO.getEnglishLevel() != null) {
            student.setEnglishLevel(updateDTO.getEnglishLevel());
        }
        if (updateDTO.getSkills() != null) {
            student.setSkills(updateDTO.getSkills());
        }

        Student updatedStudent = studentRepository.save(student);
        return new StudentDTO(updatedStudent);
    }

    public StudentDTO getStudentById(Integer id, String token) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));

        StudentDTO studentDTO = new StudentDTO(student);

        // Fetch user details from Auth Service
        UserResponseDTO userResponse = authServiceClient.getUserById(student.getAuthUserId(), token);
        if (userResponse != null) {
            studentDTO.setEmail(userResponse.getEmail());
            studentDTO.setPhone(userResponse.getPhone());
        }

        return studentDTO;
    }

    @Transactional
    public void updateAvatarPath(Integer authUserId, String imagePath) {
        Student student = studentRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with auth user ID: " + authUserId));

        // Delete old avatar if exists
        if (student.getImagePath() != null && !student.getImagePath().isEmpty()) {
            fileStorageService.deleteFile(student.getImagePath());
        }

        student.setImagePath(imagePath);
        studentRepository.save(student);
    }
}
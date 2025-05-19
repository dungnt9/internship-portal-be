package com.dungnguyen.user_service.service;

import com.dungnguyen.user_service.client.CMSAuthServiceClient;
import com.dungnguyen.user_service.dto.auth.CreateUserRequestDTO;
import com.dungnguyen.user_service.dto.auth.UserResponseDTO;
import com.dungnguyen.user_service.dto.cms.CMSStudentCreateDTO;
import com.dungnguyen.user_service.dto.cms.CMSStudentDTO;
import com.dungnguyen.user_service.dto.cms.CMSStudentUpdateDTO;
import com.dungnguyen.user_service.entity.Student;
import com.dungnguyen.user_service.exception.StudentNotFoundException;
import com.dungnguyen.user_service.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CMSStudentService {

    private final StudentRepository studentRepository;
    private final CMSAuthServiceClient authServiceClient;

    public List<CMSStudentDTO> getAllStudents() {
        // Get all students from the repository
        List<Student> students = studentRepository.findAll();

        // Get all student users from auth service
        List<UserResponseDTO> authUsers = authServiceClient.getAllUsers();

        // Filter to keep only student users
        if (authUsers != null) {
            authUsers = authUsers.stream()
                    .filter(user -> "ROLE_STUDENT".equals(user.getRole()))
                    .collect(Collectors.toList());

            // Create a map of auth users for easy lookup
            Map<Integer, UserResponseDTO> authUserMap = authUsers.stream()
                    .collect(Collectors.toMap(UserResponseDTO::getId, Function.identity()));

            // Combine the data from both sources
            List<CMSStudentDTO> result = new ArrayList<>();

            for (Student student : students) {
                CMSStudentDTO dto = new CMSStudentDTO(student);
                UserResponseDTO authUser = authUserMap.get(student.getAuthUserId());

                if (authUser != null) {
                    dto.setEmail(authUser.getEmail());
                    dto.setPhone(authUser.getPhone());
                    dto.setIsActive(authUser.getIsActive());

                    result.add(dto);

                    // Remove this auth user from the map as it's been processed
                    authUserMap.remove(student.getAuthUserId());
                }
            }

            // Add any remaining student auth users that don't have corresponding student records
            for (UserResponseDTO remainingAuthUser : authUserMap.values()) {
                if ("ROLE_STUDENT".equals(remainingAuthUser.getRole())) {
                    CMSStudentDTO dto = new CMSStudentDTO();
                    dto.setAuthUserId(remainingAuthUser.getId());
                    dto.setEmail(remainingAuthUser.getEmail());
                    dto.setPhone(remainingAuthUser.getPhone());
                    dto.setIsActive(remainingAuthUser.getIsActive());

                    result.add(dto);
                }
            }

            return result;
        }

        // If we couldn't get auth users, just return student data without auth info
        return students.stream().map(CMSStudentDTO::new).collect(Collectors.toList());
    }

    public CMSStudentDTO getStudentById(Integer id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));

        CMSStudentDTO studentDTO = new CMSStudentDTO(student);

        // Fetch user details from Auth Service
        UserResponseDTO userResponse = authServiceClient.getUserById(student.getAuthUserId());
        if (userResponse != null) {
            studentDTO.setEmail(userResponse.getEmail());
            studentDTO.setPhone(userResponse.getPhone());
            studentDTO.setIsActive(userResponse.getIsActive());
        }

        return studentDTO;
    }

    @Transactional
    public CMSStudentDTO createStudent(CMSStudentCreateDTO createDTO) {
        // Check if student code already exists
        studentRepository.findAll().stream()
                .filter(s -> s.getStudentCode().equals(createDTO.getStudentCode()))
                .findFirst()
                .ifPresent(s -> {
                    throw new IllegalArgumentException("Student code already exists: " + createDTO.getStudentCode());
                });

        // First, create the user in auth service
        CreateUserRequestDTO authUserDTO = new CreateUserRequestDTO();
        authUserDTO.setEmail(createDTO.getEmail());
        authUserDTO.setPhone(createDTO.getPhone());
        authUserDTO.setPassword(createDTO.getPassword());
        authUserDTO.setRoleName("ROLE_STUDENT");
        authUserDTO.setIsActive(true);

        UserResponseDTO createdAuthUser = authServiceClient.createUser(authUserDTO);

        if (createdAuthUser == null) {
            throw new RuntimeException("Failed to create user in Auth Service");
        }

        // Then create the student in user service
        Student student = new Student();
        student.setAuthUserId(createdAuthUser.getId());
        student.setStudentCode(createDTO.getStudentCode());
        student.setName(createDTO.getName());
        student.setClassName(createDTO.getClassName());
        student.setMajor(createDTO.getMajor());
        student.setGender(createDTO.getGender());
        student.setBirthday(createDTO.getBirthday());
        student.setAddress(createDTO.getAddress());
        student.setCpa(createDTO.getCpa());
        student.setEnglishLevel(createDTO.getEnglishLevel());
        student.setSkills(createDTO.getSkills());

        Student savedStudent = studentRepository.save(student);

        // Return combined data
        CMSStudentDTO result = new CMSStudentDTO(savedStudent);
        result.setEmail(createdAuthUser.getEmail());
        result.setPhone(createdAuthUser.getPhone());
        result.setIsActive(createdAuthUser.getIsActive());

        return result;
    }

    @Transactional
    public CMSStudentDTO updateStudent(Integer id, CMSStudentUpdateDTO updateDTO) {
        // Find the student
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));

        // Check student code uniqueness if changed
        if (updateDTO.getStudentCode() != null && !updateDTO.getStudentCode().equals(student.getStudentCode())) {
            studentRepository.findAll().stream()
                    .filter(s -> s.getStudentCode().equals(updateDTO.getStudentCode()))
                    .findFirst()
                    .ifPresent(s -> {
                        throw new IllegalArgumentException("Student code already exists: " + updateDTO.getStudentCode());
                    });
        }

        // Update auth user information if available
        if (updateDTO.getEmail() != null || updateDTO.getPhone() != null ||
                updateDTO.getPassword() != null || updateDTO.getIsActive() != null) {

            CreateUserRequestDTO authUserDTO = new CreateUserRequestDTO();
            authUserDTO.setEmail(updateDTO.getEmail());
            authUserDTO.setPhone(updateDTO.getPhone());
            authUserDTO.setPassword(updateDTO.getPassword());
            authUserDTO.setIsActive(updateDTO.getIsActive());

            UserResponseDTO updatedAuthUser = authServiceClient.updateUser(student.getAuthUserId(), authUserDTO);

            if (updatedAuthUser == null) {
                throw new RuntimeException("Failed to update user in Auth Service");
            }
        }

        // Update student information
        if (updateDTO.getStudentCode() != null) {
            student.setStudentCode(updateDTO.getStudentCode());
        }
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

        // Return combined data
        CMSStudentDTO result = new CMSStudentDTO(updatedStudent);

        // Fetch updated user details from Auth Service
        UserResponseDTO userResponse = authServiceClient.getUserById(student.getAuthUserId());
        if (userResponse != null) {
            result.setEmail(userResponse.getEmail());
            result.setPhone(userResponse.getPhone());
            result.setIsActive(userResponse.getIsActive());
        }

        return result;
    }
}
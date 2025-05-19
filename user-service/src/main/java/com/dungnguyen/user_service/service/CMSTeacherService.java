package com.dungnguyen.user_service.service;

import com.dungnguyen.user_service.client.CMSAuthServiceClient;
import com.dungnguyen.user_service.dto.auth.CreateUserRequestDTO;
import com.dungnguyen.user_service.dto.auth.UserResponseDTO;
import com.dungnguyen.user_service.dto.cms.CMSTeacherCreateDTO;
import com.dungnguyen.user_service.dto.cms.CMSTeacherDTO;
import com.dungnguyen.user_service.dto.cms.CMSTeacherUpdateDTO;
import com.dungnguyen.user_service.entity.Teacher;
import com.dungnguyen.user_service.exception.TeacherNotFoundException;
import com.dungnguyen.user_service.repository.TeacherRepository;
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
public class CMSTeacherService {

    private final TeacherRepository teacherRepository;
    private final CMSAuthServiceClient authServiceClient;

    public List<CMSTeacherDTO> getAllTeachers() {
        // Get all teachers from the repository
        List<Teacher> teachers = teacherRepository.findAll();

        // Get all teacher users from auth service
        List<UserResponseDTO> authUsers = authServiceClient.getAllUsers();

        // Filter to keep only teacher users
        if (authUsers != null) {
            authUsers = authUsers.stream()
                    .filter(user -> "ROLE_TEACHER".equals(user.getRole()))
                    .collect(Collectors.toList());

            // Create a map of auth users for easy lookup
            Map<Integer, UserResponseDTO> authUserMap = authUsers.stream()
                    .collect(Collectors.toMap(UserResponseDTO::getId, Function.identity()));

            // Combine the data from both sources
            List<CMSTeacherDTO> result = new ArrayList<>();

            for (Teacher teacher : teachers) {
                CMSTeacherDTO dto = new CMSTeacherDTO(teacher);
                UserResponseDTO authUser = authUserMap.get(teacher.getAuthUserId());

                if (authUser != null) {
                    dto.setEmail(authUser.getEmail());
                    dto.setPhone(authUser.getPhone());
                    dto.setIsActive(authUser.getIsActive());

                    result.add(dto);

                    // Remove this auth user from the map as it's been processed
                    authUserMap.remove(teacher.getAuthUserId());
                }
            }

            // Add any remaining teacher auth users that don't have corresponding teacher records
            for (UserResponseDTO remainingAuthUser : authUserMap.values()) {
                if ("ROLE_TEACHER".equals(remainingAuthUser.getRole())) {
                    CMSTeacherDTO dto = new CMSTeacherDTO();
                    dto.setAuthUserId(remainingAuthUser.getId());
                    dto.setEmail(remainingAuthUser.getEmail());
                    dto.setPhone(remainingAuthUser.getPhone());
                    dto.setIsActive(remainingAuthUser.getIsActive());

                    result.add(dto);
                }
            }

            return result;
        }

        // If we couldn't get auth users, just return teacher data without auth info
        return teachers.stream().map(CMSTeacherDTO::new).collect(Collectors.toList());
    }

    public CMSTeacherDTO getTeacherById(Integer id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with ID: " + id));

        CMSTeacherDTO teacherDTO = new CMSTeacherDTO(teacher);

        // Fetch user details from Auth Service
        UserResponseDTO userResponse = authServiceClient.getUserById(teacher.getAuthUserId());
        if (userResponse != null) {
            teacherDTO.setEmail(userResponse.getEmail());
            teacherDTO.setPhone(userResponse.getPhone());
            teacherDTO.setIsActive(userResponse.getIsActive());
        }

        return teacherDTO;
    }

    @Transactional
    public CMSTeacherDTO createTeacher(CMSTeacherCreateDTO createDTO) {
        // First, create the user in auth service
        CreateUserRequestDTO authUserDTO = new CreateUserRequestDTO();
        authUserDTO.setEmail(createDTO.getEmail());
        authUserDTO.setPhone(createDTO.getPhone());
        authUserDTO.setPassword(createDTO.getPassword());
        authUserDTO.setRoleName("ROLE_TEACHER");
        authUserDTO.setIsActive(true);

        UserResponseDTO createdAuthUser = authServiceClient.createUser(authUserDTO);

        if (createdAuthUser == null) {
            throw new RuntimeException("Failed to create user in Auth Service");
        }

        // Then create the teacher in user service
        Teacher teacher = new Teacher();
        teacher.setAuthUserId(createdAuthUser.getId());
        teacher.setName(createDTO.getName());
        teacher.setDepartment(createDTO.getDepartment());
        teacher.setPosition(createDTO.getPosition());

        Teacher savedTeacher = teacherRepository.save(teacher);

        // Return combined data
        CMSTeacherDTO result = new CMSTeacherDTO(savedTeacher);
        result.setEmail(createdAuthUser.getEmail());
        result.setPhone(createdAuthUser.getPhone());
        result.setIsActive(createdAuthUser.getIsActive());

        return result;
    }

    @Transactional
    public CMSTeacherDTO updateTeacher(Integer id, CMSTeacherUpdateDTO updateDTO) {
        // Find the teacher
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with ID: " + id));

        // Update auth user information if available
        if (updateDTO.getEmail() != null || updateDTO.getPhone() != null ||
                updateDTO.getPassword() != null || updateDTO.getIsActive() != null) {

            CreateUserRequestDTO authUserDTO = new CreateUserRequestDTO();
            authUserDTO.setEmail(updateDTO.getEmail());
            authUserDTO.setPhone(updateDTO.getPhone());
            authUserDTO.setPassword(updateDTO.getPassword());
            authUserDTO.setIsActive(updateDTO.getIsActive());

            UserResponseDTO updatedAuthUser = authServiceClient.updateUser(teacher.getAuthUserId(), authUserDTO);

            if (updatedAuthUser == null) {
                throw new RuntimeException("Failed to update user in Auth Service");
            }
        }

        // Update teacher information
        if (updateDTO.getName() != null) {
            teacher.setName(updateDTO.getName());
        }
        if (updateDTO.getDepartment() != null) {
            teacher.setDepartment(updateDTO.getDepartment());
        }
        if (updateDTO.getPosition() != null) {
            teacher.setPosition(updateDTO.getPosition());
        }

        Teacher updatedTeacher = teacherRepository.save(teacher);

        // Return combined data
        CMSTeacherDTO result = new CMSTeacherDTO(updatedTeacher);

        // Fetch updated user details from Auth Service
        UserResponseDTO userResponse = authServiceClient.getUserById(teacher.getAuthUserId());
        if (userResponse != null) {
            result.setEmail(userResponse.getEmail());
            result.setPhone(userResponse.getPhone());
            result.setIsActive(userResponse.getIsActive());
        }

        return result;
    }
}
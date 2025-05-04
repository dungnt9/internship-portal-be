package com.dungnguyen.user_service.service;

import com.dungnguyen.user_service.client.AuthServiceClient;
import com.dungnguyen.user_service.dto.TeacherDTO;
import com.dungnguyen.user_service.dto.TeacherUpdateDTO;
import com.dungnguyen.user_service.dto.auth.UserResponseDTO;
import com.dungnguyen.user_service.entity.Teacher;
import com.dungnguyen.user_service.exception.TeacherNotFoundException;
import com.dungnguyen.user_service.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final AuthServiceClient authServiceClient;

    public TeacherDTO getTeacherByAuthUserId(Integer authUserId, String token) {
        Teacher teacher = teacherRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with auth user ID: " + authUserId));

        TeacherDTO teacherDTO = new TeacherDTO(teacher);

        // Fetch user details from Auth Service
        UserResponseDTO userResponse = authServiceClient.getUserById(authUserId, token);
        if (userResponse != null) {
            teacherDTO.setEmail(userResponse.getEmail());
            teacherDTO.setPhone(userResponse.getPhone());
        }

        return teacherDTO;
    }

    public Integer getCurrentUserAuthId(String token) {
        return authServiceClient.getUserIdFromToken(token);
    }

    @Transactional
    public TeacherDTO updateTeacher(Integer authUserId, TeacherUpdateDTO updateDTO) {
        Teacher teacher = teacherRepository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher not found with auth user ID: " + authUserId));

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
        return new TeacherDTO(updatedTeacher);
    }
}
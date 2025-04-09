package com.dungnguyen.user_service.service;

import com.dungnguyen.user_service.entity.Student;
import com.dungnguyen.user_service.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
    }

    public Student getStudentByCode(String studentCode) {
        return studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with code: " + studentCode));
    }

    public Student getStudentByUserRefId(Integer userRefId) {
        return studentRepository.findByUserRefId(userRefId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with user reference id: " + userRefId));
    }

    public Student createStudent(Student student) {
        if (studentRepository.existsByStudentCode(student.getStudentCode())) {
            throw new IllegalArgumentException("Student code already exists: " + student.getStudentCode());
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Integer id, Student studentDetails) {
        Student student = getStudentById(id);

        // Only update the non-null fields
        if (studentDetails.getFullName() != null) {
            student.setFullName(studentDetails.getFullName());
        }
        if (studentDetails.getClassName() != null) {
            student.setClassName(studentDetails.getClassName());
        }
        if (studentDetails.getMajor() != null) {
            student.setMajor(studentDetails.getMajor());
        }
        if (studentDetails.getGender() != null) {
            student.setGender(studentDetails.getGender());
        }
        if (studentDetails.getBirthday() != null) {
            student.setBirthday(studentDetails.getBirthday());
        }
        if (studentDetails.getAddress() != null) {
            student.setAddress(studentDetails.getAddress());
        }
        if (studentDetails.getCpa() != null) {
            student.setCpa(studentDetails.getCpa());
        }
        if (studentDetails.getEnglishLevel() != null) {
            student.setEnglishLevel(studentDetails.getEnglishLevel());
        }
        if (studentDetails.getSkills() != null) {
            student.setSkills(studentDetails.getSkills());
        }

        return studentRepository.save(student);
    }

    public void deleteStudent(Integer id) {
        Student student = getStudentById(id);
        studentRepository.delete(student);
    }
}
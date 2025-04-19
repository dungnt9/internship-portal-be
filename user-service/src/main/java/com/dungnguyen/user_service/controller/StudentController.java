package com.dungnguyen.user_service.controller;

import com.dungnguyen.user_service.entity.Student;
import com.dungnguyen.user_service.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/code/{studentCode}")
    public ResponseEntity<Student> getStudentByCode(@PathVariable String studentCode) {
        return ResponseEntity.ok(studentService.getStudentByCode(studentCode));
    }

    @GetMapping("/user/{userRefId}")
    public ResponseEntity<Student> getStudentByUserRefId(@PathVariable Integer userRefId) {
        return ResponseEntity.ok(studentService.getStudentByUserRefId(userRefId));
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return new ResponseEntity<>(studentService.createStudent(student), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        return ResponseEntity.ok(studentService.updateStudent(id, student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
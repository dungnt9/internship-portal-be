package com.dungnguyen.user_service.repository;

import com.dungnguyen.user_service.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByAuthUserId(Integer authUserId);
}
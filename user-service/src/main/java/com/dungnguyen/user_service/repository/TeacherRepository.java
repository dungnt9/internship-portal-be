package com.dungnguyen.user_service.repository;

import com.dungnguyen.user_service.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Optional<Teacher> findByAuthUserId(Integer authUserId);
}
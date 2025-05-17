package com.dungnguyen.registration_service.repository;

import com.dungnguyen.registration_service.entity.ExternalInternship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExternalInternshipRepository extends JpaRepository<ExternalInternship, Integer> {

    @Query("SELECT e FROM ExternalInternship e WHERE e.studentId = :studentId AND e.deletedAt IS NULL")
    List<ExternalInternship> findByStudentId(@Param("studentId") Integer studentId);
}
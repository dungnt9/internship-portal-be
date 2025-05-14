package com.dungnguyen.registration_service.repository;

import com.dungnguyen.registration_service.entity.ExternalInternship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExternalInternshipRepository extends JpaRepository<ExternalInternship, Integer> {

    // Find all external internships by student ID where deletedAt is null
    @Query("SELECT e FROM ExternalInternship e WHERE e.studentId = :studentId AND e.deletedAt IS NULL ORDER BY e.createdAt DESC")
    List<ExternalInternship> findByStudentId(@Param("studentId") Integer studentId);

    // Find external internship by ID and student ID
    @Query("SELECT e FROM ExternalInternship e WHERE e.id = :id AND e.studentId = :studentId AND e.deletedAt IS NULL")
    Optional<ExternalInternship> findByIdAndStudentId(@Param("id") Integer id, @Param("studentId") Integer studentId);

    // Find external internship by period ID and student ID
    @Query("SELECT e FROM ExternalInternship e WHERE e.period.id = :periodId AND e.studentId = :studentId AND e.deletedAt IS NULL")
    Optional<ExternalInternship> findByPeriodIdAndStudentId(@Param("periodId") String periodId, @Param("studentId") Integer studentId);

    // Check if student has external internship for a specific period
    @Query("SELECT COUNT(e) > 0 FROM ExternalInternship e WHERE e.period.id = :periodId AND e.studentId = :studentId AND e.deletedAt IS NULL")
    boolean existsByPeriodIdAndStudentId(@Param("periodId") String periodId, @Param("studentId") Integer studentId);
}
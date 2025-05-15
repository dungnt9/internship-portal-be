package com.dungnguyen.registration_service.repository;

import com.dungnguyen.registration_service.entity.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Integer> {

    @Query("SELECT a FROM InternshipApplication a WHERE a.studentId = :studentId AND a.period.id = :periodId AND a.deletedAt IS NULL")
    Optional<InternshipApplication> findByStudentIdAndPeriodId(@Param("studentId") Integer studentId, @Param("periodId") String periodId);

    @Query("SELECT a FROM InternshipApplication a WHERE a.studentId = :studentId AND a.deletedAt IS NULL ORDER BY a.createdAt DESC")
    List<InternshipApplication> findByStudentId(@Param("studentId") Integer studentId);
}
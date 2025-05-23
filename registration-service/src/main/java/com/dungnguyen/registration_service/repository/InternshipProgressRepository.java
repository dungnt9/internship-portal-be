package com.dungnguyen.registration_service.repository;

import com.dungnguyen.registration_service.entity.InternshipProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipProgressRepository extends JpaRepository<InternshipProgress, Integer> {

    // Find all progress records by company ID
    @Query("SELECT p FROM InternshipProgress p WHERE p.position.companyId = :companyId")
    List<InternshipProgress> findByCompanyId(@Param("companyId") Integer companyId);

    // Find all progress records by company ID and period ID
    @Query("SELECT p FROM InternshipProgress p WHERE p.position.companyId = :companyId AND p.period.id = :periodId")
    List<InternshipProgress> findByCompanyIdAndPeriodId(@Param("companyId") Integer companyId, @Param("periodId") String periodId);

    // Find progress record by student ID for a company
    @Query("SELECT p FROM InternshipProgress p WHERE p.studentId = :studentId AND p.position.companyId = :companyId")
    Optional<InternshipProgress> findByStudentIdAndCompanyId(@Param("studentId") Integer studentId, @Param("companyId") Integer companyId);

    // Find all progress records assigned to a teacher
    @Query("SELECT p FROM InternshipProgress p WHERE p.teacherId = :teacherId")
    List<InternshipProgress> findByTeacherId(@Param("teacherId") Integer teacherId);

    // Find all progress records by teacher ID and period ID
    @Query("SELECT p FROM InternshipProgress p WHERE p.teacherId = :teacherId AND p.period.id = :periodId")
    List<InternshipProgress> findByTeacherIdAndPeriodId(@Param("teacherId") Integer teacherId, @Param("periodId") String periodId);

    // Find progress record by ID and teacher ID
    @Query("SELECT p FROM InternshipProgress p WHERE p.id = :id AND p.teacherId = :teacherId")
    Optional<InternshipProgress> findByIdAndTeacherId(@Param("id") Integer id, @Param("teacherId") Integer teacherId);

    @Query("SELECT p FROM InternshipProgress p WHERE p.studentId = :studentId AND p.period.id = :periodId")
    Optional<InternshipProgress> findByStudentIdAndPeriodId(@Param("studentId") Integer studentId, @Param("periodId") String periodId);
}
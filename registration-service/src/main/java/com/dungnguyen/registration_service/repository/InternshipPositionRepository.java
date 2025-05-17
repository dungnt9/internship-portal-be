package com.dungnguyen.registration_service.repository;

import com.dungnguyen.registration_service.entity.InternshipPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipPositionRepository extends JpaRepository<InternshipPosition, Integer> {

    // Find all positions where deletedAt is null (not deleted)
    @Query("SELECT p FROM InternshipPosition p WHERE p.deletedAt IS NULL ORDER BY p.createdAt DESC")
    List<InternshipPosition> findAllActivePositions();

    // Find all positions by company ID where deletedAt is null and period status is not 'END'
    @Query("SELECT p FROM InternshipPosition p WHERE p.companyId = :companyId AND p.period.status != 'END' AND p.deletedAt IS NULL ORDER BY p.createdAt DESC")
    List<InternshipPosition> findActivePositionsByCompanyId(@Param("companyId") Integer companyId);

    // Find all positions by company ID with status OPEN, for the ACTIVE period
    @Query("SELECT p FROM InternshipPosition p WHERE p.companyId = :companyId AND p.status = 'OPEN' AND p.period.status = 'UPCOMING' AND p.deletedAt IS NULL ORDER BY p.createdAt DESC")
    List<InternshipPosition> findOpenPositionsByCompanyIdForUpcomingPeriod(@Param("companyId") Integer companyId);

    // Find all positions by company ID for a specific period
    @Query("SELECT p FROM InternshipPosition p WHERE p.companyId = :companyId AND p.period.id = :periodId AND p.deletedAt IS NULL ORDER BY p.createdAt DESC")
    List<InternshipPosition> findByCompanyIdAndPeriodId(@Param("companyId") Integer companyId, @Param("periodId") String periodId);

    // Find a position by ID and company ID
    @Query("SELECT p FROM InternshipPosition p WHERE p.id = :id AND p.companyId = :companyId AND p.deletedAt IS NULL")
    Optional<InternshipPosition> findByIdAndCompanyId(@Param("id") Integer id, @Param("companyId") Integer companyId);

    @Query("SELECT p FROM InternshipPosition p WHERE p.companyId = :companyId AND p.deletedAt IS NULL")
    List<InternshipPosition> findByCompanyId(@Param("companyId") Integer companyId);
}
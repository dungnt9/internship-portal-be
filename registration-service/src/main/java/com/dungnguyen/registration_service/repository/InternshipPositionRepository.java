package com.dungnguyen.registration_service.repository;

import com.dungnguyen.registration_service.entity.InternshipPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternshipPositionRepository extends JpaRepository<InternshipPosition, Integer> {

    // Find all positions where deletedAt is null (not deleted)
    @Query("SELECT p FROM InternshipPosition p WHERE p.deletedAt IS NULL ORDER BY p.createdAt DESC")
    List<InternshipPosition> findAllActivePositions();

    // Find all positions by company ID where deletedAt is null and period status is not 'END'
    @Query("SELECT p FROM InternshipPosition p WHERE p.companyId = :companyId AND p.period.status != 'END' AND p.deletedAt IS NULL ORDER BY p.createdAt DESC")
    List<InternshipPosition> findActivePositionsByCompanyId(@Param("companyId") Integer companyId);
}
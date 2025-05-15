package com.dungnguyen.registration_service.repository;

import com.dungnguyen.registration_service.entity.InternshipPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipPeriodRepository extends JpaRepository<InternshipPeriod, String> {

    @Query("SELECT p FROM InternshipPeriod p WHERE p.deletedAt IS NULL ORDER BY p.id DESC")
    List<InternshipPeriod> findAllActivePeriods();

    @Query("SELECT p FROM InternshipPeriod p WHERE p.status = 'ACTIVE' AND p.deletedAt IS NULL")
    Optional<InternshipPeriod> findCurrentActivePeriod();

    @Query("SELECT p FROM InternshipPeriod p WHERE p.status = com.dungnguyen.registration_service.entity.InternshipPeriod.Status.UPCOMING AND p.deletedAt IS NULL")
    Optional<InternshipPeriod> findCurrentUpcomingPeriod();
}
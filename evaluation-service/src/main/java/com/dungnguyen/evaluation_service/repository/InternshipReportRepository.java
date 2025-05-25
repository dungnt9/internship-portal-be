package com.dungnguyen.evaluation_service.repository;

import com.dungnguyen.evaluation_service.entity.InternshipReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipReportRepository extends JpaRepository<InternshipReport, Integer> {
    List<InternshipReport> findByProgressId(Integer progressId);
    Optional<InternshipReport> findByProgressIdAndDeletedAtIsNull(Integer progressId);
}
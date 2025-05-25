package com.dungnguyen.evaluation_service.repository;

import com.dungnguyen.evaluation_service.entity.CompanyEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyEvaluationRepository extends JpaRepository<CompanyEvaluation, Integer> {
    Optional<CompanyEvaluation> findByProgressId(Integer progressId);
    boolean existsByProgressId(Integer progressId);
}

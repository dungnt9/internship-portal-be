package com.dungnguyen.evaluation_service.repository;

import com.dungnguyen.evaluation_service.entity.EvaluationCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationCriteriaRepository extends JpaRepository<EvaluationCriteria, Integer> {
}
package com.dungnguyen.evaluation_service.repository;

import com.dungnguyen.evaluation_service.entity.CompanyEvaluationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyEvaluationDetailRepository extends JpaRepository<CompanyEvaluationDetail, Integer> {
    List<CompanyEvaluationDetail> findByEvaluationId(Integer evaluationId);
}
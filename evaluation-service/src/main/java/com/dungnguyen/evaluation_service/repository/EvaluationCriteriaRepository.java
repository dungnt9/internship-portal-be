package com.dungnguyen.evaluation_service.repository;

import com.dungnguyen.evaluation_service.entity.EvaluationCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationCriteriaRepository extends JpaRepository<EvaluationCriteria, Integer> {
    List<EvaluationCriteria> findByDeletedAtIsNullOrderByIdAsc();

    // Find criteria by ID excluding soft deleted ones
    Optional<EvaluationCriteria> findByIdAndDeletedAtIsNull(Integer id);

    // Check if criteria with name exists (excluding soft deleted)
    boolean existsByNameAndDeletedAtIsNull(String name);

    // Check if criteria with name exists excluding current ID (for updates)
    boolean existsByNameAndDeletedAtIsNullAndIdNot(String name, Integer id);
}
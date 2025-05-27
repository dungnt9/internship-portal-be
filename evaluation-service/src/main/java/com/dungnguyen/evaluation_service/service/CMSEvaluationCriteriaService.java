package com.dungnguyen.evaluation_service.service;

import com.dungnguyen.evaluation_service.dto.EvaluationCriteriaDTO;
import com.dungnguyen.evaluation_service.dto.CMSEvaluationCriteriaCreateDTO;
import com.dungnguyen.evaluation_service.dto.CMSEvaluationCriteriaUpdateDTO;
import com.dungnguyen.evaluation_service.entity.EvaluationCriteria;
import com.dungnguyen.evaluation_service.exception.EvaluationCriteriaNotFoundException;
import com.dungnguyen.evaluation_service.repository.CompanyEvaluationDetailRepository;
import com.dungnguyen.evaluation_service.repository.EvaluationCriteriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CMSEvaluationCriteriaService {

    private final EvaluationCriteriaRepository criteriaRepository;
    private final CompanyEvaluationDetailRepository evaluationDetailRepository;

    /**
     * Get all evaluation criteria (excluding soft deleted ones)
     */
    public List<EvaluationCriteriaDTO> getAllCriteria() {
        List<EvaluationCriteria> criteria = criteriaRepository.findByDeletedAtIsNullOrderByIdAsc();
        return criteria.stream()
                .map(EvaluationCriteriaDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Get evaluation criteria by ID
     */
    public EvaluationCriteriaDTO getCriteriaById(Integer id) {
        EvaluationCriteria criteria = findCriteriaByIdNotDeleted(id);
        return new EvaluationCriteriaDTO(criteria);
    }

    /**
     * Create new evaluation criteria
     */
    @Transactional
    public EvaluationCriteriaDTO createCriteria(CMSEvaluationCriteriaCreateDTO createDTO) {
        // Validate input
        if (createDTO.getName() == null || createDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Criteria name is required");
        }

        // Check if criteria with same name already exists
        if (criteriaRepository.existsByNameAndDeletedAtIsNull(createDTO.getName().trim())) {
            throw new IllegalArgumentException("Evaluation criteria with this name already exists");
        }

        // Create new criteria
        EvaluationCriteria criteria = new EvaluationCriteria();
        criteria.setName(createDTO.getName().trim());
        criteria.setDescription(createDTO.getDescription() != null ? createDTO.getDescription().trim() : null);

        EvaluationCriteria savedCriteria = criteriaRepository.save(criteria);
        log.info("Created new evaluation criteria with ID: {}", savedCriteria.getId());

        return new EvaluationCriteriaDTO(savedCriteria);
    }

    /**
     * Update evaluation criteria
     */
    @Transactional
    public EvaluationCriteriaDTO updateCriteria(Integer id, CMSEvaluationCriteriaUpdateDTO updateDTO) {
        EvaluationCriteria criteria = findCriteriaByIdNotDeleted(id);

        // Update name if provided
        if (updateDTO.getName() != null && !updateDTO.getName().trim().isEmpty()) {
            String newName = updateDTO.getName().trim();

            // Check if another criteria with same name exists (excluding current criteria)
            if (criteriaRepository.existsByNameAndDeletedAtIsNullAndIdNot(newName, id)) {
                throw new IllegalArgumentException("Evaluation criteria with this name already exists");
            }

            criteria.setName(newName);
        }

        // Update description if provided
        if (updateDTO.getDescription() != null) {
            criteria.setDescription(updateDTO.getDescription().trim().isEmpty() ? null : updateDTO.getDescription().trim());
        }

        EvaluationCriteria updatedCriteria = criteriaRepository.save(criteria);
        log.info("Updated evaluation criteria with ID: {}", updatedCriteria.getId());

        return new EvaluationCriteriaDTO(updatedCriteria);
    }

    /**
     * Delete evaluation criteria (soft delete)
     */
    @Transactional
    public void deleteCriteria(Integer id) {
        EvaluationCriteria criteria = findCriteriaByIdNotDeleted(id);

        // Check if this criteria is being used in any evaluations
        long usageCount = evaluationDetailRepository.countByCriteriaIdAndDeletedAtIsNull(id);
        if (usageCount > 0) {
            throw new IllegalStateException("Cannot delete evaluation criteria that is currently being used in " + usageCount + " evaluation(s)");
        }

        // Perform soft delete
        criteria.setDeletedAt(LocalDateTime.now());
        criteriaRepository.save(criteria);

        log.info("Soft deleted evaluation criteria with ID: {}", id);
    }

    /**
     * Helper method to find criteria by ID (not deleted)
     */
    private EvaluationCriteria findCriteriaByIdNotDeleted(Integer id) {
        return criteriaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EvaluationCriteriaNotFoundException("Evaluation criteria not found with ID: " + id));
    }
}
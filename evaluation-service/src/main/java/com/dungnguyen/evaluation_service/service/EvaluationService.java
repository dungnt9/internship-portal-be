package com.dungnguyen.evaluation_service.service;

import com.dungnguyen.evaluation_service.entity.CompanyEvaluation;
import com.dungnguyen.evaluation_service.entity.CompanyEvaluationDetail;
import com.dungnguyen.evaluation_service.entity.EvaluationCriteria;
import com.dungnguyen.evaluation_service.entity.InternshipReport;
import com.dungnguyen.evaluation_service.repository.CompanyEvaluationDetailRepository;
import com.dungnguyen.evaluation_service.repository.CompanyEvaluationRepository;
import com.dungnguyen.evaluation_service.repository.EvaluationCriteriaRepository;
import com.dungnguyen.evaluation_service.repository.InternshipReportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvaluationService {

    private final CompanyEvaluationRepository companyEvaluationRepository;
    private final CompanyEvaluationDetailRepository companyEvaluationDetailRepository;
    private final EvaluationCriteriaRepository evaluationCriteriaRepository;
    private final InternshipReportRepository internshipReportRepository;

    @Transactional
    public void createEvaluationRecords(Integer progressId) {
        try {
            // Kiểm tra xem đã có evaluation cho progress này chưa
            if (companyEvaluationRepository.existsByProgressId(progressId)) {
                log.info("Evaluation records already exist for progress ID: {}", progressId);
                return;
            }

            // 1. Tạo Company Evaluation (bản ghi chính)
            CompanyEvaluation companyEvaluation = new CompanyEvaluation();
            companyEvaluation.setProgressId(progressId);
            // Các trường khác để null/default, sẽ được điền sau khi doanh nghiệp đánh giá

            CompanyEvaluation savedEvaluation = companyEvaluationRepository.save(companyEvaluation);
            log.info("Created company evaluation with ID: {} for progress ID: {}",
                    savedEvaluation.getId(), progressId);

            // 2. Tạo Company Evaluation Details cho từng criteria
            List<EvaluationCriteria> allCriteria = evaluationCriteriaRepository.findAll();

            for (EvaluationCriteria criteria : allCriteria) {
                CompanyEvaluationDetail detail = new CompanyEvaluationDetail();
                detail.setEvaluation(savedEvaluation);
                detail.setCriteria(criteria);
                // Comments để null, sẽ được điền sau

                companyEvaluationDetailRepository.save(detail);
            }

            log.info("Created {} evaluation detail records for evaluation ID: {}",
                    allCriteria.size(), savedEvaluation.getId());

            // 3. Tạo Internship Report (bản ghi trống)
            InternshipReport report = new InternshipReport();
            report.setProgressId(progressId);
            report.setTitle(""); // Để trống, sinh viên sẽ điền sau
            report.setContent(""); // Để trống, sinh viên sẽ điền sau
            // filePath và submissionDate để null

            InternshipReport savedReport = internshipReportRepository.save(report);
            log.info("Created empty internship report with ID: {} for progress ID: {}",
                    savedReport.getId(), progressId);

            log.info("Successfully created all evaluation records for progress ID: {}", progressId);

        } catch (Exception e) {
            log.error("Failed to create evaluation records for progress ID: {}", progressId, e);
            throw e; // Re-throw để RabbitMQ có thể retry
        }
    }
}
package com.dungnguyen.evaluation_service.listener;

import com.dungnguyen.evaluation_service.config.RabbitMQConfig;
import com.dungnguyen.evaluation_service.dto.message.InternshipProgressCreatedMessage;
import com.dungnguyen.evaluation_service.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InternshipProgressMessageListener {

    private final EvaluationService evaluationService;

    @RabbitListener(queues = RabbitMQConfig.PROGRESS_CREATED_QUEUE)
    public void handleInternshipProgressCreated(InternshipProgressCreatedMessage message) {
        try {
            log.info("Received internship progress created message: {}", message);

            // Tạo các bản ghi đánh giá
            evaluationService.createEvaluationRecords(message.getProgressId());

            log.info("Successfully processed internship progress created message for progress ID: {}",
                    message.getProgressId());

        } catch (Exception e) {
            log.error("Failed to process internship progress created message: {}", message, e);
            throw e; // Re-throw để message được gửi vào DLQ nếu cần
        }
    }
}
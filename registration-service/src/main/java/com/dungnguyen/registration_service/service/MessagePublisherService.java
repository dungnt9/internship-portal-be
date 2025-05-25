package com.dungnguyen.registration_service.service;

import com.dungnguyen.registration_service.config.RabbitMQConfig;
import com.dungnguyen.registration_service.dto.message.InternshipProgressCreatedMessage;
import com.dungnguyen.registration_service.entity.InternshipProgress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagePublisherService {

    private final RabbitTemplate rabbitTemplate;

    public void publishInternshipProgressCreated(InternshipProgress progress) {
        try {
            InternshipProgressCreatedMessage message = new InternshipProgressCreatedMessage();
            message.setProgressId(progress.getId());
            message.setStudentId(progress.getStudentId());
            message.setPositionId(progress.getPosition() != null ? progress.getPosition().getId() : null);
            message.setPeriodId(progress.getPeriod().getId());
            message.setTeacherId(progress.getTeacherId());
            message.setStartDate(progress.getStartDate());
            message.setEndDate(progress.getEndDate());
            message.setIsExternal(progress.getIsExternal());
            message.setExternalId(progress.getExternal() != null ? progress.getExternal().getId() : null);
            message.setExternalCompanyName(progress.getCompanyName());
            message.setExternalPositionTitle(progress.getPositionTitle());
            message.setCreatedAt(progress.getCreatedAt());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.INTERNSHIP_EXCHANGE,
                    RabbitMQConfig.PROGRESS_CREATED_ROUTING_KEY,
                    message
            );

            log.info("Published internship progress created message for progress ID: {}", progress.getId());
        } catch (Exception e) {
            log.error("Failed to publish internship progress created message for progress ID: {}",
                    progress.getId(), e);
        }
    }
}
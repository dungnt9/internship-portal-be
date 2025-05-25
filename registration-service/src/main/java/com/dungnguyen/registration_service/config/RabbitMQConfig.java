package com.dungnguyen.registration_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange
    public static final String INTERNSHIP_EXCHANGE = "internship.exchange";

    // Queue
    public static final String PROGRESS_CREATED_QUEUE = "progress.created.queue";

    // Routing Key
    public static final String PROGRESS_CREATED_ROUTING_KEY = "progress.created";

    @Bean
    public TopicExchange internshipExchange() {
        return new TopicExchange(INTERNSHIP_EXCHANGE);
    }

    @Bean
    public Queue progressCreatedQueue() {
        return QueueBuilder.durable(PROGRESS_CREATED_QUEUE).build();
    }

    @Bean
    public Binding progressCreatedBinding() {
        return BindingBuilder
                .bind(progressCreatedQueue())
                .to(internshipExchange())
                .with(PROGRESS_CREATED_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
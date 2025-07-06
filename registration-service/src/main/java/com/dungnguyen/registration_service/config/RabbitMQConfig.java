package com.dungnguyen.registration_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;   //kết nối tới RabbitMQ
import org.springframework.amqp.rabbit.core.RabbitTemplate;  //dùng để gửi message
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;  //convert object Java <-> JSON
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    //Khai báo các hằng số
    // Exchange
    public static final String INTERNSHIP_EXCHANGE = "internship.exchange";

    // Queue
    public static final String PROGRESS_CREATED_QUEUE = "progress.created.queue";

    // Routing Key
    public static final String PROGRESS_CREATED_ROUTING_KEY = "progress.created";
    //Khởi tạo một TopicExchange: kiểu exchange cho phép routing dựa vào pattern của routing key
    // (giống *.created hoặc internship.#).
    @Bean
    public TopicExchange internshipExchange() {
        return new TopicExchange(INTERNSHIP_EXCHANGE);
    }
    //Tạo một queue tên progress.created.queue, kiểu durable (khi RabbitMQ restart thì queue vẫn còn)
    @Bean
    public Queue progressCreatedQueue() {
        return QueueBuilder.durable(PROGRESS_CREATED_QUEUE).build();
    }
    //Nối progress.created.queue với internship.exchange
    //Dùng routing key "progress.created"
    //nếu message được gửi tới internship.exchange với routing key "progress.created"
    // thì sẽ được route vào queue này
    @Bean
    public Binding progressCreatedBinding() {
        return BindingBuilder
                .bind(progressCreatedQueue())
                .to(internshipExchange())
                .with(PROGRESS_CREATED_ROUTING_KEY);
    }
    // Tạo converter để RabbitMQ có thể tự động convert object Java thành JSON khi gửi,
    // và convert JSON thành object khi nhận.
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    //Dùng ConnectionFactory để kết nối đến RabbitMQ server.
    //Gắn Jackson2JsonMessageConverter để hỗ trợ gửi nhận object thay vì chuỗi thô.
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }
}
package com.baby_billing.brt.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Value("${rabbitmq.brt.to.hrs.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.brt.to.hrs.key}")
    private String routingKey;

    @Bean
    public Queue brtQueue() {
        return new Queue(queueName);
    }

    @Bean
    public TopicExchange brtExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding brtToCdrBinding() {
        return BindingBuilder.bind(brtQueue()).
                to(brtExchange())
                .with(routingKey);
    }
}

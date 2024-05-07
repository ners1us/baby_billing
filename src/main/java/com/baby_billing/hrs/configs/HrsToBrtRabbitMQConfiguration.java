package com.baby_billing.hrs.configs;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HrsToBrtRabbitMQConfiguration {

    @Value("${rabbitmq.month.hrs.to.brt.queue.name}")
    private String monthQueueName;

    @Value("${rabbitmq.call.hrs.to.brt.queue.name}")
    private String callQueueName;

    @Value("${rabbitmq.exchange.hrs-brt.name}")
    private String exchangeName;

    @Value("${rabbitmq.month.hrs.to.brt.key}")
    private String monthRoutingKey;

    @Value("${rabbitmq.call.hrs.to.brt.key}")
    private String callRoutingKey;

    @Bean
    public Queue callHrsQueue() {
        return new Queue(callQueueName);
    }

    @Bean
    public Queue monthHrsQueue() {
        return new Queue(monthQueueName);
    }

    @Bean
    public TopicExchange hrsExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding monthHrsToBrtBinding() {
        return BindingBuilder.bind(monthHrsQueue()).
                to(hrsExchange())
                .with(monthRoutingKey);
    }

    @Bean
    public Binding callHrsToBrtBinding() {
        return BindingBuilder.bind(callHrsQueue()).
                to(hrsExchange())
                .with(callRoutingKey);
    }
}

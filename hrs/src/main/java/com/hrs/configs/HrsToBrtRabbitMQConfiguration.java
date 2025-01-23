package com.hrs.configs;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HrsToBrtRabbitMQConfiguration {

    @Value("${rabbitmq.month.hrs.to.brt.queue.name}")
    private String monthQueueName;

    @Value("${rabbitmq.call.hrs.to.brt.queue.name}")
    private String callQueueName;

    @Value("${rabbitmq.brt.to.hrs.queue.name}")
    private String brtToHrsQueueName;

    @Value("${rabbitmq.exchange.brt-hrs.name}")
    private String exchangeName;

    @Value("${rabbitmq.month.hrs.to.brt.key}")
    private String monthRoutingKey;

    @Value("${rabbitmq.call.hrs.to.brt.key}")
    private String callRoutingKey;

    @Value("${rabbitmq.brt.to.hrs.key}")
    private String brtToHrsRoutingKey;

    @Bean
    public Queue callHrsQueue() {
        return new Queue(callQueueName);
    }

    @Bean
    public Queue monthHrsQueue() {
        return new Queue(monthQueueName);
    }

    @Bean
    public Queue brtToHrsQueue() {
        return new Queue(brtToHrsQueueName);
    }

    @Bean
    public DirectExchange hrsBrtExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public DirectExchange brtHrsExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding monthHrsToBrtBinding() {
        return BindingBuilder.bind(monthHrsQueue()).
                to(hrsBrtExchange())
                .with(monthRoutingKey);
    }

    @Bean
    public Binding callHrsToBrtBinding() {
        return BindingBuilder.bind(callHrsQueue()).
                to(hrsBrtExchange())
                .with(callRoutingKey);
    }

    @Bean
    public Binding brtToHrsBinding() {
        return BindingBuilder.bind(brtToHrsQueue()).to(brtHrsExchange()).with(brtToHrsRoutingKey);
    }
}

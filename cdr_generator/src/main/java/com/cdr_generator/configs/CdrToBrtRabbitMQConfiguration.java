package com.cdr_generator.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CdrToBrtRabbitMQConfiguration {

    @Value("${rabbitmq.cdr.to.brt.queue.name}")
    private String cdrToBrtQueue;

    @Value("${rabbitmq.exchange.cdr-brt.name}")
    private String cdrBrtExchangeName;

    @Value("${rabbitmq.cdr.to.brt.key}")
    private String cdrToBrtRoutingKey;

    @Bean
    public Queue cdrQueue() {
        return new Queue(cdrToBrtQueue);
    }

    @Bean
    public DirectExchange cdrBrtExchange() {
        return new DirectExchange(cdrBrtExchangeName);
    }

    @Bean
    public Binding bindingCdr() {
        return BindingBuilder.bind(cdrQueue())
                .to(cdrBrtExchange())
                .with(cdrToBrtRoutingKey);
    }

    @Bean
    public MessageConverter converter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}

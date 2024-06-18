package com.brt.configs;

import com.brt.converters.CdrHistoryMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrtRabbitMQConfiguration {

    @Value("${rabbitmq.cdr.to.brt.queue.name}")
    private String cdrToBrtQueueName;

    @Value("${rabbitmq.brt.to.hrs.queue.name}")
    private String brtToHrsQueueName;

    @Value("${rabbitmq.exchange.cdr-brt.name}")
    private String cdrBrtExchangeName;

    @Value("${rabbitmq.exchange.brt-hrs.name}")
    private String brtHrsExchangeName;

    @Value("${rabbitmq.cdr.to.brt.key}")
    private String cdrToBrtRoutingKey;

    @Value("${rabbitmq.brt.to.hrs.key}")
    private String brtToHrsRoutingKey;

    @Value("${rabbitmq.month.hrs.to.brt.queue.name}")
    private String monthQueueName;

    @Value("${rabbitmq.call.hrs.to.brt.queue.name}")
    private String callQueueName;

    @Value("${rabbitmq.exchange.brt-hrs.name}")
    private String exchangeName;

    @Value("${rabbitmq.month.hrs.to.brt.key}")
    private String monthRoutingKey;

    @Value("${rabbitmq.call.hrs.to.brt.key}")
    private String callRoutingKey;

    @Bean
    public Queue cdrToBrtQueue() {
        return new Queue(cdrToBrtQueueName);
    }

    @Bean
    public Queue brtToHrsQueue() {
        return new Queue(brtToHrsQueueName);
    }

    @Bean
    public TopicExchange cdrBrtExchange() {
        return new TopicExchange(cdrBrtExchangeName);
    }

    @Bean
    public TopicExchange brtHrsExchange() {
        return new TopicExchange(brtHrsExchangeName);
    }

    @Bean
    public TopicExchange hrsBrtExchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding cdrToBrtBinding() {
        return BindingBuilder.bind(cdrToBrtQueue()).to(cdrBrtExchange()).with(cdrToBrtRoutingKey);
    }

    @Bean
    public Binding brtToHrsBinding() {
        return BindingBuilder.bind(brtToHrsQueue()).to(brtHrsExchange()).with(brtToHrsRoutingKey);
    }

    @Bean
    public Queue callHrsQueue() {
        return new Queue(callQueueName);
    }

    @Bean
    public Queue monthHrsQueue() {
        return new Queue(monthQueueName);
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
    public MessageConverter jsonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }
    @Bean
    public CdrHistoryMessageConverter cdrHistoryMessageConverter(ObjectMapper objectMapper) {
        return new CdrHistoryMessageConverter(objectMapper);
    }

}

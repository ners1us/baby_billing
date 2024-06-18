package com.brt.converters;

import com.brt.dto.CdrHistory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@Component
public class CdrHistoryMessageConverter implements MessageConverter {

    private final ObjectMapper objectMapper;

    public CdrHistoryMessageConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Object fromMessage(Message message) {
        try {
            return objectMapper.readValue(message.getBody(), CdrHistory.class);
        } catch (Exception e) {
            throw new MessageConversionException("Failed to convert message to CdrHistory", e);
        }
    }

    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) {
        throw new UnsupportedOperationException("Conversion to message is not supported");
    }
}

package com.baby_billing.hrs.converters;

import com.baby_billing.hrs.models.TariffRules;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TariffRulesConverter implements AttributeConverter<TariffRules, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(TariffRules tariffRules) {
        try {
            return objectMapper.writeValueAsString(tariffRules);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Error converting TariffRules to JSON string", ex);
        }
    }

    @Override
    public TariffRules convertToEntityAttribute(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, TariffRules.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Error converting JSON string to TariffRules", ex);
        }
    }
}

package com.hrs.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrs.models.TariffRules;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Конвертер для преобразования объектов типа TariffRules в строку JSON и обратно.
 */
@Converter
public class TariffRulesConverter implements AttributeConverter<TariffRules, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Преобразует объект TariffRules в строку JSON.
     *
     * @param tariffRules Объект TariffRules для преобразования.
     * @return Строка JSON.
     * @throws RuntimeException если возникает ошибка при преобразовании.
     */
    @Override
    public String convertToDatabaseColumn(TariffRules tariffRules) {
        try {
            return objectMapper.writeValueAsString(tariffRules);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Error converting TariffRules to JSON string", ex);
        }
    }

    /**
     * Преобразует строку JSON в объект TariffRules.
     *
     * @param jsonString Строка JSON для преобразования.
     * @return Объект TariffRules.
     * @throws RuntimeException если возникает ошибка при преобразовании.
     */
    @Override
    public TariffRules convertToEntityAttribute(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, TariffRules.class);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Error converting JSON string to TariffRules", ex);
        }
    }
}

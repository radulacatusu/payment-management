package com.mine.payment.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @stefanl
 */
@Component
public class JsonUtil {

    /**
     * @param values
     * @return
     * @throws JsonProcessingException
     */
    public String writeValueAsString(Map<String, String> values) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(values);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}

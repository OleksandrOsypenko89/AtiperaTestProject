package com.osypenko.atiperatestproject.model.httpWrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({
        "documentation_url"
})
public class ExceptionResponseMessage {
    private String message;

    public String exceptionMessage(HttpResponseWrapper responseWrapper, ObjectMapper objectMapper) {
        try {
            var responseMessage = objectMapper.readValue(responseWrapper.body(), ExceptionResponseMessage.class);
            return responseMessage.getMessage();
        } catch (JsonProcessingException e) {
            log.error("Error parsing exception message: {}", responseWrapper.body());
            return responseWrapper.status() != 200 ? "Not found" : "Error processing request";
        }
    }
}

package com.osypenko.atiperatestproject.model.httpWrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({
        "documentation_url"
})
public class ResponseMessage {
    private String message;

    public String exceptionMessage(HttpResponseWrapper responseWrapper, ObjectMapper objectMapper) {
        try {
            ResponseMessage responseMessage = objectMapper.readValue(responseWrapper.getBody(), ResponseMessage.class);
            return responseMessage.getMessage();
        } catch (JsonProcessingException e) {
            log.error("Error parsing exception message: {}", responseWrapper.getBody(), e);
            return "Error processing request";
        }
    }
}

package com.osypenko.atiperatestproject.model.httpWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExceptionResponseMessageTest {
    ObjectMapper objectMapper;
    HttpResponseWrapper responseWrapper;
    ExceptionResponseMessage exceptionResponseMessage;

    @BeforeEach
    void setUp() {
        objectMapper = mock(ObjectMapper.class);
        responseWrapper = mock(HttpResponseWrapper.class);
        exceptionResponseMessage = new ExceptionResponseMessage();
    }

    @Test
    void responseBodyMessage_ValidResponse() throws JsonProcessingException {
        when(responseWrapper.body()).thenReturn("{\"message\":\"Test message\"}");
        when(objectMapper.readValue(anyString(), eq(ExceptionResponseMessage.class)))
                .thenReturn(new ExceptionResponseMessage("Test message") {});

        String result = exceptionResponseMessage.responseBodyMessage(responseWrapper, objectMapper);

        assertEquals("Test message", result);
    }

    @Test
    void responseBodyMessage_InvalidJson() throws JsonProcessingException {
        when(responseWrapper.body()).thenReturn("Invalid JSON");
        when(objectMapper.readValue(anyString(), eq(ExceptionResponseMessage.class)))
                .thenThrow(new JsonProcessingException("Test") {});

        when(responseWrapper.status()).thenReturn(404);

        String result = exceptionResponseMessage.responseBodyMessage(responseWrapper, objectMapper);

        assertEquals("Not Found", result);
    }
}
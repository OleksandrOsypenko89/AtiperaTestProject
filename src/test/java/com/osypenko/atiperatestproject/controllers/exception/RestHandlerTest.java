package com.osypenko.atiperatestproject.controllers.exception;

import com.osypenko.atiperatestproject.controllers.RepositoryRestController;
import com.osypenko.atiperatestproject.exception.ExceptionMessage;
import com.osypenko.atiperatestproject.services.RepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RepositoryRestController.class)
class RestHandlerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    private RepositoryService repositoryService;

    String testLogin = "testRepositoryLogin";

    @Test
    void handleConflict_ShouldReturnCorrectResponseEntity() throws Exception {
        var statusTest = 404;
        var messageTest = "Resource Not Found";
        var testExceptionMessage = new ExceptionMessage(statusTest, messageTest);

        when(repositoryService.parseRepository(testLogin)).thenThrow(testExceptionMessage);

        mockMvc.perform(get("/{testLogin}", testLogin)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(statusTest))
                .andExpect(jsonPath("$.message").value(messageTest));
    }

    @Test
    void handleGenericException_HandlingAllUnexpectedErrors() throws Exception {
        var testRuntimeException = new RuntimeException();

        when(repositoryService.parseRepository(testLogin)).thenThrow(testRuntimeException);

        mockMvc.perform(get("/{testLogin}", testLogin)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}
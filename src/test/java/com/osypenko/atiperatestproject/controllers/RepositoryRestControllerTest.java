package com.osypenko.atiperatestproject.controllers;

import com.osypenko.atiperatestproject.dto.BranchDTO;
import com.osypenko.atiperatestproject.dto.RepositoryDTO;
import com.osypenko.atiperatestproject.services.RepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RepositoryRestController.class)
class RepositoryRestControllerTest {
    @MockBean
    RepositoryService repositoryService;
    @Autowired
    MockMvc mockMvc;

    String testLogin = "testRepositoryLogin";

    List<RepositoryDTO> getDtoList() {
        var expectedBranchDTOOne = new BranchDTO(
                "testBranchNameOne"
                , "testCommitSHAOne"
        );
        var expectedBranchDTOTwo = new BranchDTO(
                "testBranchNameTwo"
                , "testCommitSHATwo"
        );
        var expectedBranchDTOList = List.of(expectedBranchDTOOne, expectedBranchDTOTwo);

        var expectedDTO = new RepositoryDTO(
                "testRepositoryName"
                , "testRepositoryLogin"
                , expectedBranchDTOList
        );
        return List.of(expectedDTO);
    }

    @Test
    void login_ResponseIsTrue() throws Exception {
        when(repositoryService.parseRepository(testLogin)).thenReturn(getDtoList());

        mockMvc.perform(get("/{testLogin}", testLogin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].repositoryName").value("testRepositoryName"))
                .andExpect(jsonPath("$.[0].ownerLogin").value("testRepositoryLogin"))
                .andExpect(jsonPath("$.[0].branches[0].branchName").value("testBranchNameOne"))
                .andExpect(jsonPath("$.[0].branches[0].commitSha").value("testCommitSHAOne"))
                .andExpect(jsonPath("$.[0].branches[1].branchName").value("testBranchNameTwo"))
                .andExpect(jsonPath("$.[0].branches[1].commitSha").value("testCommitSHATwo"));

        verify(repositoryService).parseRepository(testLogin);
    }

    @Test
    void login_HeaderIsTrue() throws Exception {
        mockMvc.perform(get("/{testLogin}", testLogin)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

        verify(repositoryService).parseRepository(testLogin);
    }

    @Test
    void login_HeaderIsFalse() throws Exception {
        mockMvc.perform(get("/{testLogin}", testLogin)
                        .accept(MediaType.APPLICATION_XML_VALUE))
                .andExpect(status().isNotAcceptable());

        verify(repositoryService, never()).parseRepository(testLogin);
    }
}

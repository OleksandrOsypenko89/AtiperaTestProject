package com.osypenko.atiperatestproject.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osypenko.atiperatestproject.dto.BranchDTO;
import com.osypenko.atiperatestproject.dto.RepositoryDTO;
import com.osypenko.atiperatestproject.exception.ExceptionMessage;
import com.osypenko.atiperatestproject.model.httpWrapper.ResponseMessage;
import com.osypenko.atiperatestproject.model.repository.Branch;
import com.osypenko.atiperatestproject.model.httpWrapper.HttpResponseWrapper;
import com.osypenko.atiperatestproject.model.repository.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepositoryService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ResponseMessage responseMessage = new ResponseMessage();

    private HttpResponseWrapper request(String url) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new HttpResponseWrapper(response.statusCode(), response.body());
        } catch (IOException | InterruptedException e) {
            log.error("Not found {}", url);
            throw new RuntimeException("Failed to send request to: " + url, e);
        }
    }



    private void checkStatus(HttpResponseWrapper responseWrapper) {
        if (responseWrapper.getStatus() != 200) {
            try {
                throw objectMapper.readValue(responseWrapper.getBody(), ExceptionMessage.class);
            } catch (Exception e) {
                String message = responseMessage.exceptionMessage(responseWrapper, objectMapper);
                log.error("Request failed with status {}: {}", responseWrapper.getStatus(), message);
                throw new ExceptionMessage(responseWrapper.getStatus(), message);
            }
        }
    }



    private List<BranchDTO> parseBranch(String[] url) {
        HttpResponseWrapper responseWrapper = request(url[0]);

        try {
            checkStatus(responseWrapper);

            List<Branch> branches = objectMapper.readValue(responseWrapper.getBody(), new TypeReference<>() {});
            List<BranchDTO> branchesDTO = new ArrayList<>();
            for (Branch branch : branches) {
                BranchDTO branchDTO = new BranchDTO();

                log.info("Branch name: {}", branch.getName());
                log.info("Sha: {}", branch.getCommit().getSha());
                branchDTO.setBranchName(branch.getName());
                branchDTO.setCommitSha(branch.getCommit().getSha());
                branchesDTO.add(branchDTO);
            }
            return branchesDTO;
        } catch (Exception e) {
            log.error("Error parsing branches", e);
            String message = responseMessage.exceptionMessage(responseWrapper, objectMapper);
            throw new ExceptionMessage(responseWrapper.getStatus(), message);
        }
    }



    public List<RepositoryDTO> parseRepository(String userName) {
        HttpResponseWrapper responseWrapper = request("https://api.github.com/users/" + userName + "/repos");

        try {
            checkStatus(responseWrapper);

            List<Repository> repositories = objectMapper.readValue(responseWrapper.getBody(), new TypeReference<>() {});
            List<RepositoryDTO> repositoryDTOList = new ArrayList<>();
            for (Repository repository : repositories) {
                if (!repository.isFork()) {
                    RepositoryDTO repositoryDTO = new RepositoryDTO();

                    log.info("Repository Name: {}", repository.getName());
                    repositoryDTO.setRepositoryName(repository.getName());

                    log.info("Owner Login: {}", repository.getOwner().getLogin());
                    repositoryDTO.setOwnerLogin(repository.getOwner().getLogin());

                    List<BranchDTO> branchDTOList = parseBranch(repository.getBranches_url().split("\\{"));
                    repositoryDTO.setBranches(branchDTOList);

                    repositoryDTOList.add(repositoryDTO);
                }
            }
            return repositoryDTOList;
        } catch (Exception e) {
            log.error("Error parsing repositories", e);
            String message = responseMessage.exceptionMessage(responseWrapper, objectMapper);
            throw new ExceptionMessage(responseWrapper.getStatus(), message);
        }
    }

}

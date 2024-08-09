package com.osypenko.atiperatestproject.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osypenko.atiperatestproject.dto.BranchDTO;
import com.osypenko.atiperatestproject.dto.RepositoryDTO;
import com.osypenko.atiperatestproject.exception.ExceptionMessage;
import com.osypenko.atiperatestproject.model.httpWrapper.ExceptionResponseMessage;
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
    private final ExceptionResponseMessage exceptionResponseMessage = new ExceptionResponseMessage();

    private HttpResponseWrapper request(String url) {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new HttpResponseWrapper(response.statusCode(), response.body());
        } catch (IOException | InterruptedException e) {
            log.error("Not found {}", url);
            throw new RuntimeException("Failed to send request to: " + url, e);
        }
    }



    private List<BranchDTO> parseBranch(String[] url) {
        var responseWrapper = request(url[0]);

        try {
            var branches = objectMapper.readValue(responseWrapper.body(), new TypeReference<List<Branch>>() {});
            var branchesDTO = new ArrayList<BranchDTO>();
            for (var branch : branches) {
                var branchDTO = new BranchDTO(branch.name(), branch.commit().sha());

                log.info("Branch name: {}", branch.name());
                log.info("Sha: {}", branch.commit().sha());
                branchesDTO.add(branchDTO);
            }
            return branchesDTO;

        } catch (Exception e) {
            log.error("Error parsing branches", e);
            var message = exceptionResponseMessage.exceptionMessage(responseWrapper, objectMapper);
            throw new ExceptionMessage(responseWrapper.status(), message);
        }
    }



    public List<RepositoryDTO> parseRepository(String userName) {
        var responseWrapper = request("https://api.github.com/users/" + userName + "/repos");

        try {
            var repositories = objectMapper.readValue(responseWrapper.body(), new TypeReference<List<Repository>>() {});
            var repositoryDTOList = new ArrayList<RepositoryDTO>();
            for (var repository : repositories) {
                if (!repository.fork()) {
                    var branchDTOList = parseBranch(repository.branches_url().split("\\{"));
                    var repositoryDTO = new RepositoryDTO(repository.name(), repository.owner().login(), branchDTOList);

                    log.info("Repository Name: {}", repository.name());
                    log.info("Owner Login: {}", repository.owner().login());
                    repositoryDTOList.add(repositoryDTO);
                }
            }
            return repositoryDTOList;

        } catch (Exception e) {
            log.error("Error parsing repositories", e);
            var message = exceptionResponseMessage.exceptionMessage(responseWrapper, objectMapper);
            throw new ExceptionMessage(responseWrapper.status(), message);
        }
    }

}

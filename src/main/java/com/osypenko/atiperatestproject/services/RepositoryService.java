package com.osypenko.atiperatestproject.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osypenko.atiperatestproject.dto.BranchDTO;
import com.osypenko.atiperatestproject.dto.RepositoryDTO;
import com.osypenko.atiperatestproject.exception.ExceptionMessage;
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

    private HttpResponseWrapper request(String url) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new HttpResponseWrapper(response.statusCode(), response.body());
    }



    private List<BranchDTO> parseBranch(String[] url) {
        HttpResponseWrapper responseWrapper = request(url[0]);
        ObjectMapper objectMapper = new ObjectMapper();
        List<BranchDTO> branchesDTO = new ArrayList<>();

        checkStatus(responseWrapper);

        try {
            List<Branch> branches = objectMapper.readValue(responseWrapper.getBody(), new TypeReference<>() {});
            for (Branch branch : branches) {
                log.info("Branch name: {}", branch.getName());
                log.info("Sha: {}", branch.getCommit().getSha());
                BranchDTO branchDTO = new BranchDTO(branch.getName(), branch.getCommit().getSha());
                branchesDTO.add(branchDTO);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return branchesDTO;
    }



    public List<RepositoryDTO> parseRepository(String login, List<RepositoryDTO> repositoryDTOList) {
        HttpResponseWrapper responseWrapper = request("https://api.github.com/users/" + login + "/repos");
        ObjectMapper objectMapper = new ObjectMapper();

        checkStatus(responseWrapper);

        try {
            List<Repository> repositories = objectMapper.readValue(responseWrapper.getBody(), new TypeReference<>() {});
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return repositoryDTOList;
    }



    public void checkStatus(HttpResponseWrapper responseWrapper) {
        if (responseWrapper.getStatus() != 200) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                throw objectMapper.readValue(responseWrapper.getBody(), ExceptionMessage.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

}

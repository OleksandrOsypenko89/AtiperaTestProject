package com.osypenko.atiperatestproject.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osypenko.atiperatestproject.model.Branch;
import com.osypenko.atiperatestproject.model.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class Services {

    private String request(String url) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();
    }

    private void parseBranch(String[] url) {
        String responseBody = request(url[0]);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Branch> branches = objectMapper.readValue(responseBody, new TypeReference<>(){});
            for (Branch branch : branches) {
                    log.info("Branch name: {}", branch.getName());
                    log.info("SHA: {}", branch.getCommit().getSha());
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    public void parseRepository(String login) {
        String responseBody = request("https://api.github.com/users/" + login + "/repos");
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Repository> repositories = objectMapper.readValue(responseBody, new TypeReference<>(){});
            for (Repository repository : repositories) {
                if (!repository.isFork()) {
                    log.info("Name: {}", repository.getName());
                    log.info("Login: {}", repository.getOwner().getLogin());
                    parseBranch(repository.getBranches_url().split("\\{"));
                }
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

}

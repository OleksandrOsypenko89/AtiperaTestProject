package com.osypenko.atiperatestproject.controllers;

import com.osypenko.atiperatestproject.dto.RepositoryDTO;
import com.osypenko.atiperatestproject.services.RepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RepositoryRestController {
    private final RepositoryService repositoryService;

    @GetMapping("/{username}")
    public ResponseEntity<List<RepositoryDTO>> login(
            @PathVariable String username,
            @RequestHeader HttpHeaders header
    ) {
        List<RepositoryDTO> repositoryDTOList = new ArrayList<>();

        if (Objects.requireNonNull(header.get("Accept")).contains("application/json")) {
            log.info("Header: {}", header.get("Accept"));
            log.info("Login repo: {}", username);
            repositoryDTOList = repositoryService.parseRepository(username, repositoryDTOList);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(repositoryDTOList);
    }
}

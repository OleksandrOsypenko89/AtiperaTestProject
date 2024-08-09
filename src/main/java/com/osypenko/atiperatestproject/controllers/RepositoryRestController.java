package com.osypenko.atiperatestproject.controllers;

import com.osypenko.atiperatestproject.dto.RepositoryDTO;
import com.osypenko.atiperatestproject.services.RepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RepositoryRestController {
    private final RepositoryService repositoryService;

    @RequestMapping(
            value = "/{username}"
            , method = RequestMethod.GET
            , produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<RepositoryDTO>> login(@PathVariable String username) {

        log.info("Login repo: {}", username);
        List<RepositoryDTO> repositoryDTOList = repositoryService.parseRepository(username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(repositoryDTOList);
    }
}

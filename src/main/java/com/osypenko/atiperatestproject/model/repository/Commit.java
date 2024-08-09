package com.osypenko.atiperatestproject.model.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({
        "url"
})
public record Commit(
        String sha
) {}

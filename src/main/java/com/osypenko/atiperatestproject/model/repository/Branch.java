package com.osypenko.atiperatestproject.model.repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({
        "protected"
})
public record Branch(
        String name
        , Commit commit
) {}

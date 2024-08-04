package com.osypenko.atiperatestproject.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({
        "url"
})
public class Commit {
    private String sha;
}

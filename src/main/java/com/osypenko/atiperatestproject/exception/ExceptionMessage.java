package com.osypenko.atiperatestproject.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({
        "cause",
        "stackTrace",
        "suppressed",
        "localizedMessage",
        "documentation_url",
})
public class ExceptionMessage extends RuntimeException {
    private int status;
    private String message;
}

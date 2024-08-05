package com.osypenko.atiperatestproject.model.httpWrapper;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponseWrapper {
    private int status;
    private String body;
}

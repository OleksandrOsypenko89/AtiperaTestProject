package com.osypenko.atiperatestproject.model.httpWrapper;

public record HttpResponseWrapper(
        int status
        , String body
) {}

package com.osypenko.atiperatestproject.exception;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionMessage extends RuntimeException {
    String status;
    String message;
}

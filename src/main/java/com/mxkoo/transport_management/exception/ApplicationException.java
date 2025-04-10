package com.mxkoo.transport_management.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationException extends RuntimeException {

    private String message;
    private HttpStatus httpStatus;
    private Instant timestamp;

    public static ApplicationException of(String message, HttpStatus httpStatus) {
        return new ApplicationException(message, httpStatus, Instant.now());
    }

    public static ApplicationException notFound(String message) {
        return new ApplicationException(message, HttpStatus.NOT_FOUND, Instant.now());
    }

}

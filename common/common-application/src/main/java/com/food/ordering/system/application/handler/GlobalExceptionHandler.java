package com.food.ordering.system.application.handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(Exception ex) {
        log.error("Exception occurred: {}", ex.getMessage());
        return ErrorDTO.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Unexpected error occurred")
                .build();
    }


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(ValidationException ex) {
        log.error("Exception occurred: {}", ex.getMessage());
        ErrorDTO errorDTO;

        if (ex instanceof ConstraintViolationException ce) {
            String violations = extractViolations(ce);
            log.error("violations: {}", violations,ce);
            errorDTO = ErrorDTO.builder()
                    .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message(violations)
                    .build();
        } else {

            log.error("violations: {}", ex.getMessage(),ex);

            errorDTO = ErrorDTO.builder()
                    .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                    .message(ex.getMessage())
                    .build();
        }


        return errorDTO;
    }

    private String extractViolations(ConstraintViolationException ce) {
        return ce.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("--"));

    }

}

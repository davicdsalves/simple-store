package com.exam.store.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity errorHandler(Exception e) {
        LOGGER.error("some error occurred", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity errorHandler(IllegalArgumentException e) {
        LOGGER.error("request error", e);
        Map<String, Object> response = createSimpleResponse(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    private Map<String, Object> createSimpleResponse(Object value) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", value);
        return response;
    }

}

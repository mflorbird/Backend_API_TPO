package com.naiki.ecommerce.controllers.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ExceptionHandlerConfig {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return new ResponseEntity<>("Los sentimos, no podemos cargar tu solicitud: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

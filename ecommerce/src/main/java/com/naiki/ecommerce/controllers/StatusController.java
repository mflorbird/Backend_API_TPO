package com.naiki.ecommerce.controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/status")

public class StatusController {

    @GetMapping
    public ResponseEntity<Map<String, String>> getStatus() {
        return ResponseEntity.ok().body(Map.of("status", "ok"));
    }
}

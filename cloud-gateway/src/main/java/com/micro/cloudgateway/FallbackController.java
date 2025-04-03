package com.micro.cloudgateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @RequestMapping("/resource-service-fallback")
    public ResponseEntity<String> resourceServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Resource Service is temporarily unavailable. Please try again later.");
    }

    @RequestMapping("/song-service-fallback")
    public ResponseEntity<String> songServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Song Service is temporarily unavailable. Please try again later.");
    }

    @RequestMapping("/default-fallback")
    public ResponseEntity<String> defaultFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("The service is temporarily unavailable. Please try again later.");
    }
}
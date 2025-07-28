package com.ghost_providers.cred.controller;

import com.ghost_providers.cred.dto.ProviderIntakeRequest;
import com.ghost_providers.cred.service.ProviderIntakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/intake")
@RequiredArgsConstructor
public class ProviderIntakeController {

    private final ProviderIntakeService providerIntakeService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> intakeProvider(@RequestBody ProviderIntakeRequest request) {
        Long providerId = providerIntakeService.createProvider(request);
        return ResponseEntity.ok(Map.of("providerId", providerId));
    }
}


package com.ghost_providers.cred.controller;

import com.ghost_providers.cred.dto.ProviderDTO;
import com.ghost_providers.cred.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/providers")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderService providerService;

    @GetMapping
    public List<ProviderDTO> getAllProviders() {
        return providerService.getAllProviders();
    }

    @GetMapping("/{id}")
    public ProviderDTO getProvider(@PathVariable Long id) {
        return providerService.getProvider(id);
    }

    @PostMapping("/update-status/{id}")
    public ResponseEntity<Map<String,String>> updateStatus (@PathVariable Long id, @RequestParam String status) { return providerService.updateStatus(id, status);}
}


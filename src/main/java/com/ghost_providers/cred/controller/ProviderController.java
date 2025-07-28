package com.ghost_providers.cred.controller;

import com.ghost_providers.cred.dto.ProviderDTO;
import com.ghost_providers.cred.service.ProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}


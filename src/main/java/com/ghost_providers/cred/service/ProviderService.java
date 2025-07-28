package com.ghost_providers.cred.service;

import com.ghost_providers.cred.dto.ProviderDTO;
import com.ghost_providers.cred.mapper.ProviderMapper;
import com.ghost_providers.cred.model.Provider;
import com.ghost_providers.cred.repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderService {
    private final ProviderRepository providerRepository;

    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    public List<ProviderDTO> getAllProviders() {
        return providerRepository.findAll()
                .stream()
                .map(ProviderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProviderDTO getProvider(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        return ProviderMapper.toDTO(provider);
    }
}

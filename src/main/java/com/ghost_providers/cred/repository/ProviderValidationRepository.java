package com.ghost_providers.cred.repository;

import com.ghost_providers.cred.model.Provider;
import com.ghost_providers.cred.model.ProviderValidation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderValidationRepository extends JpaRepository<ProviderValidation, Long> {
}


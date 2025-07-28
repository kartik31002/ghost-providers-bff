package com.ghost_providers.cred.repository;

import com.ghost_providers.cred.model.ProviderLicense;
import com.ghost_providers.cred.model.ProviderSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderSpecialtyRepository extends JpaRepository<ProviderSpecialty, Long> {
}


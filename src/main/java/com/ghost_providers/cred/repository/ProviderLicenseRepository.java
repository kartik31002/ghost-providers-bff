package com.ghost_providers.cred.repository;

import com.ghost_providers.cred.model.Provider;
import com.ghost_providers.cred.model.ProviderLicense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderLicenseRepository extends JpaRepository<ProviderLicense, Long> {
}


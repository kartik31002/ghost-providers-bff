package com.ghost_providers.cred.repository;

import com.ghost_providers.cred.model.Provider;
import com.ghost_providers.cred.model.ProviderEducation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderEducationRepository extends JpaRepository<ProviderEducation, Long> {
}


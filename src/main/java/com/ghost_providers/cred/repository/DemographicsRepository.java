package com.ghost_providers.cred.repository;

import com.ghost_providers.cred.model.Demographics;
import com.ghost_providers.cred.model.ProviderPsvStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemographicsRepository extends JpaRepository<Demographics, Long> {
}


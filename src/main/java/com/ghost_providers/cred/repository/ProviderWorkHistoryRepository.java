package com.ghost_providers.cred.repository;

import com.ghost_providers.cred.model.Provider;
import com.ghost_providers.cred.model.ProviderWorkHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderWorkHistoryRepository extends JpaRepository<ProviderWorkHistory, Long> {
}


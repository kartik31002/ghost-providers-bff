package com.ghost_providers.cred.repository;

import com.ghost_providers.cred.model.Provider;
import com.ghost_providers.cred.model.ProviderDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderDocumentRepository extends JpaRepository<ProviderDocument, Long> {
}


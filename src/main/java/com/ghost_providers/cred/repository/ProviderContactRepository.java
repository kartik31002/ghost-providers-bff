package com.ghost_providers.cred.repository;

import com.ghost_providers.cred.model.ProviderContact;
import com.ghost_providers.cred.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderContactRepository extends JpaRepository<ProviderContact, Long> {
}


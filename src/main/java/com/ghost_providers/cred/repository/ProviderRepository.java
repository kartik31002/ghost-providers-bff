package com.ghost_providers.cred.repository;

import com.ghost_providers.cred.model.Provider;
import com.ghost_providers.cred.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProviderRepository extends JpaRepository<Provider, Long> {
    List<Provider> findByStatus(Provider.Status status);
}



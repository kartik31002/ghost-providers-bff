package com.ghost_providers.cred.repository;

import com.ghost_providers.cred.model.FileDocument;
import com.ghost_providers.cred.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDocumentRepository extends JpaRepository<FileDocument, Long> {
}


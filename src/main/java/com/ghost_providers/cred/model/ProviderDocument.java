package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "provider_documents")
@Data
public class ProviderDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @Column(name = "type", length = 100)
    private String type;

    @Column(name = "filename", length = 255)
    private String filename;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;

    @Column(name = "uploaded_at", updatable = false)
    private LocalDateTime uploadedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "ocr_status")
    private OcrStatus ocrStatus;

    @Column(name = "metadata_json", columnDefinition = "JSON")
    private String metadataJson;

    @PrePersist
    protected void onUpload() {
        this.uploadedAt = LocalDateTime.now();
    }

    public enum OcrStatus {
        success, fail, low_confidence
    }

    // Getters and setters
}


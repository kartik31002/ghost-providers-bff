package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "provider_validations")
@Data
public class ProviderValidation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @Column(name = "field", length = 100)
    private String field;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ValidationStatus status;

    @Column(name = "error_details", columnDefinition = "TEXT")
    private String errorDetails;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    public enum ValidationStatus {
        pending, passed, failed
    }

    // Getters and setters
}


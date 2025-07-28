package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "provider_validation_errors")
@Data
public class ValidationError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    private String field;
    private String message;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    public enum Severity {
        ERROR, WARNING
    }
}

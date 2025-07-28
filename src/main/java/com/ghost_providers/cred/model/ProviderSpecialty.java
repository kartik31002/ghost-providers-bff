package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "provider_specialties")
@Data
public class ProviderSpecialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    private String taxonomyCode;
    private String description;
    private boolean isPrimary;
}

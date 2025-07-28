package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "provider_credentials")
@Data
public class ProviderCredential {

    @Id
    private Long providerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "provider_id")
    private Provider provider;

    private String npi;
    private String tin;
    private String medicareId;
    private String medicaidId;
    private String deaNumber;
}



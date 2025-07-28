package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "provider_contacts")
@Data
public class ProviderContact {

    @Id
    private Long providerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "provider_id")
    private Provider provider;

    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
}

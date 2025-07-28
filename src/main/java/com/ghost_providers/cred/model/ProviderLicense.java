package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "provider_licenses")
@Data
public class ProviderLicense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    private String state;
    private String licenseNumber;
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    private LicenseStatus status;

    @Getter
    public enum LicenseStatus {
        ACTIVE("Active"),
        EXPIRED("Expired"),
        SUSPENDED("Suspended"),
        PENDING("Pending");

        private final String label;

        LicenseStatus(String label) {
            this.label = label;
        }
    }
}

package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "provider_education")
@Data
public class ProviderEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @Column(name = "institution", length = 255)
    private String institution;

    @Column(name = "degree", length = 100)
    private String degree;

    @Column(name = "field", length = 100)
    private String field;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "document_url", columnDefinition = "TEXT")
    private String documentUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "verified_status")
    private VerifiedStatus verifiedStatus;

    @Column(name = "verification_date")
    private LocalDateTime verificationDate;

    public enum VerifiedStatus {
        pending, verified, failed
    }

    // Getters and setters
}

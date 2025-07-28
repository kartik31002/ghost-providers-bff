package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "provider_psv_status")
@Data
public class ProviderPsvStatus {

    @Id
    private Long providerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private VerificationStatus licenses;

    @Enumerated(EnumType.STRING)
    private VerificationStatus deaNpi;

    @Enumerated(EnumType.STRING)
    private VerificationStatus education;

    @Enumerated(EnumType.STRING)
    private VerificationStatus malpractice;

    @Enumerated(EnumType.STRING)
    private VerificationStatus workHistory;

    @Enumerated(EnumType.STRING)
    private VerificationStatus sanctions;

    @Enumerated(EnumType.STRING)
    private OverallStatus overallStatus;

    public enum VerificationStatus {
        VERIFIED, PENDING, FAILED, NOT_STARTED
    }

    public enum OverallStatus {
        NOT_STARTED, IN_PROGRESS, COMPLETED, ISSUES_FOUND
    }

    @PrePersist
    protected void onCreate() {
        this.licenses = VerificationStatus.NOT_STARTED;
        this.deaNpi = VerificationStatus.NOT_STARTED;
        this.education = VerificationStatus.NOT_STARTED;
        this.malpractice = VerificationStatus.NOT_STARTED;
        this.workHistory = VerificationStatus.NOT_STARTED;
        this.sanctions = VerificationStatus.NOT_STARTED;
        this.overallStatus = OverallStatus.NOT_STARTED;
    }
}



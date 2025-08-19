package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

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

    @Getter
    public enum VerificationStatus {
        VERIFIED("verified"), PENDING("pending"), FAILED("failed"), NOT_STARTED("not-started");

        private final String label;

        VerificationStatus(String label) {
            this.label = label;
        }
    }

    @Getter
    public enum OverallStatus {
        NOT_STARTED("not-started"), IN_PROGRESS("in-progress"), COMPLETED("completed"), ISSUES_FOUND("issues-found");
        private final String label;

        OverallStatus(String label) {
            this.label = label;
        }
    }

    @PrePersist
    protected void onCreate() {
        this.licenses = VerificationStatus.PENDING;
        this.deaNpi = VerificationStatus.PENDING;
        this.education = VerificationStatus.PENDING;
        this.malpractice = VerificationStatus.PENDING;
        this.workHistory = VerificationStatus.PENDING;
        this.sanctions = VerificationStatus.PENDING;
        this.overallStatus = OverallStatus.COMPLETED;
    }
}



package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "committee_reviews")
@Data
public class CommitteeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private Provider provider;

    @Column(name = "pdf_summary_url", columnDefinition = "TEXT")
    private String pdfSummaryUrl;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    private User reviewer;

    @Enumerated(EnumType.STRING)
    @Column(name = "decision")
    private Decision decision;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "network_flag")
    private Boolean networkFlag;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    public enum Decision {
        approved, rejected, request_info
    }

    // Getters and setters
}

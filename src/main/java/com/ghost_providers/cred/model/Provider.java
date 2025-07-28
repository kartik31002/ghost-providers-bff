package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "providers")
@Data
public class Provider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String roId;

    @Enumerated(EnumType.STRING)
    private IntakeSource intakeSource;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "provider", cascade = CascadeType.ALL)
    private ProviderCredential credentials;

    @OneToOne(mappedBy = "provider", cascade = CascadeType.ALL)
    private ProviderContact contact;

    @OneToOne(mappedBy = "provider", cascade = CascadeType.ALL)
    private Demographics demographics;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<ValidationError> validationErrors;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<ProviderLicense> stateLicenses;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<ProviderSpecialty> specialties;

    @OneToOne(mappedBy = "provider", cascade = CascadeType.ALL)
    private ProviderPsvStatus psvStatus;

    @Getter
    public enum IntakeSource {
        ROSTER_AUTOMATION("Roster Automation"),
        PROVIDER_EMAIL("Provider Email"),
        FILE_UPLOAD("File Upload");

        private final String label;

        IntakeSource(String label) {
            this.label = label;
        }
    }

    @Getter
    public enum Status {
        NEW("New"),
        APPLICATION_REVIEW_IN_PROGRESS("Application Review In Progress"),
        APPLICATION_SUBMITTED("Application Submitted"),
        PSV_IN_PROGRESS("PSV In Progress"),
        COMMITTEE_APPROVAL_PENDING("Committee Approval Pending"),
        APPLICATION_VALIDATION_FAILED("Application Validation Failed"),
        PSV_FAILED("PSV Failed"),
        PROVIDER_CREDENTIALED("Provider Credentialed");

        private final String label;

        Status(String label) {
            this.label = label;
        }

    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = Status.NEW;
    }

}

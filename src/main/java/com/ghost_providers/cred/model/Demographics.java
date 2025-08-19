package com.ghost_providers.cred.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "provider_demographics")
@Data
public class Demographics {

    @Id
    private Long providerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @Column(name = "dob")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String ssn;

    @Getter
    public enum Gender {
        MALE("male"), FEMALE("female"), OTHER("other"), PREFER_NOT_TO_SAY("prefer-not-to-say");
        private final String label;

        Gender(String label) {
            this.label = label;
        }
    }
}

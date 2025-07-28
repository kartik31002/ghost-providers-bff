package com.ghost_providers.cred.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProviderIntakeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String intakeSource;

    private CredentialsDto credentials;
    private DemographicsDto demographics;
    private ContactDto contactInfo;
    private List<LicenseDto> licenses;
    private List<SpecialtyDto> specialties;
}


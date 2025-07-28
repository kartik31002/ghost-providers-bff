package com.ghost_providers.cred.dto;

import lombok.Data;

import java.util.List;

@Data
public class CredentialsDto {
    private String npi;
    private String tin;
    private List<LicenseDto> stateLicenses;
    private List<SpecialtyDto> specialties;
    private String deaNumber;
    private String caqhId;
    private String medicareId;
    private String medicaidId;
}

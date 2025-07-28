package com.ghost_providers.cred.dto;

import lombok.Data;

@Data
public class MedicalLicenseResponse {
    private String providerFullName;
    private String licenseNumber;
    private String licenseEffectiveDate;
    private String licenseExpirationDate;
    private String issuingAuthority;
    private String issuingState;
    private String issuanceDate;
}


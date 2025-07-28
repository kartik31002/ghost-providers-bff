package com.ghost_providers.cred.dto;

import lombok.Data;

@Data
public class HealthInsuranceResponse {
    private String insuredName;
    private String policyNumber;
    private String insuranceProvider;
    private String groupNumber;
    private String coverageEffectiveDate;
    private String coverageExpirationDate;
    private String planType;
    private String coverageDetails;
    private String issuedBy;
}

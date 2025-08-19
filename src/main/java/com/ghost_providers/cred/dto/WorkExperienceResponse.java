package com.ghost_providers.cred.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WorkExperienceResponse {
    @JsonProperty("Provider Full Name")
    private String providerFullName;

    @JsonProperty("NPI Number")
    private String npiNumber;

    @JsonProperty("Specialty")
    private String specialty;

    @JsonProperty("State License Number")
    private String stateLicenseNumber;

    @JsonProperty("Employment History")
    private List<EmploymentHistoryEntry> employmentHistory;

    @JsonProperty("overall_gap_flag")
    private boolean overallGapFlag;
}

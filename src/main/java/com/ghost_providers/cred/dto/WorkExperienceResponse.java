package com.ghost_providers.cred.dto;

import lombok.Data;

import java.util.List;



@Data
public class WorkExperienceResponse {
    private String providerFullName;
    private String npiNumber;
    private String specialty;
    private String stateLicenseNumber;
    private List<EmploymentHistoryEntry> employmentHistory;
    private boolean overallGapFlag;
}


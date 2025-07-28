package com.ghost_providers.cred.dto;

import lombok.Data;

@Data
public class EmploymentHistoryEntry {
    private String organization;
    private String role;
    private String location;
    private String startDate;
    private String endDate;
    private boolean gapFlag;
    private String gapReasonComputed;
}

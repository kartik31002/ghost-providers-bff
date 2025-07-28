package com.ghost_providers.cred.dto;

import lombok.Data;

@Data
public class PsvStatusDto {
    private String licenses;
    private String deaNpi;
    private String education;
    private String malpractice;
    private String workHistory;
    private String sanctions;
    private String overallStatus;
}
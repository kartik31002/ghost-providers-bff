package com.ghost_providers.cred.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LicenseDto {
    private String state;
    private String licenseNumber;
    private LocalDate expirationDate;
    private String status;
}
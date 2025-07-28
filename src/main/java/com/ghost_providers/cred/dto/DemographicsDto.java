package com.ghost_providers.cred.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DemographicsDto {
    private LocalDate dateOfBirth;
    private String gender;
    private String ssn;
}

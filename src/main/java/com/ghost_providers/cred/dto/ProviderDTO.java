package com.ghost_providers.cred.dto;

import java.util.List;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProviderDTO {

    private Long id;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String roId;
    private String intakeSource;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private DemographicsDto demographics;
    private ContactDto contact;
    private CredentialsDto credentials;
    private PsvStatusDto psvStatus;
    private List<ValidationErrorDto> validationErrors;

}


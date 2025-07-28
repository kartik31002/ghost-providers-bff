package com.ghost_providers.cred.dto;


import lombok.Data;

@Data
public class ValidationErrorDto {
    private String field;
    private String message;
    private String severity;
}
package com.ghost_providers.cred.dto;

import lombok.Data;

@Data
public class ContactDto {
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String phone;
    private String email;
}

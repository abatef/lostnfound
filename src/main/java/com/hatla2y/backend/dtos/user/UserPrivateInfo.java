package com.hatla2y.backend.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivateInfo {
    private String email;
    private String phoneNumber;
    private String homeAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}

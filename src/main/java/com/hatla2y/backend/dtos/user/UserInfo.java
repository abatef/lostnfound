package com.hatla2y.backend.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {
    public static class Address {
        private String homeAddress;
        private String city;
        private String state;
        private String zipCode;
        private String country;
    }
}

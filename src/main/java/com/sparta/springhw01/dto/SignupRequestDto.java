package com.sparta.springhw01.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    private String username;
    private String password;
    private String email;
    private boolean admin;
    private String adminToken = "";

    public boolean isAdmin() {
        return admin;
    }
}
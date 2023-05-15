package org.api.dealshopper.models;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String email;
    private String newPassword;

    public PasswordResetRequest(String password, String mail){
        newPassword=password;
        email=mail;
    }
    public PasswordResetRequest(){};
}
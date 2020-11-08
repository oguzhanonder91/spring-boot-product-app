package com.common.dto;

import com.util.validation.PasswordMatches;
import com.util.validation.ValidPassword;

@PasswordMatches
public class PasswordDto {

    private String oldPassword;

    @ValidPassword
    private String password;

    private String matchingPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}

package com.common.dto;

import com.util.validation.PasswordMatches;
import com.util.validation.ValidEmail;
import com.util.validation.ValidPassword;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordMatches
public class UserDto {

    @NotNull
    @Size(min = 1, message = "{Size.userDto.firstName}")
    private String name;

    @NotNull
    @Size(min = 1, message = "{Size.userDto.lastName}")
    private String surname;

    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

    @NotNull
    @Size(min = 1)
    private String roleCode;

    @ValidEmail
    @NotNull
    @Size(min = 1, message = "{Size.userDto.email}")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}

package com.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

@Component
public class SecurityUtil {

    @Value("${decodeSplit}")
    private String split;

    public String getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return "anonymous";
        }

        return ((User) authentication.getPrincipal()).getUsername();
    }

    public LoginRequest loginRequestBase64Decode(LoginRequest loginRequest) {
        byte [] decodeUsername = Base64Utils.decode(Base64Utils.decodeFromString(loginRequest.getUsername()));
        byte [] decodePassword = Base64Utils.decode(Base64Utils.decodeFromString(loginRequest.getPassword()));
        String pass = new String(decodePassword).split(split)[0];
        String user = new String(decodeUsername).split(split)[0];
        loginRequest.setPassword(pass);
        loginRequest.setUsername(user);
        return loginRequest;
    }
}

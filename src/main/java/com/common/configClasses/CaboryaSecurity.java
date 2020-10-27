package com.common.configClasses;

public class CaboryaSecurity {

    private String decodeSplit;

    private String base64Secret;

    private String secret;

    private long tokenValidity;

    private long rememberMeTokenValidity;

    private long tokenRegistrationAndResetPasswordExpiration;

    public String getDecodeSplit() {
        return decodeSplit;
    }

    public void setDecodeSplit(String decodeSplit) {
        this.decodeSplit = decodeSplit;
    }

    public String getBase64Secret() {
        return base64Secret;
    }

    public void setBase64Secret(String base64Secret) {
        this.base64Secret = base64Secret;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getTokenValidity() {
        return tokenValidity;
    }

    public void setTokenValidity(long tokenValidity) {
        this.tokenValidity = tokenValidity;
    }

    public long getRememberMeTokenValidity() {
        return rememberMeTokenValidity;
    }

    public void setRememberMeTokenValidity(long rememberMeTokenValidity) {
        this.rememberMeTokenValidity = rememberMeTokenValidity;
    }

    public long getTokenRegistrationAndResetPasswordExpiration() {
        return tokenRegistrationAndResetPasswordExpiration;
    }

    public void setTokenRegistrationAndResetPasswordExpiration(long tokenRegistrationAndResetPasswordExpiration) {
        this.tokenRegistrationAndResetPasswordExpiration = tokenRegistrationAndResetPasswordExpiration;
    }
}

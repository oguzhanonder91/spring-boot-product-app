package com.common.dto;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;

    private final String token;

    private long expirationDate;

    private long issuedAt;

    public JwtResponse(String token, long expirationDate, long issuedAt) {
        this.token = token;
        this.expirationDate = expirationDate;
        this.issuedAt = issuedAt;
    }

    public String getToken() {
        return this.token;
    }

    public long getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(long expirationDate) {
        this.expirationDate = expirationDate;
    }

    public long getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(long issuedAt) {
        this.issuedAt = issuedAt;
    }
}

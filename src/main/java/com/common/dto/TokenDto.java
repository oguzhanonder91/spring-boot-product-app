package com.common.dto;

import com.util.enums.TokenType;

import javax.persistence.Column;

/**
 * @author Oğuzhan ÖNDER
 * @date 14.04.2021 - 12:12
 */
public class TokenDto extends BaseDto{

    private TokenType tokenType;

    private String value;

    private String domain;

    private String email;

    private long expiry;

    private long issuedAt;

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public long getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(long issuedAt) {
        this.issuedAt = issuedAt;
    }
}

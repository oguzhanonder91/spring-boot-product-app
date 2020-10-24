package com.common.entity;

import com.util.enums.TokenType;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Token extends BaseEntity {

    @Column
    private TokenType tokenType;

    @Column
    private String value;

    @Column
    private String domain;

    @Column
    private String email;

    @Column
    private long expiry;

    @Column
    private long issuedAt;

    public Token() {
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    private void setTokenType(TokenType tokenType) {
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

    private void setExpiry(long expiry) {
        this.expiry = expiry;
    }

    public long getIssuedAt() {
        return issuedAt;
    }

    private void setIssuedAt(long issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Token tokenType(TokenType tokenType) {
        this.setTokenType(tokenType);
        return this;
    }

    public Token value(String value) {
        this.setValue(value);
        return this;
    }

    public Token email(String email) {
        this.setEmail(email);
        return this;
    }

    public Token expiry(long expiry) {
        this.setExpiry(expiry);
        return this;
    }

    public Token issuedAt(long issuedAt) {
        this.setIssuedAt(issuedAt);
        return this;
    }

}

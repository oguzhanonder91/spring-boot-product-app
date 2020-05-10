package com.common.entity;

import com.util.enums.TokenType;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Token extends BaseEntity{

    @Column
    private TokenType tokenType;

    @Column
    private String value;

    @Column
    private String domain;

    @Column
    private String email;


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
}

package com.util;

import com.common.configuration.CaboryaConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Autowired
    private CaboryaConfig caboryaConfig;

    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes;
        try {
            String secret = caboryaConfig.getSecurity().getSecret();
            if (!StringUtils.isEmpty(secret)) {
                LOGGER.warn("Warning: the JWT key used is not Base64-encoded. " +
                        "We recommend using the `caborya.security.base64-secret` key for optimum security.");
                keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            } else {
                LOGGER.debug("Using a Base64-encoded JWT secret key");
                keyBytes = Decoders.BASE64.decode(caboryaConfig.getSecurity().getBase64Secret());
            }
            this.key = Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public String generateJwtToken(UserDetails userDetails, boolean rememberMe) {
        long issueDate = new Date().getTime();
        long validity = validity(rememberMe);
        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date(issueDate))
                .setExpiration(new Date(issueDate + validity))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }

    public long getExpirationDate(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getExpiration().getTime();
    }

    public long getIssuedDate(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getIssuedAt().getTime();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (SecurityException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

    private long validity(boolean isRememberMe) {
        long validity;
        if (isRememberMe) {
            validity = caboryaConfig.getSecurity().getRememberMeTokenValidity() * 1000;
        } else {
            validity = caboryaConfig.getSecurity().getTokenValidity() * 1000;
        }
        return validity;
    }

}

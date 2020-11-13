package com.common.dao;

import com.common.dto.LoginRequest;
import com.common.entity.Token;
import com.common.exception.BaseException;
import com.common.security.MyUserDetailsService;
import com.util.JwtTokenUtil;
import com.util.SecurityUtil;
import com.util.enums.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthDao {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private TokenDao tokenDao;

    public Token loginAttempt(LoginRequest loginRequest, HttpServletRequest request) {
        LoginRequest decode = securityUtil.decode(loginRequest);
        authenticate(decode.getUsername(), decode.getPassword());
        UserDetails userDetails = controlUserDetailService(decode);
        String token = generateToken(userDetails, loginRequest.isRememberMe());
        return prepare(token, userDetails, request);
    }

    public Token logoutAttempt(HttpServletRequest request) {
        final String user = securityUtil.getCurrentAuditor();
        String token = jwtTokenUtil.parseJwt(request);
        return tokenDao.controlToken(token, user, TokenType.AUTH, request);
    }

    private UserDetails controlUserDetailService(LoginRequest decodeLoginRequest) {
        return userDetailsService
                .loadUserByUsername(decodeLoginRequest.getUsername());
    }

    private String generateToken(UserDetails userDetails, boolean isRememberMe) {
        return jwtTokenUtil.generateJwtToken(userDetails, isRememberMe);
    }

    private Token prepare(String token, UserDetails userDetails, HttpServletRequest httpServletRequest) {
        long expiry = jwtTokenUtil.getExpirationDate(token);
        long issuedAt = jwtTokenUtil.getIssuedDate(token);
        Token newTokenObj = new Token()
                .value(token)
                .email(userDetails.getUsername())
                .tokenType(TokenType.AUTH)
                .expiry(expiry)
                .issuedAt(issuedAt);
        return tokenDao.tokenPrepare(newTokenObj, httpServletRequest);
    }

    private void authenticate(final String username, final String password) throws BaseException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new BaseException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BaseException("INVALID_CREDENTIALS", e);
        }
    }
}


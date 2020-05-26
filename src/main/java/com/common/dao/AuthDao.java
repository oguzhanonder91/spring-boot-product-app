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
        String token = generateToken(userDetails);
        return prepare(token, userDetails, request);
    }

    public Token logoutAttempt(HttpServletRequest request) {
        final String user = securityUtil.getCurrentAuditor();
        String token = jwtTokenUtil.parseJwt(request);
        Token validToken = tokenDao.controlToken(token, user, TokenType.AUTH, request);
        return validToken;
    }

    private UserDetails controlUserDetailService(LoginRequest decodeLoginRequest) {
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(decodeLoginRequest.getUsername());
        return userDetails;
    }

    private String generateToken(UserDetails userDetails) {
        final String token = jwtTokenUtil.generateJwtToken(userDetails);
        return token;
    }

    private Token prepare(String token, UserDetails userDetails, HttpServletRequest httpServletRequest) {
        long expiry = jwtTokenUtil.getExpirationDate(token);
        Token tokenObj = tokenDao.tokenPrepare(token, userDetails.getUsername(), TokenType.AUTH, expiry, httpServletRequest);
        return tokenObj;
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


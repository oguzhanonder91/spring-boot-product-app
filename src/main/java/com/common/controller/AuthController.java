package com.common.controller;

import com.common.dao.TokenDao;
import com.common.entity.Token;
import com.common.exception.BaseException;
import com.common.security.MyUserDetailsService;
import com.common.service.TokenService;
import com.util.JwtResponse;
import com.util.JwtTokenUtil;
import com.util.LoginRequest;
import com.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    private TokenService tokenService;

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody final LoginRequest loginRequest, HttpServletRequest httpServletRequest) throws BaseException {
        LoginRequest decodeLoginRequest = securityUtil.decode(loginRequest);
        authenticate(decodeLoginRequest.getUsername(), decodeLoginRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(decodeLoginRequest.getUsername());
        final String token = jwtTokenUtil.generateJwtToken(userDetails);
        Token tokenObj = tokenDao.tokenCreate(token, userDetails.getUsername(), httpServletRequest);
        tokenService.save(tokenObj);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping(value = "/logout")
    @ResponseBody
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) throws BaseException {
        final String user = securityUtil.getCurrentAuditor();
        String token = jwtTokenUtil.parseJwt(httpServletRequest);
        Token validToken = tokenDao.controlToken(token, user, httpServletRequest);
        if (validToken != null) {
            tokenService.deleteReal(validToken);
        }
        return ResponseEntity.ok(user);
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


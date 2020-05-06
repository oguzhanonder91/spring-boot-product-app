package com.common.controller;

import com.common.exception.BaseException;
import com.common.security.MyUserDetailsService;
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

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody final LoginRequest loginRequest) throws BaseException {
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(loginRequest.getUsername());
        final String token = jwtTokenUtil.generateJwtToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping(value = "/logout")
    @ResponseBody
    public ResponseEntity<?> logout() throws BaseException {
        final String user = securityUtil.getCurrentAuditor();
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


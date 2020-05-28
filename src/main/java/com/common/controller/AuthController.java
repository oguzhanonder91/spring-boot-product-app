package com.common.controller;

import com.common.dao.AuthDao;
import com.common.dao.TokenDao;
import com.common.dto.JwtResponse;
import com.common.dto.LoginRequest;
import com.common.entity.Token;
import com.common.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    private AuthDao authDao;

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody final LoginRequest loginRequest, HttpServletRequest httpServletRequest) throws BaseException {
        Token token = authDao.loginAttempt(loginRequest,httpServletRequest);
        tokenDao.create(token);
        return ResponseEntity.ok(new JwtResponse(token.getValue(),token.getExpiry(),token.getIssuedAt()));
    }

    @PostMapping(value = "/logout")
    @ResponseBody
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) throws BaseException {
        Token token= authDao.logoutAttempt(httpServletRequest);
        if (token!= null) {
            tokenDao.deleteRealToken(token);
        }
        return ResponseEntity.ok("Başarılı Şekilde Çıkış Yapıldı");
    }


}


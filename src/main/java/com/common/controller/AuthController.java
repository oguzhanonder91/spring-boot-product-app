package com.common.controller;

import com.common.dao.AuthDao;
import com.common.dao.TokenDao;
import com.common.dao.UserDao;
import com.common.dto.JwtResponse;
import com.common.dto.LoginRequest;
import com.common.entity.Token;
import com.common.entity.User;
import com.common.exception.BaseException;
import com.util.annotations.MyServiceAnnotation;
import com.util.annotations.MyServiceGroupAnnotation;
import com.util.enums.MethodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("auth")
@MyServiceGroupAnnotation(name = "Kimlik Doğrulama",path = "auth")
public class AuthController {

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    private AuthDao authDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageSource messages;

    @PostMapping(value = "/login")
    @MyServiceAnnotation(name = "Login", path = "/login", type = MethodType.POST, permissionRoles = {"ADMIN","USER"})
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody final LoginRequest loginRequest, HttpServletRequest httpServletRequest) throws BaseException {
        Token token = authDao.loginAttempt(loginRequest, httpServletRequest);
        tokenDao.create(token);
        User user = userDao.findByEmail(token.getEmail());
        user.setLastLoginTime(new Date(System.currentTimeMillis()));
        userDao.update(user);
        return ResponseEntity.ok(new JwtResponse(token.getValue(), token.getExpiry(), token.getIssuedAt()));
    }

    @PostMapping(value = "/logout")
    @MyServiceAnnotation(name = "Logout", path = "/logout", type = MethodType.POST, permissionRoles = {"ADMIN","USER"})
    @ResponseBody
    public ResponseEntity<?> logout(HttpServletRequest httpServletRequest) throws BaseException {
        Token token = authDao.logoutAttempt(httpServletRequest);
        if (token != null) {
            User user = userDao.findByEmail(token.getEmail());
            user.setLastLogoutTime(new Date(System.currentTimeMillis()));
            userDao.update(user);
            tokenDao.deleteRealToken(token);
        }
        return ResponseEntity.ok(messages.getMessage("message.logoutSucc", null, httpServletRequest.getLocale()));
    }


}


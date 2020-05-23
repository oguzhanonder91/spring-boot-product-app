package com.common.controller;

import com.common.dao.UserDao;
import com.common.dto.UserDto;
import com.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @PostMapping(path = "/registration")
    public ResponseEntity<String> registrationNewUser(@Valid @RequestBody final UserDto userDto) {
        final User user = userDao.registerNewUserAccount(userDto);
        userDao.create(user);
        return new ResponseEntity<>("Kayıt Başarılı", HttpStatus.OK);
    }
}

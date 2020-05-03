package com.common.controller;

import com.common.dao.UserDao;
import com.common.dto.UserDto;
import com.common.entity.User;
import com.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/registration")
    public ResponseEntity<String> registrationNewUser(@Valid @RequestBody UserDto userDto) {
        User user = userDao.registerNewUserAccount(userDto);
        userService.save(user);
        return new ResponseEntity<>("Kayıt Başarılı", HttpStatus.OK);
    }
}

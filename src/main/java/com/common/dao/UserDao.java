package com.common.dao;

import com.common.dto.UserDto;
import com.common.entity.User;
import com.common.exception.BaseException;
import com.common.service.RoleService;
import com.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UserDao {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUserAccount(final UserDto accountDto) {
        if (emailExist(accountDto.getEmail())) {
            throw new BaseException("There is an account with that email adress: " + accountDto.getEmail());
        }
        User user = new User();

        user.setName(accountDto.getName());
        user.setSurname(accountDto.getSurname());
        user.setEmail(accountDto.getEmail());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setRoles(Arrays.asList(roleService.findByCode(accountDto.getRoleCode())));
        user.setEnabled(true);
        return user;
    }

    private boolean emailExist(final String email) {
        return userService.findByEmail(email) != null;
    }
}

package com.common.dao;

import com.common.dto.UserDto;
import com.common.entity.User;
import com.common.exception.BaseException;
import com.common.exception.BaseNotFoundException;
import com.common.service.UserService;
import com.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UserDao {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityUtil securityUtil;

    public User registerNewUserAccount(final UserDto accountDto) {
        if (emailExist(accountDto.getEmail())) {
            throw new BaseException("There is an account with that email adress: " + accountDto.getEmail());
        }
        User user = new User();

        user.setName(accountDto.getName());
        user.setSurname(accountDto.getSurname());
        user.setEmail(accountDto.getEmail());
        user.setPassword(passwordEncoder.encode(securityUtil.decode(accountDto.getPassword())));
        user.setRoles(Arrays.asList(roleDao.findByCode(accountDto.getRoleCode())));
        return user;
    }

    public User save(final User user) {
        return userService.save(user);
    }

    public boolean emailExist(final String email) {
        return userService.findByEmail(email) != null;
    }

    public User findByEmail(final String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new BaseNotFoundException("Not found User -> " + email);
        }
        return user;
    }

    public User update(User user) {
        return userService.update(user);
    }

    public User findById(String id) {
        User user = userService.findByIdAndEntityState(id);
        if (user == null) {
            throw new BaseNotFoundException("Not found User -> " + id);
        }
        return user;
    }

    public void changePassword(User user , String password){
        user.setPassword(passwordEncoder.encode(securityUtil.decode(password)));
        update(user);
    }

}

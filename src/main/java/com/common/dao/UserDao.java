package com.common.dao;

import com.common.dto.UserDto;
import com.common.entity.User;
import com.common.exception.BaseException;
import com.common.exception.BaseNotFoundException;
import com.common.service.UserService;
import com.common.specification.SearchCriteria;
import com.common.specification.SearchOperation;
import com.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final String userRoleCode = "USER";

    public User registerNewUserAccount(final UserDto accountDto) {
        if (emailExist(accountDto.getEmail())) {
            throw new BaseException("There is an account with that email adress: " + accountDto.getEmail());
        }
        User user = new User();

        user.setName(accountDto.getName());
        user.setSurname(accountDto.getSurname());
        user.setEmail(accountDto.getEmail());
        user.setPassword(passwordEncoder.encode(securityUtil.decode(accountDto.getPassword())));
        if (accountDto.getRoles() == null || accountDto.getRoles().isEmpty()) {
            user.setRoles(Arrays.asList(roleDao.findByCode(userRoleCode)));
        } else {
            user.setRoles(roleDao.findByCode(accountDto.getRoles().stream().map(m -> m.getCode()).collect(Collectors.toList())));
        }
        return user;
    }

    public User save(final User user) {
        return userService.saveForEntity(user);
    }

    public boolean emailExist(final String email) {
        return this.findUser(email) != null;
    }

    private User findUser(String email) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .and(SearchOperation.EQUAL, "email", email)
                .buildActive();
        User user = userService.caboryaFindByParamsForEntity(searchCriteria).size() > 0
                ? userService.caboryaFindByParamsForEntity(searchCriteria).get(0)
                : null;
        return user;
    }

    public User findByEmail(final String email) {
        User user = this.findUser(email);
        if (user == null) {
            throw new BaseNotFoundException("Not found User -> " + email);
        }
        return user;
    }

    public User update(User user) {
        return userService.updateForEntity(user);
    }

    public User findById(String id) {
        Optional<User> user = userService.findByIdActiveForEntity(id);
        if (!user.isPresent()) {
            throw new BaseNotFoundException("Not found User -> " + id);
        }
        return user.get();
    }

    public void changePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(securityUtil.decode(password)));
        update(user);
    }

}

package com.common.service.impl;

import com.common.entity.User;
import com.common.repository.UserRepository;
import com.common.service.UserService;
import com.util.enums.EntityState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByEmail(final String email) {
        return userRepository.findByEmailAndEntityState(email, EntityState.ACTIVE);
    }

    @Override
    public User findByIdAndEntityState(String id) {
        return userRepository.findByIdAndEntityState(id, EntityState.ACTIVE);
    }
}


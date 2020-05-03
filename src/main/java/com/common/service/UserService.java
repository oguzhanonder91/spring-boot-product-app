package com.common.service;


import com.common.entity.User;

public interface UserService extends BaseService<User>{
    User findByEmail(String email);
}

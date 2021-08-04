package com.common.service.impl;

import com.common.dto.UserDto;
import com.common.entity.User;
import com.common.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends BaseServiceImpl<User, UserDto> implements UserService {

}


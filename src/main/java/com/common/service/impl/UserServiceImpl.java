package com.common.service.impl;

import com.common.dto.UserDto;
import com.common.entity.User;
import com.common.repository.UserRepository;
import com.common.service.UserService;
import com.util.enums.EntityState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends BaseServiceImpl<User, UserDto> implements UserService {

}


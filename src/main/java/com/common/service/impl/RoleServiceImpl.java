package com.common.service.impl;

import com.common.entity.Role;
import com.common.repository.RoleRepository;
import com.common.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by oguzhanonder - 19.10.2018
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role findByCode(String code) {
        return roleRepository.findByCode(code);
    }
}

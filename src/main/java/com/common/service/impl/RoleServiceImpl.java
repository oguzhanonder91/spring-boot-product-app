package com.common.service.impl;

import com.common.entity.Role;
import com.common.repository.RoleRepository;
import com.common.service.RoleService;
import com.util.enums.EntityState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findByName(final String name) {
        return roleRepository.findByNameAndEntityState(name, EntityState.ACTIVE);
    }

    @Override
    public Role findByCode(final String code) {
        return roleRepository.findByCodeAndEntityState(code, EntityState.ACTIVE);
    }
}

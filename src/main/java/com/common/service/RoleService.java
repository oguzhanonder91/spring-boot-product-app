package com.common.service;


import com.common.entity.Role;

public interface RoleService extends BaseService<Role> {

    Role findByName(final String name);

    Role findByCode(final String code);
}

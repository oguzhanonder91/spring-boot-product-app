package com.common.service;


import com.common.entity.Role;

public interface RoleService extends BaseService<Role> {

    Role findByName(String name);

    Role findByCode(String code);
}

package com.common.dao;

import com.common.entity.Role;
import com.common.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleDao {

    @Autowired
    private RoleService roleService;

    public Role save(Role role) {
        return roleService.save(role);
    }

    public Role findByCode(String code) {
        return roleService.findByCode(code);
    }

    public Role findByName(String name) {
        return roleService.findByName(name);
    }

    public Role update(Role role) {
        return roleService.update(role);
    }

    public void softDelete(Role role) {
        roleService.softDelete(role);
    }

    public List<Role> findAll() {
        return roleService.findAll();
    }
}

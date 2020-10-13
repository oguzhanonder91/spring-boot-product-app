package com.common.service.impl;

import com.common.entity.Permission;
import com.common.entity.Role;
import com.common.repository.PermissionRepository;
import com.common.service.PermissionService;
import com.util.enums.PermissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Optional<Permission> findByTypeAndItemId(PermissionType permissionType, String itemId) {
        return permissionRepository.findByTypeAndItemId(permissionType, itemId);
    }

    @Override
    public List<Permission> findByItemIdIn(List<String> items) {
        return permissionRepository.findByItemIdIn(items);
    }

    @Override
    public Optional<Permission> findByItemIdAndTypeAndRoles_Code(String item, PermissionType permissionType, String code) {
        return permissionRepository.findByItemIdAndTypeAndRoles_Code(item, permissionType, code);
    }

    @Override
    public List<Permission> findByTypeAndRoles_CodeIn(PermissionType permissionType, List<String> codes) {
        return permissionRepository.findByTypeAndRoles_CodeIn(permissionType, codes);
    }

    @Override
    public List<Permission> findByItemIdAndTypeAndRolesIn(String item, PermissionType permissionType, List<Role> roles) {
        return permissionRepository.findByItemIdAndTypeAndRolesIn(item, permissionType, roles);
    }
}

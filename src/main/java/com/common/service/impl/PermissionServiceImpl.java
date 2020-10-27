package com.common.service.impl;

import com.common.entity.Permission;
import com.common.entity.Role;
import com.common.repository.PermissionRepository;
import com.common.service.PermissionService;
import com.util.enums.EntityState;
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
        return permissionRepository.findByTypeAndItemIdAndEntityState(permissionType, itemId, EntityState.ACTIVE);
    }

    @Override
    public List<Permission> findByItemIdIn(List<String> items) {
        return permissionRepository.findByEntityStateAndItemIdIn(EntityState.ACTIVE, items);
    }

    @Override
    public Optional<Permission> findByItemIdAndTypeAndRoles_Code(String item, PermissionType permissionType, String code) {
        return permissionRepository.findByItemIdAndTypeAndEntityStateAndRoles_Code(item, permissionType, EntityState.ACTIVE, code);
    }

    @Override
    public List<Permission> findByTypeAndRoles_CodeIn(PermissionType permissionType, List<String> codes) {
        return permissionRepository.findByTypeAndEntityStateAndRoles_CodeIn(permissionType, EntityState.ACTIVE, codes);
    }

    @Override
    public List<Permission> findByItemIdAndTypeAndRolesIn(String item, PermissionType permissionType, List<Role> roles) {
        return permissionRepository.findByItemIdAndEntityStateAndTypeAndRolesIn(item, EntityState.ACTIVE , permissionType, roles);
    }

    @Override
    public List<Permission> findByTypeAndRolesIn(PermissionType permissionType, List<Role> roles) {
        return permissionRepository.findByTypeAndEntityStateAndRolesIn(permissionType, EntityState.ACTIVE , roles);
    }
}

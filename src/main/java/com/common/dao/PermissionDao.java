package com.common.dao;

import com.common.entity.Permission;
import com.common.entity.Role;
import com.common.service.PermissionService;
import com.util.enums.PermissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionDao {

    @Autowired
    private PermissionService permissionService;

    public Permission findByTypeAndItemId(PermissionType permissionType, String itemId) {
        return permissionService.findByTypeAndItemId(permissionType, itemId).orElse(null);
    }

    public Permission save(Permission permission) {
        return permissionService.save(permission);
    }

    public List<Permission> findByItemIdIn(List<String> items) {
        return permissionService.findByItemIdIn(items);
    }

    public void realDeleteAll(List<Permission> permissions) {
        permissionService.realDeleteAll(permissions);
    }

    public Permission findByItemIdAndTypeAndRoleCode(String itemId, PermissionType permissionType, String code) {
        return permissionService.findByItemIdAndTypeAndRoles_Code(itemId, permissionType, code).orElse(null);
    }

    public List<Permission> findByTypeAndRolesInCode(PermissionType permissionType, List<String> codes) {
        return permissionService.findByTypeAndRoles_CodeIn(permissionType, codes);
    }

    public List<Permission> findByItemIdAndTypeAndRolesIn(String item , PermissionType permissionType, List<Role> roles) {
        return permissionService.findByItemIdAndTypeAndRolesIn(item,permissionType,roles);
    }
}

package com.common.service;

import com.common.entity.Permission;
import com.common.entity.Role;
import com.util.enums.PermissionType;

import java.util.List;
import java.util.Optional;

public interface PermissionService extends BaseService<Permission> {
    Optional<Permission> findByTypeAndItemId(PermissionType permissionType, String itemId);

    List<Permission> findByItemIdIn(List<String> items);

    Optional<Permission> findByItemIdAndTypeAndRoles_Code(String item , PermissionType permissionType , String code);

    List<Permission> findByTypeAndRoles_CodeIn(PermissionType permissionType, List<String> code);

    List<Permission> findByItemIdAndTypeAndRolesIn(String item , PermissionType permissionType, List<Role> roles);

    List<Permission> findByTypeAndRolesIn(PermissionType permissionType, List<Role> roles);

}

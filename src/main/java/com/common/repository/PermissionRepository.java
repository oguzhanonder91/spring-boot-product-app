package com.common.repository;

import com.common.entity.Permission;
import com.common.entity.Role;
import com.util.enums.EntityState;
import com.util.enums.PermissionType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends BaseRepository<Permission> {
    Optional<Permission> findByTypeAndItemIdAndEntityState(PermissionType permissionType, String itemId, EntityState state);

    List<Permission> findByEntityStateAndItemIdIn(EntityState state, List<String> items);

    Optional<Permission> findByItemIdAndTypeAndEntityStateAndRoles_Code(String item, PermissionType permissionType, EntityState state, String code);

    List<Permission> findByTypeAndEntityStateAndRoles_CodeIn(PermissionType permissionType, EntityState state, List<String> code);

    List<Permission> findByItemIdAndEntityStateAndTypeAndRolesIn(String item, EntityState state, PermissionType permissionType, List<Role> roles);

    List<Permission> findByTypeAndEntityStateAndRolesIn(PermissionType permissionType, EntityState state, List<Role> roles);


}

package com.common.repository;

import com.common.entity.Permission;
import com.util.enums.PermissionType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends BaseRepository<Permission> {
    Optional<Permission> findByTypeAndItemId(PermissionType permissionType, String itemId);

    List<Permission> findByItemIdIn(List<String> items);

    Optional<Permission> findByItemIdAndTypeAndRoles_Code(String item, PermissionType permissionType, String code);

    List<Permission> findByTypeAndRoles_CodeIn(PermissionType permissionType, List<String> code);
}

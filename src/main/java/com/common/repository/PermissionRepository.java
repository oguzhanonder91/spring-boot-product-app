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
}

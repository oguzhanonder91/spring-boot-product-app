package com.common.service;

import com.common.dto.PermissionDto;
import com.common.entity.Permission;
import com.common.entity.Role;
import com.util.enums.PermissionType;

import java.util.List;
import java.util.Optional;

public interface PermissionService extends BaseService<Permission, PermissionDto> {
}

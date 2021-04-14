package com.common.service.impl;

import com.common.dto.PermissionDto;
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
public class PermissionServiceImpl extends BaseServiceImpl<Permission, PermissionDto> implements PermissionService {
}

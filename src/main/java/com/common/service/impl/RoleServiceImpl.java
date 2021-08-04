package com.common.service.impl;

import com.common.dto.RoleDto;
import com.common.entity.Role;
import com.common.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, RoleDto> implements RoleService {
}

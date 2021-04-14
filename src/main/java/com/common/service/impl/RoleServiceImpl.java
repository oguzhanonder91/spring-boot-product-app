package com.common.service.impl;

import com.common.dto.RoleDto;
import com.common.entity.Role;
import com.common.repository.RoleRepository;
import com.common.service.RoleService;
import com.util.enums.EntityState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends BaseServiceImpl<Role, RoleDto> implements RoleService {
}

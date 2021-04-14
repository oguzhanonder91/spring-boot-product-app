package com.common.service.impl;

import com.common.dto.ServiceGroupDto;
import com.common.entity.ServiceGroup;
import com.common.repository.ServiceGroupRepository;
import com.common.service.ServiceGroupService;
import com.util.enums.EntityState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceGroupServiceImpl extends BaseServiceImpl<ServiceGroup, ServiceGroupDto> implements ServiceGroupService {

}

package com.common.service.impl;

import com.common.dto.ServiceDto;
import com.common.repository.ServiceRepository;
import com.common.service.MyService;
import com.util.enums.EntityState;
import com.util.enums.MethodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyServiceImpl extends BaseServiceImpl<com.common.entity.Service, ServiceDto> implements MyService {
}

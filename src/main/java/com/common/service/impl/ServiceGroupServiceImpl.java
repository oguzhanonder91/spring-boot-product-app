package com.common.service.impl;

import com.common.entity.ServiceGroup;
import com.common.repository.ServiceGroupRepository;
import com.common.service.ServiceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceGroupServiceImpl extends BaseServiceImpl<ServiceGroup> implements ServiceGroupService {

    @Autowired
    private ServiceGroupRepository serviceGroupRepository;

    @Override
    public Optional<ServiceGroup> findByPath(String path) {
        return serviceGroupRepository.findByPath(path);
    }

    @Override
    public List<ServiceGroup> findByPathNotIn(List<String> paths) {
        return serviceGroupRepository.findByPathNotIn(paths);
    }

}

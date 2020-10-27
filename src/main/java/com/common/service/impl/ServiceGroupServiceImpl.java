package com.common.service.impl;

import com.common.entity.ServiceGroup;
import com.common.repository.ServiceGroupRepository;
import com.common.service.ServiceGroupService;
import com.util.enums.EntityState;
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
        return serviceGroupRepository.findByPathAndEntityState(path, EntityState.ACTIVE);
    }

    @Override
    public List<ServiceGroup> findByPathNotIn(List<String> paths) {
        return serviceGroupRepository.findByEntityStateAndPathNotIn(EntityState.ACTIVE, paths);
    }

    @Override
    public List<ServiceGroup> findByKeyNotIn(List<String> keys) {
        return serviceGroupRepository.findByEntityStateAndServiceGroupKeyNotIn(EntityState.ACTIVE, keys);
    }

    @Override
    public Optional<ServiceGroup> findByKeyAndPath(String key, String path) {
        return serviceGroupRepository.findByServiceGroupKeyAndPathAndEntityState(key, path, EntityState.ACTIVE);
    }
}

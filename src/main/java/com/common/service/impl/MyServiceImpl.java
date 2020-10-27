package com.common.service.impl;

import com.common.repository.ServiceRepository;
import com.common.service.MyService;
import com.util.enums.EntityState;
import com.util.enums.MethodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MyServiceImpl extends BaseServiceImpl<com.common.entity.Service> implements MyService {
    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public Optional<com.common.entity.Service> findByPathAndMethod(String path, MethodType methodType) {
        return serviceRepository.findByPathAndMethodAndEntityState(path, methodType, EntityState.ACTIVE);
    }

    @Override
    public List<com.common.entity.Service> findByPathNotIn(List<String> paths) {
        return serviceRepository.findByEntityStateAndPathNotIn(EntityState.ACTIVE, paths);
    }

    @Override
    public List<com.common.entity.Service> findByKeyNotIn(List<String> keys) {
        return serviceRepository.findByEntityStateAndServiceKeyNotIn(EntityState.ACTIVE, keys);
    }

    @Override
    public Optional<com.common.entity.Service> findByKeyAndMethod(String key, MethodType methodType) {
        return serviceRepository.findByServiceKeyAndMethodAndEntityState(key, methodType, EntityState.ACTIVE);
    }
}

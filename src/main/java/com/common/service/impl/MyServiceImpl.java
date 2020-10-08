package com.common.service.impl;

import com.common.repository.ServiceRepository;
import com.common.service.MyService;
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
        return serviceRepository.findByPathAndMethod(path, methodType);
    }

    @Override
    public List<com.common.entity.Service> findByPathNotIn(List<String> paths) {
        return serviceRepository.findByPathNotIn(paths);
    }
}

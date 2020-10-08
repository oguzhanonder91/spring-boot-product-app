package com.common.dao;

import com.common.entity.ServiceGroup;
import com.common.service.ServiceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceGroupDao {

    @Autowired
    private ServiceGroupService serviceGroupService;

    public ServiceGroup findByPath(String path) {
        return serviceGroupService.findByPath(path).orElse(null);
    }

    public ServiceGroup save(ServiceGroup serviceGroup) {
        return serviceGroupService.save(serviceGroup);
    }

    public List<ServiceGroup> findByPathNotIn(List<String> paths) {
        return serviceGroupService.findByPathNotIn(paths);
    }

    public void realDeleteAll(List<ServiceGroup> serviceGroups) {
        serviceGroupService.realDeleteAll(serviceGroups);
    }
}

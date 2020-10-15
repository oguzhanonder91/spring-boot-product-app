package com.common.service;

import com.common.entity.ServiceGroup;

import java.util.List;
import java.util.Optional;

public interface ServiceGroupService extends BaseService<ServiceGroup> {
    Optional<ServiceGroup> findByPath(String path);

    List<ServiceGroup> findByPathNotIn(List<String> paths);

    List<ServiceGroup> findByKeyNotIn(List<String> key);

    Optional<ServiceGroup> findByKeyAndPath(String key,String path);
}

package com.common.service;

import com.common.entity.Service;
import com.util.enums.MethodType;

import java.util.List;
import java.util.Optional;

public interface MyService extends BaseService<Service> {
    Optional<Service> findByPathAndMethod(String path, MethodType methodType);

    List<Service> findByPathNotIn(List<String> paths);

}

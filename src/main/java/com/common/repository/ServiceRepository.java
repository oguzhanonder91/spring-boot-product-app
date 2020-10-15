package com.common.repository;

import com.common.entity.Service;
import com.util.enums.MethodType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends BaseRepository<Service> {
    Optional<Service> findByPathAndMethod(String path, MethodType methodType);

    List<Service> findByPathNotIn(List<String> paths);

    List<Service> findByServiceKeyNotIn(List<String> keys);

    Optional<Service> findByServiceKeyAndMethod(String key, MethodType methodType);

}

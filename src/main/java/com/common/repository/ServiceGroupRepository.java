package com.common.repository;

import com.common.entity.ServiceGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceGroupRepository extends BaseRepository<ServiceGroup> {
    Optional<ServiceGroup> findByPath(String path);

    List<ServiceGroup> findByPathNotIn(List<String> paths);
}

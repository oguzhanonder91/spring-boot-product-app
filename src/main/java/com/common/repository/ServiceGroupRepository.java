package com.common.repository;

import com.common.entity.ServiceGroup;
import com.util.enums.EntityState;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceGroupRepository extends BaseRepository<ServiceGroup> {
    Optional<ServiceGroup> findByPathAndEntityState(String path, EntityState state);

    List<ServiceGroup> findByEntityStateAndPathNotIn(EntityState state , List<String> paths);

    List<ServiceGroup> findByEntityStateAndServiceGroupKeyNotIn(EntityState state , List<String> key);

    Optional<ServiceGroup> findByServiceGroupKeyAndPathAndEntityState(String key,String path,EntityState state);
}

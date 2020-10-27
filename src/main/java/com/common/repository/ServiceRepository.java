package com.common.repository;

import com.common.entity.Service;
import com.util.enums.EntityState;
import com.util.enums.MethodType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends BaseRepository<Service> {
    Optional<Service> findByPathAndMethodAndEntityState(String path, MethodType methodType, EntityState state);

    List<Service> findByEntityStateAndPathNotIn(EntityState state, List<String> paths);

    List<Service> findByEntityStateAndServiceKeyNotIn(EntityState state, List<String> keys);

    Optional<Service> findByServiceKeyAndMethodAndEntityState(String key, MethodType methodType, EntityState state);

}

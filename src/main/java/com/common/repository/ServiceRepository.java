package com.common.repository;

import com.common.entity.Service;
import com.util.enums.EntityState;
import com.util.enums.MethodType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends BaseRepository<Service> {
}

package com.common.repository;

import com.common.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<Role> {

    Role findByName(final String name);

    Role findByCode(final String code);

}

package com.common.repository;

import com.common.entity.Menu;
import com.util.enums.EntityState;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends BaseRepository<Menu> {
    List<Menu> getAllByParentNullAndEntityState(EntityState state);

    Optional<Menu> findByCodeAndEntityState(String code, EntityState state);

    List<Menu> findByParentNullAndEntityStateAndIdIn(EntityState state, List<String> ids);
}

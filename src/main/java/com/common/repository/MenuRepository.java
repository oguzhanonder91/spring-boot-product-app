package com.common.repository;

import com.common.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends BaseRepository<Menu> {
    List<Menu> getAllByParentNull();
    Optional<Menu> findByCode(String code);
    List<Menu> findByParentNullAndIdIn(List<String> ids);
}

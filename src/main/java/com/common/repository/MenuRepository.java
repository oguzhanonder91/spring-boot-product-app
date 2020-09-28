package com.common.repository;

import com.common.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends BaseRepository<Menu> {
    List<Menu> getAllByParentNull();
}

package com.common.service;

import com.common.entity.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuService extends BaseService<Menu> {
    List<Menu> getAllByParentNull();

    Optional<Menu> findByCode(String code);

    List<Menu> findByParentNullAndIdIn(List<String> ids);

}

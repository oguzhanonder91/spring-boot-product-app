package com.common.service;

import com.common.entity.Menu;

import java.util.List;

public interface MenuService extends BaseService<Menu> {
    List<Menu> getAllByParentNull();
}

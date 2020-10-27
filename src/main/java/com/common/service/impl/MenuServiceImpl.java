package com.common.service.impl;

import com.common.entity.Menu;
import com.common.repository.MenuRepository;
import com.common.service.MenuService;
import com.util.enums.EntityState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Menu> getAllByParentNull() {
        return menuRepository.getAllByParentNullAndEntityState(EntityState.ACTIVE);
    }

    @Override
    public Optional<Menu> findByCode(String code) {
        return menuRepository.findByCodeAndEntityState(code, EntityState.ACTIVE);
    }

    @Override
    public List<Menu> findByParentNullAndIdIn(List<String> ids) {
        return menuRepository.findByParentNullAndEntityStateAndIdIn(EntityState.ACTIVE, ids);
    }
}

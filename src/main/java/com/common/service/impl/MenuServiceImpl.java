package com.common.service.impl;

import com.common.entity.Menu;
import com.common.repository.MenuRepository;
import com.common.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Menu> getAllByParentNull() {
        return menuRepository.getAllByParentNull();
    }
}

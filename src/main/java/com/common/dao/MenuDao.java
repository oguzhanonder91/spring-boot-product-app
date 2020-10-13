package com.common.dao;

import com.common.entity.Menu;
import com.common.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuDao {

    @Autowired
    private MenuService menuService;

    public List<Menu> saveAll(List<Menu> menus) {
        return menuService.saveAll(menus);
    }

    public Menu saveAndFlush(Menu menu) {
        return menuService.saveAndFlush(menu);
    }

    public Menu save(Menu menu) {
        return menuService.save(menu);
    }

    public List<Menu> findAll() {
        return menuService.findAll();
    }

    public List<Menu> getAllByParentNull() {
        return menuService.getAllByParentNull();
    }

    public Menu findByCode(String code) {
        return menuService.findByCode(code).orElse(null);
    }

    public List<Menu> getMenusOfUser(List<String> ids) {
        List<Menu> menus = menuService.findByParentNullAndIdIn(ids);
        for (Menu menu : menus) {
            if (menu.getChildren() != null) {
                menu.getChildren().removeIf(p -> !ids.contains(p.getId()));
            }
        }
        return menus;
    }
}

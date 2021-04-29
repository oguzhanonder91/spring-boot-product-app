package com.common.dao;

import com.common.entity.Menu;
import com.common.service.MenuService;
import com.common.specification.SearchCriteria;
import com.common.specification.SearchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuDao {

    @Autowired
    private MenuService menuService;

    public List<Menu> saveAll(List<Menu> menus) {
        return menuService.saveForEntity(menus);
    }

    public Menu save(Menu menu) {
        return menuService.saveForEntity(menu);
    }

    public List<Menu> findAll() {
        return menuService.findAllForEntity();
    }

    public List<Menu> getAllByParentNull() {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .and(SearchOperation.IS_NULL, "parent")
                .buildActive();
        return menuService.caboryaFindByParamsForEntity(searchCriteria);
    }

    public Menu findByCode(String code) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .and(SearchOperation.EQUAL, "code",code)
                .buildActive();
        List<Menu> menus =  menuService.caboryaFindByParamsForEntity(searchCriteria);
        return menus.size() > 0 ? menus.get(0) : null;
    }

    public List<Menu> getMenusOfUser(List<String> ids) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .and(SearchOperation.IS_NULL, "parent")
                .and(SearchOperation.IN,"id",ids)
                .buildActive();
        List<Menu> menus = menuService.caboryaFindByParamsForEntity(searchCriteria);
        for (Menu menu : menus) {
            if (menu.getChildren() != null) {
                menu.getChildren().removeIf(p -> !ids.contains(p.getId()));
            }
        }
        return menus;
    }
}

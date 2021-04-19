package com.common.dao;

import com.common.entity.Role;
import com.common.service.RoleService;
import com.common.specification.SearchCriteria;
import com.common.specification.SearchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleDao {

    @Autowired
    private RoleService roleService;

    public Role save(Role role) {
        return roleService.saveForEntity(role);
    }

    public Role findByCode(String code) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "code", code)
                .buildActive();
        List<Role> roles =  roleService.caboryaFindByParamsForEntity(searchCriteria);
        return roles.size() > 0 ? roles.get(0) : null;
    }

    public List<Role> findByCode(List<String> codes) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.IN, "code", codes)
                .buildActive();
        return roleService.caboryaFindByParamsForEntity(searchCriteria);
    }

    public Role findByName(String name) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "name", name)
                .buildActive();
        List<Role> roles =  roleService.caboryaFindByParamsForEntity(searchCriteria);
        return roles.size() > 0 ? roles.get(0) : null;
    }

    public Role update(Role role) {
        return roleService.updateForEntity(role);
    }

    public void softDelete(Role role) {
        roleService.softDeleteForEntity(role);
    }

    public List<Role> findAll() {
        return roleService.findAllForEntity();
    }

    public List<Role> findAllActive() {
        return roleService.findAllActiveForEntity();
    }

}

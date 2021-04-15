package com.common.dao;

import com.common.entity.Permission;
import com.common.entity.Role;
import com.common.service.PermissionService;
import com.common.specification.SearchCriteria;
import com.common.specification.SearchOperation;
import com.util.enums.PermissionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermissionDao {

    @Autowired
    private PermissionService permissionService;

    public Permission findByTypeAndItemId(PermissionType permissionType, String itemId) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "type", permissionType)
                .addFilter(SearchOperation.EQUAL, "itemId", itemId)
                .buildActive();

        List<Permission> permissions = permissionService.caboryaFindByParamsForEntity(searchCriteria);
        return permissions.size() > 0 ? permissions.get(0) : null;
    }

    public Permission save(Permission permission) {
        return permissionService.saveForEntity(permission);
    }

    public List<Permission> findByItemIdIn(List<String> items) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.IN, "itemId", items)
                .buildActive();
        return permissionService.caboryaFindByParamsForEntity(searchCriteria);
    }

    public void realDeleteAll(List<Permission> permissions) {
        permissionService.realDeleteAllForEntity(permissions);
    }

    public Permission findByItemIdAndTypeAndRoleCode(String itemId, PermissionType permissionType, String code) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "itemId", itemId)
                .addFilter(SearchOperation.EQUAL, "type", permissionType)
                .addFilter(SearchOperation.EQUAL, "roles.code", code)
                .buildActive();
        List<Permission> permissions = permissionService.caboryaFindByParamsForEntity(searchCriteria);
        return permissions.size() > 0 ? permissions.get(0) : null;
    }

    public List<Permission> findByTypeAndRolesInCode(PermissionType permissionType, List<String> codes) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "type", permissionType)
                .addFilter(SearchOperation.IN, "roles.code", codes)
                .buildActive();
        return permissionService.caboryaFindByParamsForEntity(searchCriteria);
    }

    public List<Permission> findByItemIdAndTypeAndRolesIn(String item , PermissionType permissionType, List<Role> roles) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "type", permissionType)
                .addFilter(SearchOperation.EQUAL, "itemId", item)
                .addFilter(SearchOperation.IN, "roles.id", roles.stream().map(i->i.getId()).collect(Collectors.toList()))
                .buildActive();
        return permissionService.caboryaFindByParamsForEntity(searchCriteria);
    }

    public List<Permission> findByTypeAndRolesIn(PermissionType permissionType, List<Role> roles) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "type", permissionType)
                .addFilter(SearchOperation.IN, "roles.id", roles.stream().map(i->i.getId()).collect(Collectors.toList()))
                .buildActive();
        return permissionService.caboryaFindByParamsForEntity(searchCriteria);
    }

    public List<Permission> findBy() {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL,"type",PermissionType.SERVICE)
                .showField("itemId")
                .showField("roles.code")
                .showField("roles.entityState")
                .showField("roles.id")
                .buildActive();
        return permissionService.caboryaFindByParams(searchCriteria);
    }
}

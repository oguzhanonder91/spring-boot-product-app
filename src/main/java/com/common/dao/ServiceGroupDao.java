package com.common.dao;

import com.common.entity.ServiceGroup;
import com.common.service.ServiceGroupService;
import com.common.specification.SearchCriteria;
import com.common.specification.SearchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceGroupDao {

    @Autowired
    private ServiceGroupService serviceGroupService;

    public List<ServiceGroup> findByPath(String path) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL,"path",path)
                .buildActive();
        return serviceGroupService.caboryaFindByParamsForEntity(searchCriteria);
    }

    public ServiceGroup save(ServiceGroup serviceGroup) {
        return serviceGroupService.saveForEntity(serviceGroup);
    }

    public List<ServiceGroup> findByPathNotIn(List<String> paths) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.NOT_IN,"path",paths)
                .buildActive();
        return serviceGroupService.caboryaFindByParamsForEntity(searchCriteria);
    }

    public void realDeleteAll(List<ServiceGroup> serviceGroups) {
        serviceGroupService.realDeleteAllForEntity(serviceGroups);
    }

    public List<ServiceGroup> findByKeyNotIn(List<String> keys) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.NOT_IN,"serviceGroupKey",keys)
                .buildActive();
        return serviceGroupService.caboryaFindByParamsForEntity(searchCriteria);
    }

    public ServiceGroup findByKeyAndPath(String key,String path) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL,"serviceGroupKey",key)
                .addFilter(SearchOperation.EQUAL,"path",path)
                .buildActive();
        List<ServiceGroup> serviceGroups = serviceGroupService.caboryaFindByParamsForEntity(searchCriteria);
        return serviceGroups.size() > 0 ? serviceGroups.get(0) : null;
    }

}

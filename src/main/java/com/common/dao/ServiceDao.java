package com.common.dao;

import com.common.entity.Service;
import com.common.service.MyService;
import com.common.specification.SearchCriteria;
import com.common.specification.SearchOperation;
import com.util.enums.MethodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceDao {

    @Autowired
    private MyService myService;

    public Service findByPathAndMethod(String path, MethodType methodType) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "path", path)
                .addFilter(SearchOperation.EQUAL, "method", methodType)
                .buildActive();
        List<Service> services = myService.caboryaFindByParamsForEntity(searchCriteria);
        return services.size() > 0 ? services.get(0) : null;
    }

    public Service save(Service service) {
        return myService.saveForEntity(service);
    }

    public List<Service> findByPathNotIn(List<String> paths) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.NOT_IN, "path", paths)
                .buildActive();
        return myService.caboryaFindByParamsForEntity(searchCriteria);
    }

    public void realDeleteAll(List<Service> services) {
        myService.realDeleteAllForEntity(services);
    }

    public List<Service> findByKeyNotIn(List<String> keys) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.NOT_IN, "serviceKey", keys)
                .buildActive();
        return myService.caboryaFindByParamsForEntity(searchCriteria);
    }

    public Service findByKeyAndMethod(String key, MethodType methodType) {
        SearchCriteria searchCriteria = new SearchCriteria.Builder()
                .addFilter(SearchOperation.EQUAL, "serviceKey", key)
                .addFilter(SearchOperation.EQUAL, "method", methodType)
                .buildActive();
        List<Service> services = myService.caboryaFindByParamsForEntity(searchCriteria);
        return services.size() > 0 ? services.get(0) : null;
    }
}

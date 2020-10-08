package com.common.dao;

import com.common.entity.Service;
import com.common.service.MyService;
import com.util.enums.MethodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServiceDao {

    @Autowired
    private MyService myService;

    public Service findByPathAndMethod(String path, MethodType methodType) {
        return myService.findByPathAndMethod(path, methodType).orElse(null);
    }

    public Service save(Service service) {
        return myService.save(service);
    }

    public List<Service> findByPathNotIn(List<String> paths) {
        return myService.findByPathNotIn(paths);
    }

    public void realDeleteAll(List<Service> services) {
        myService.realDeleteAll(services);
    }
}

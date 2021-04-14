package com.common.dto;

import com.common.entity.ServiceGroup;
import com.util.enums.MethodType;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

/**
 * @author Oğuzhan ÖNDER
 * @date 14.04.2021 - 12:10
 */
public class ServiceDto extends BaseDto {
    private String path;

    private MethodType method;

    private String name;

    private String serviceKey;

    private ServiceGroup serviceGroup;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MethodType getMethod() {
        return method;
    }

    public void setMethod(MethodType method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public ServiceGroup getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(ServiceGroup serviceGroup) {
        this.serviceGroup = serviceGroup;
    }
}

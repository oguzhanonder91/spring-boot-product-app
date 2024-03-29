package com.common.dto;

import com.util.enums.MethodType;

/**
 * @author Oğuzhan ÖNDER
 * @date 14.04.2021 - 12:10
 */
public class ServiceDto extends BaseDto {
    private String path;

    private MethodType method;

    private String name;

    private String serviceKey;

    private ServiceGroupDto serviceGroup;

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

    public ServiceGroupDto getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(ServiceGroupDto serviceGroup) {
        this.serviceGroup = serviceGroup;
    }
}

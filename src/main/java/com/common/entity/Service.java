package com.common.entity;

import com.util.enums.MethodType;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Where(clause = "entity_state=1")
public class Service extends BaseEntity {

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private MethodType method;

    @Column
    private String name;

    @Column(columnDefinition="LONGTEXT")
    private String serviceKey;

    @ManyToOne
    private ServiceGroup serviceGroup;

    public Service(String path, MethodType method, String name, ServiceGroup serviceGroup) {
        this.path = path;
        this.method = method;
        this.name = name;
        this.serviceGroup = serviceGroup;
    }

    public Service(String path, String key, MethodType method, String name, ServiceGroup serviceGroup) {
        this.path = path;
        this.serviceKey = key;
        this.method = method;
        this.name = name;
        this.serviceGroup = serviceGroup;
    }

    public Service() {
    }

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

    public ServiceGroup getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(ServiceGroup serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }
}

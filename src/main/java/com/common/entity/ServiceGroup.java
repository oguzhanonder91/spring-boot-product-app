package com.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ServiceGroup extends BaseEntity {

    @Column(nullable = false)
    private String path;

    @Column
    private String name;

    @Column(columnDefinition="LONGTEXT")
    private String serviceGroupKey;

    public ServiceGroup(String path, String name) {
        this.path = path;
        this.name = name;
    }

    public ServiceGroup(String path, String name,String key) {
        this.path = path;
        this.name = name;
        this.serviceGroupKey = key;
    }

    public ServiceGroup() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceGroupKey() {
        return serviceGroupKey;
    }

    public void setServiceGroupKey(String serviceGroupKey) {
        this.serviceGroupKey = serviceGroupKey;
    }
}

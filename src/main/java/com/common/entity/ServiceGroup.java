package com.common.entity;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Where(clause = "entity_state=1")
public class ServiceGroup extends BaseEntity {

    @Column(nullable = false)
    private String path;

    @Column
    private String name;

    public ServiceGroup(String path, String name) {
        this.path = path;
        this.name = name;
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
}

package com.common.entity;

import com.util.enums.MethodType;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Where(clause = "entity_state=1")
public class Service extends BaseEntity {

    @Column
    private String path;

    @Column
    private MethodType method;

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
}

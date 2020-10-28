package com.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.Collection;

@Entity
public class Role extends BaseEntity {

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private Collection<User> users;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    public Role() {
    }

    public Role(String name, String code) {
        this.name = name;
        this.code = code;
    }
    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

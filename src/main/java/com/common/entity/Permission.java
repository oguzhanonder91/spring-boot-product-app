package com.common.entity;

import com.util.enums.PermissionType;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Where(clause = "entity_state=1")
public class Permission extends BaseEntity{

    @Column
    private PermissionType type;

    @ManyToMany
    private Collection<Role> roles;

    @ManyToMany
    private Collection<User> users;

    public PermissionType getType() {
        return type;
    }

    public void setType(PermissionType type) {
        this.type = type;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }
}

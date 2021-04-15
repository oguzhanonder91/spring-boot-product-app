package com.common.dto;

import com.common.entity.Role;
import com.common.entity.User;
import com.util.enums.PermissionType;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import java.util.Collection;

/**
 * @author Oğuzhan ÖNDER
 * @date 14.04.2021 - 12:08
 */
public class PermissionDto extends BaseDto{
    private PermissionType type;

    private String itemId;

    private Collection<RoleDto> roles;

    private Collection<UserDto> users;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Collection<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleDto> roles) {
        this.roles = roles;
    }

    public Collection<UserDto> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserDto> users) {
        this.users = users;
    }

    public PermissionType getType() {
        return type;
    }

    public void setType(PermissionType type) {
        this.type = type;
    }
}

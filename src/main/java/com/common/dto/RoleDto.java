package com.common.dto;

import com.common.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;

/**
 * @author Oğuzhan ÖNDER
 * @date 14.04.2021 - 12:09
 */
public class RoleDto extends BaseDto{

    @JsonIgnore
    private Collection<UserDto> users;

    private String name;

    private String code;

    public Collection<UserDto> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserDto> users) {
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

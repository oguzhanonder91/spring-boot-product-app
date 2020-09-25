package com.common.entity;


import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Where(clause = "entity_state=1")
public class Menu extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    @Column
    private int orderItem;

    @OneToMany(mappedBy = "parent")
    private List<Menu> children;

    @ManyToOne
    private Menu parent;

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

    public int getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(int orderItem) {
        this.orderItem = orderItem;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }
}

package com.example.entity;

import com.common.entity.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Trade extends BaseEntity {

    @Column(precision = 10, scale = 2)
    private double sale;

    @Column(precision = 10, scale = 2)
    private double purchase;

    @Column
    private long count;

    @Column(precision = 10, scale = 2)
    private double balance;

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public double getPurchase() {
        return purchase;
    }

    public void setPurchase(double purchase) {
        this.purchase = purchase;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

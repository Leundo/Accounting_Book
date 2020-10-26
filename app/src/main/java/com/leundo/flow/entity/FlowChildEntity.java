package com.leundo.flow.entity;

import com.leundo.data.Bill;

/**
 * 子项数据的实体类
 */
public class FlowChildEntity {


    private Bill bill;

    public FlowChildEntity(Bill bill) {
        this.bill = bill;
    }

    public Bill getBill() {
        return bill;
    }
    public void setBill(Bill bill) {
        this.bill = bill;
    }

}


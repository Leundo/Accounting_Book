package com.leundo.flow.entity;

import java.util.ArrayList;

/**
 * 可展开收起的组数据的实体类 它比GroupEntity只是多了一个boolean类型的isExpand，用来表示展开和收起的状态。
 */
public class FlowGroupEntity {

    private String headerTitle;
    private String footer;
    private ArrayList<FlowChildEntity> children;
    private boolean isExpand;
    private double expenditure;
    private double income;

    public void setExpenditure(double expenditure) {
        this.expenditure = expenditure;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpenditure() {
        return expenditure;
    }

    public double getIncome() {
        return income;
    }

    public FlowGroupEntity(String headerTitle, String footer, boolean isExpand,
                           ArrayList<FlowChildEntity> children, double expenditure, double income) {
        this.headerTitle = headerTitle;
        this.footer = footer;
        this.isExpand = isExpand;
        this.children = children;
        this.expenditure = expenditure;
        this.income = income;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public ArrayList<FlowChildEntity> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<FlowChildEntity> children) {
        this.children = children;
    }
}

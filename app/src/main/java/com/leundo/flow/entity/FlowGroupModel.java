package com.leundo.flow.entity;

import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.leundo.flow.adapter.FlowSettingAdapter;
import com.xuexiang.templateproject.MyApp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class FlowGroupModel {
    // Group单例
    public static ArrayList<FlowGroupEntity> flowGroups = getFlowGroupsInstance();

    private static ArrayList<FlowGroupEntity> getFlowGroupsInstance() {
        Calendar calendar = Calendar.getInstance();
        int thisYear = calendar.get(Calendar.YEAR);
        BillDao billDao = new BillDao(MyApp.getContext());

        Calendar start = Calendar.getInstance();
        start.set(thisYear,0,1,0,0,0);
        Calendar end = Calendar.getInstance();
        end.set(thisYear + 1,0,1,0,0,0);


        ArrayList<Bill> bills = billDao.queryBillByTime(start.getTime(), end.getTime());

        return getFlowGroupsFromBillsByYear(bills, thisYear);
    }

    public static int showedYear = Calendar.getInstance().get(Calendar.YEAR);
    public static int showedMonth = Calendar.getInstance().get(Calendar.MONTH);
    public static boolean isShowInYear = true;
    private static int allExpenditure = 0;
    private static int allIncome = 0;

    public enum FilteredAttribute {
        number, firm
    }



    // Example: filter(.number, "父亲"), 执行后flowGroups中只有number为"父亲"的Bill
    public static void filter(FilteredAttribute attribute, String filteredName) {
        if (FlowSettingAdapter.ACCOUNT_ALL.equals(filteredName)) {
            return;
        }

        Iterator<FlowGroupEntity> groupIterator = flowGroups.iterator();
        while (groupIterator.hasNext()) {

            FlowGroupEntity group = groupIterator.next();
            ArrayList<FlowChildEntity> children = group.getChildren();
            Iterator<FlowChildEntity> childrenIterator = children.iterator();

            while (childrenIterator.hasNext()) {
                FlowChildEntity child = childrenIterator.next();
                Bill bill = child.getBill();

                if (attribute == FilteredAttribute.number) {
                    if (bill.getNumber() == null || !bill.getNumber().equals(filteredName)) {

                        // 更新group的income和expenditure
                        String billCurrency = bill.getCurrency();
                        double billAmount = bill.getAmount();
                        try {
                            if(!Bill.EXCHANGE_RATE.containsKey(billCurrency)) {
                                throw new Exception("不存在 " + billCurrency + " 的汇率");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (billAmount > 0) {
                            group.setIncome(group.getIncome() - billAmount * Bill.EXCHANGE_RATE.get(billCurrency));
                        } else {
                            group.setExpenditure(group.getExpenditure() - billAmount * Bill.EXCHANGE_RATE.get(billCurrency));
                        }

                        childrenIterator.remove();
                    }
                } else if (attribute == FilteredAttribute.firm) {
                    if (bill.getFirm() == null || !bill.getFirm().equals(filteredName)) {

                        String billCurrency = bill.getCurrency();
                        double billAmount = bill.getAmount();
                        try {
                            if(!Bill.EXCHANGE_RATE.containsKey(billCurrency)) {
                                throw new Exception("不存在 " + billCurrency + " 的汇率");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (billAmount > 0) {
                            group.setIncome(group.getIncome() - billAmount * Bill.EXCHANGE_RATE.get(billCurrency));
                        } else {
                            group.setExpenditure(group.getExpenditure() - billAmount * Bill.EXCHANGE_RATE.get(billCurrency));
                        }

                        childrenIterator.remove();
                    }
                }
            }

            if (children.size() <= 0) {
                groupIterator.remove();
            }

        }

        // 更新总收入和总支出
        getExpenditureAndIncome();

    }

    // Bill数组转FlowExpandableGroupEntity
    // bills必须按时间降序排列，且不能超过该年
    public static ArrayList<FlowGroupEntity> getFlowGroupsFromBillsByYear(ArrayList<Bill> bills, int year) {

        allExpenditure = 0;
        allIncome = 0;

        // 结果
        ArrayList<FlowGroupEntity> groups = new ArrayList<>();
        ArrayList<FlowChildEntity> children = new ArrayList<>();


        int month = 11;
        double expenditure = 0;
        double income = 0;
        // 每月第一天
        Calendar start = Calendar.getInstance();
        start.set(year,month,1,0,0,0);



        for (Bill bill: bills) {
            // bill的时间比start小，找下一个月
            while(bill.getTime() < start.getTimeInMillis()) {
                // 上一组children非空，加入groups，children重定向到新数组
                if (children.size() != 0) {
                    groups.add(new FlowGroupEntity((month + 1) + "月", null, true, children, expenditure, income));
                    children = new ArrayList<>();
                    // 统计值清零
                    expenditure = 0;
                    income = 0;
                }
                month -= 1;
                try {
                    if (month < 0)
                        throw new Exception("含有小于该年的bill");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                start.set(year,month,1,0,0,0);
            }

            children.add(new FlowChildEntity(bill));
//            // 汇率转换
//            String billCurrency = bill.getCurrency();
//            double billAmount = bill.getAmount();
//            try {
//                if(!Bill.EXCHANGE_RATE.containsKey(billCurrency)) {
//                    throw new Exception("不存在 " + billCurrency + " 的汇率");
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            if (billAmount > 0) {
//                income += billAmount * Bill.EXCHANGE_RATE.get(billCurrency);
//                allIncome += billAmount * Bill.EXCHANGE_RATE.get(billCurrency);
//            } else {
//                expenditure += billAmount * Bill.EXCHANGE_RATE.get(billCurrency);
//                allExpenditure += billAmount * Bill.EXCHANGE_RATE.get(billCurrency);
//            }

            if (bill.getCategory().equals(Bill.CATEGORY_INCOME_TITLE)) {
                income += Math.abs(bill.getCNYAmount());
                allIncome += Math.abs(bill.getCNYAmount());
            } else if (bill.getCategory().equals(Bill.CATEGORY_EXPENDITURE_TITLE)) {
                expenditure -= Math.abs(bill.getCNYAmount());
                allExpenditure -= Math.abs(bill.getCNYAmount());
            }


        }
        // 最后一组在循环内未加入
        if (children.size() != 0) {
            groups.add(new FlowGroupEntity((month + 1) + "月", null, true, children, expenditure, income));
        }

        // 更新总收入和总支出
//        getExpenditureAndIncome();
        // 更新时间和模式
        isShowInYear = true;
        showedYear = year;

        return groups;
    }

    // Bill数组转FlowExpandableGroupEntity
    // bills必须按时间降序排列，且不能超过该月
    public static ArrayList<FlowGroupEntity> getFlowGroupsFromBillsByMonth(ArrayList<Bill> bills, int year, int month) {

        allExpenditure = 0;
        allIncome = 0;

        // 结果
        ArrayList<FlowGroupEntity> groups = new ArrayList<>();
        ArrayList<FlowChildEntity> children = new ArrayList<>();



        double expenditure = 0;
        double income = 0;

        Calendar start = Calendar.getInstance();
        // 该月最后一天
        start.set(year,month+1,0,0,0,0);
        int weekNum = start.get(Calendar.WEEK_OF_MONTH);
        // 计数器，用于计算每周第一天
        int count = 1;
        int offset = start.get(Calendar.DAY_OF_WEEK);
        // 该周第一天
        start.set(year,month+1,1-offset,0,0,0);



        for (Bill bill: bills) {
            // bill的时间比start小，找下一个周(更早的周)
            while(bill.getTime() < start.getTimeInMillis()) {
                // 上一组children非空，加入groups，children重定向到新数组
                if (children.size() != 0) {
                    groups.add(new FlowGroupEntity("第" + weekNum + "周", null, true, children, expenditure, income));
                    children = new ArrayList<>();
                    // 统计值清零
                    expenditure = 0;
                    income = 0;
                }
                weekNum -= 1;
                try {
                    if (weekNum <= 0)
                        throw new Exception("含有小于该周的bill");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                start.set(year,month+1,1-offset-(count*7),0,0,0);
                count += 1;
            }

            children.add(new FlowChildEntity(bill));


            // 原来没有考虑转账, 现在考虑了
//            if (billAmount > 0) {
//                income += billAmount * Bill.EXCHANGE_RATE.get(billCurrency);
//                allIncome += billAmount * Bill.EXCHANGE_RATE.get(billCurrency);
//            } else {
//                expenditure += billAmount * Bill.EXCHANGE_RATE.get(billCurrency);
//                allExpenditure += billAmount * Bill.EXCHANGE_RATE.get(billCurrency);
//            }
            if (bill.getCategory().equals(Bill.CATEGORY_INCOME_TITLE)) {
                income += Math.abs(bill.getCNYAmount());
                allIncome += Math.abs(bill.getCNYAmount());
            } else if (bill.getCategory().equals(Bill.CATEGORY_EXPENDITURE_TITLE)) {
                expenditure -= Math.abs(bill.getCNYAmount());
                allExpenditure -= Math.abs(bill.getCNYAmount());
            }


        }
        // 最后一组在循环内未加入
        if (children.size() != 0) {
            groups.add(new FlowGroupEntity("第" + weekNum + "周", null, true, children, expenditure, income));
        }

        // 更新总收入和总支出
//        getExpenditureAndIncome();
        // 更新时间和模式
        isShowInYear = false;
        showedYear = year;
        showedMonth = month;

        return groups;
    }

    // 更新支出和收入
    public static void getExpenditureAndIncome() {
        allExpenditure = 0;
        allIncome = 0;
        for (FlowGroupEntity group:flowGroups) {
            allExpenditure += group.getExpenditure();
            allIncome += group.getIncome();
        }

    }

    // 格式化输出Expenditure和Income
    public static String getExpenditureString() {
        // 保留两位小数
        DecimalFormat decimalFormatter = new DecimalFormat("0.00");
        return decimalFormatter.format(Math.abs(allExpenditure));
    }

    public static String getIncomeString() {
        // 保留两位小数
        DecimalFormat decimalFormatter = new DecimalFormat("0.00");
        return decimalFormatter.format(Math.abs(allIncome));
    }

    public static String getSurplusString() {
        // 保留两位小数
        DecimalFormat decimalFormatter = new DecimalFormat("0.00");
        return decimalFormatter.format(Math.abs(allIncome) - Math.abs(allExpenditure));
    }
}

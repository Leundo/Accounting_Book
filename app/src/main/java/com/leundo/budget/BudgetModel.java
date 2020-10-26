/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.leundo.budget;

import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.xuexiang.templateproject.MyApp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BudgetModel {
    public static final String NUM_TABLE_BUDGET_TITLE = "预算";
    public static double budget = getBudget();

    private static double getBudget() {
        BillDao billDao = new BillDao(MyApp.getContext());
        if (billDao.isNumExisted(NUM_TABLE_BUDGET_TITLE)) {
            return billDao.queryNum(NUM_TABLE_BUDGET_TITLE);
        } else {
            return 0;
        }

    }

    public static void updateBudget() {
        BillDao billDao = new BillDao(MyApp.getContext());
        if (billDao.isNumExisted(NUM_TABLE_BUDGET_TITLE)) {
            budget = billDao.queryNum(NUM_TABLE_BUDGET_TITLE);
        } else {
            budget = 0;
        }
    }

//    public static String getAvailableBudget() {
//        BillDao billDao = new BillDao(MyApp.getContext());
//        double availableBudget = budget;
//
//        // 每月第一天
//        Calendar start = Calendar.getInstance();
//        start.set(start.get(Calendar.YEAR),start.get(Calendar.MONTH),1,0,0,0);
//        // 下月第一天
//        Calendar end = Calendar.getInstance();
//        end.set(end.get(Calendar.YEAR),end.get(Calendar.MONTH) + 1,1,0,0,0);
//
//        ArrayList<Bill> bills = billDao.queryBillByTime(start.getTime(), end.getTime());
//        for (Bill bill : bills) {
//            if (bill.getCategory().equals(Bill.CATEGORY_EXPENDITURE_TITLE)) {
//                availableBudget -= Math.abs(bill.getCNYAmount());
//            }
//        }
//
//        DecimalFormat decimalFormatter = new DecimalFormat("0.00");
//        return decimalFormatter.format(availableBudget);
//    }

}

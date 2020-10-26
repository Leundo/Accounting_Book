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

package com.loancold.item;

import com.xuexiang.templateproject.R;

import java.text.DecimalFormat;

public class BillDayOverviewItem implements ItemType {

    String time;
    double income;
    double outcome;

    public BillDayOverviewItem(String time, double income, double outcome){
        this.time = time;
        this.income = income;
        this.outcome = outcome;
    }

    @Override
    public int getItemType() {
        return R.layout.item_bill_dayoverview;
    }

    public String getTime() {
        return time;
    }

    public double getIncome() {
        return income;
    }
    public String getIncomeString() {
        return "收入: "+new DecimalFormat("0.00").format(income);
    }

    public double getOutcome() {
        return outcome;
    }
    public String getOutcomeString() {
        return "支出: "+new DecimalFormat("0.00").format(outcome);
    }
}

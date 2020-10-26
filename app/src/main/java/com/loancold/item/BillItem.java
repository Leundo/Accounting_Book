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
import com.leundo.data.Bill;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BillItem extends Bill implements ItemType, Serializable {

    int billTypeImgSourceId;

    public int getBillTypeImgSourceId() {
        return billTypeImgSourceId;
    }

    public BillItem(Bill bill) {
        super(bill.getId(), bill.getAccount(), bill.getCategory(), bill.getType1(),
                bill.getType2(), bill.getAmount(), bill.getTime(), bill.getNumber(),
                bill.getFirm(), bill.getItem(), bill.getRemark(), bill.getCurrency());

        //TODO:Store this as file rather than "switch"
        this.billTypeImgSourceId = R.drawable.ic_money;

        // 2020.10.24 修改, 修改人: leundo
        /*
        switch (getType1()){
            case "餐饮":  this.billTypeImgSourceId =   R.drawable.ic_food;break;
            case "购物":  this.billTypeImgSourceId =   R.drawable.ic_shopping;break;
            case "收入":  this.billTypeImgSourceId =   R.drawable.ic_add_bill;break;
        }
        */
        // 修改后
        if (Bill.typeIcon.containsKey(getType1()))
            billTypeImgSourceId = Bill.typeIcon.get(getType1());
        else {
            billTypeImgSourceId = Bill.typeDefaultIcon;
        }


    }

    @Override
    public int getItemType() {
        return R.layout.item_bill;
    }

    public String getTimeHHMM(){
        Date date = new Date(getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }

    @Override
    public String getRemark() {
        if(super.getRemark() == null)
            return getType1()+"/"+getType2();
        else
            return super.getRemark();
    }

    public String getInOutType(){
        return getAmount()>0? "收入":"支出";
    }

    public String getAmountString(){
        String currency = getCurrency();
        if(currency.equals(Bill.CURRENCY_CNY_NAME))
            return Double.toString(getAmount());
        else
            return Double.toString(getAmount())+ " " +currency;
    }
}

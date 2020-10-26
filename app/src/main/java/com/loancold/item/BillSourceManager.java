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

import com.leundo.data.Bill;
import com.xuexiang.templateproject.R;

public class BillSourceManager {

    // Default: R.drawable.ic_money
    public static int getBillTypeImgSourceId(Bill bill){
        String type1 = bill.getType1();
        //TODO:Store this as file rather than "switch"
        switch (type1){
            case "餐饮":  return  R.drawable.ic_food;
            case "购物":  return  R.drawable.ic_shopping;
            case "收入":  return  R.drawable.ic_add_bill;
        }
        return R.drawable.ic_money;
    }
}

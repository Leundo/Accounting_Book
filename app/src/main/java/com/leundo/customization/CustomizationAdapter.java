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
package com.leundo.customization;

import android.content.Context;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.leundo.data.Bill;
import com.xuexiang.templateproject.R;

public class CustomizationAdapter extends GroupedRecyclerViewAdapter {
/**
 * 已废弃，全部
*/
//    private String[] currency = {Bill.CURRENCY_USD_NAME, Bill.CURRENCY_EUR_NAME, Bill.CURRENCY_JPY_NAME, Bill.CURRENCY_GBP_NAME, Bill.CURRENCY_AUD_NAME, Bill.CURRENCY_CAD_NAME, Bill.CURRENCY_CHF_NAME };
//    private String[] currencyName = {"美元", "欧元", "日元", "英镑", "欧元", "加元", "瑞士法郎"};

    private String[] rowName = {"操作", "属性", "图标", "一级类别", "名称"};
    private String[] rowDefault = {FUNC_ADD_STR, "一级类别", "", "", ""};

    private static String FUNC_ADD_STR = "添加";
    private static String FUNC_DELETE_STR = "删除";

    private static String ATTR_TYPE1_ITEM_NAME = "一级类别";
    private static String ATTR_TYPE2_ITEM_NAME = "二级类别";

    private final static int FUNC_ROW = 0;
    private final static int ATTR_ROW = 1;
    private final static int ICON_ROW = 2;
    private final static int TYPE1_ROW = 3;
    private final static int NAME_ROW = 4;


    public CustomizationAdapter(Context context) {
        super(context);
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return rowName.length;
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return false;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return 0;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.adapter_flow_setting_child;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        switch (childPosition) {
            case ATTR_ROW:
            case FUNC_ROW:
                holder.setText(R.id.flow_setting_child_name, rowName[childPosition]);
                holder.setText(R.id.flow_setting_child_result, rowDefault[childPosition]);
                break;
            case ICON_ROW:
                if (rowDefault[ATTR_ROW].equals(ATTR_TYPE1_ITEM_NAME)) {
                    holder.setText(R.id.flow_setting_child_name, rowName[childPosition]);
                    holder.setText(R.id.flow_setting_child_result, rowDefault[childPosition]);
                }
                break;
            case TYPE1_ROW:
                if (rowDefault[ATTR_ROW].equals(ATTR_TYPE2_ITEM_NAME)) {
                    holder.setText(R.id.flow_setting_child_name, rowName[childPosition]);
                    holder.setText(R.id.flow_setting_child_result, rowDefault[childPosition]);
                }
                break;
            case NAME_ROW:
                if (rowDefault[FUNC_ROW].equals(FUNC_ADD_STR)) {
                    holder.setText(R.id.flow_setting_child_name, rowName[childPosition]);
                    holder.setText(R.id.flow_setting_child_result, rowDefault[childPosition]);
                } else if (rowDefault[FUNC_ROW].equals(FUNC_DELETE_STR)) {
                    holder.setText(R.id.flow_setting_child_name, rowName[childPosition]);
                    holder.setText(R.id.flow_setting_child_result, rowDefault[childPosition]);
                }
                break;

            default:
//                throw new IllegalStateException("Unexpected value: " + childPosition);
        }

    }
}

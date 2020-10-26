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
import com.xuexiang.templateproject.R;

public class PickIconAdapter extends GroupedRecyclerViewAdapter {
//    private String[] currency = {Bill.CURRENCY_USD_NAME, Bill.CURRENCY_EUR_NAME, Bill.CURRENCY_JPY_NAME, Bill.CURRENCY_GBP_NAME, Bill.CURRENCY_AUD_NAME, Bill.CURRENCY_CAD_NAME, Bill.CURRENCY_CHF_NAME };
//    private String[] currencyName = {"美元", "欧元", "日元", "英镑", "欧元", "加元", "瑞士法郎"};


    public PickIconAdapter(Context context) {
        super(context);
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return PickIconModel.iconResource.length;
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
        return R.layout.adapter_pickicon_item;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
//        holder.setText(R.id.flow_setting_child_name, currencyName[childPosition]);
//        holder.setText(R.id.flow_setting_child_result, Bill.EXCHANGE_RATE.get(currency[childPosition]) + "");
//        holder.setText(R.id.pickicon_adapter_image, "").setImageResource(R.id.pickicon_adapter_image, R.drawable.ic_food);
        holder.setImageResource(R.id.pickicon_adapter_image, PickIconModel.iconResource[childPosition]);
    }

}

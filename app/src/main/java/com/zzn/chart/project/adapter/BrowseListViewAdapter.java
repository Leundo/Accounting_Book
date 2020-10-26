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

package com.zzn.chart.project.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leundo.data.Bill;
import com.xuexiang.templateproject.R;
import com.zzn.chart.project.data.BrowseGroupEntity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BrowseListViewAdapter extends BaseExpandableListAdapter {

    private ArrayList<BrowseGroupEntity> mBill;

    public BrowseListViewAdapter(ArrayList<BrowseGroupEntity> BillList) {
        mBill = BillList;
    }

    //返回一级列表的个数
    @Override
    public int getGroupCount() {
        return mBill.size();
    }

    //返回每个二级列表的个数
    @Override
    public int getChildrenCount(int groupPosition) { //参数groupPosition表示第几个一级列表
        Log.d("smyhvae", "-->" + groupPosition);
        return mBill.get(groupPosition).size();
    }

    //返回一级列表的单个item（返回的是对象）
    @Override
    public Object getGroup(int groupPosition) {
        return mBill.get(groupPosition);
    }

    //返回二级列表中的单个item（返回的是对象）
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mBill.get(groupPosition).getChildren(childPosition);  //不要误写成groups[groupPosition][childPosition]
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //每个item的id是否是固定？一般为true
    @Override
    public boolean hasStableIds() {
        return true;
    }

    //【重要】填充一级列表
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
//            convertView = getLayoutInflater().inflate(R.layout.item_group, null);
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_dayoverview,parent,false);
        } else {

        }
        TextView tv_group_date = (TextView) convertView.findViewById(R.id.tv_dayTime);
        TextView tv_moneyIn = (TextView) convertView.findViewById(R.id.tv_moneyIn);
        TextView tv_moneyOut = (TextView) convertView.findViewById(R.id.tv_moneyOut);
        TextView tv_moneyDiff = (TextView) convertView.findViewById(R.id.tv_moneyDiff);

        //TextView tv_group_weekday = (TextView) convertView.findViewById(R.id.browse_header_title_weekday);
        tv_group_date.setText(mBill.get(groupPosition).getHeaderTitle());
        tv_moneyIn.setText("");
        tv_moneyOut.setText("");
        tv_moneyDiff.setText("");

        return convertView;
    }

    //【重要】填充二级列表
    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            //convertView = getLayoutInflater().inflate(R.layout.item_child, null);
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill,parent,false);
        }

        Bill bill = mBill.get(groupPosition).getChildren(childPosition);
        TextView tv_browse_child_type = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tv_browse_child_time = (TextView) convertView.findViewById(R.id.tv_time);
        TextView tv_browse_child_amount = (TextView) convertView.findViewById(R.id.tv_money);

        ImageView iv_bill = (ImageView) convertView.findViewById(R.id.iv_bill);

        if (Bill.typeIcon.containsKey(bill.getType1())) {
            iv_bill.setImageResource(Bill.typeIcon.get(bill.getType1()));
        } else {
            iv_bill.setImageResource(Bill.typeIcon.get("无"));
        }

        // 保留两位小数
        DecimalFormat decimalFormatter = new DecimalFormat("0.00");
        // 忽略CNY
        if(bill.getCurrency().equals(Bill.CURRENCY_CNY_NAME)) {
            tv_browse_child_amount.setText(decimalFormatter.format(bill.getAmount()));
        } else {
            tv_browse_child_amount.setText(decimalFormatter.format(bill.getAmount()) + " " + bill.getCurrency());
        }

        tv_browse_child_type.setText(bill.getType1() + bill.getType2());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        tv_browse_child_time.setText(""+formatter.format(bill.getTime()));
        return convertView;
    }

    //二级列表中的item是否能够被选中？可以改为true
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

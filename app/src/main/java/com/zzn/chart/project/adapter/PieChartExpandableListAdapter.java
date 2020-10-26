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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.xuexiang.templateproject.R;


public class PieChartExpandableListAdapter extends BaseExpandableListAdapter {//此版本没用

    private String[] groups;
    private String[][] childitems;

    public PieChartExpandableListAdapter(String[] datagroups,String[][] datachilditems) {
        groups = datagroups;
        childitems = datachilditems;
    }

    //返回一级列表的个数
    @Override
    public int getGroupCount() {
        return groups.length;
    }

    //返回每个二级列表的个数
    @Override
    public int getChildrenCount(int groupPosition) { //参数groupPosition表示第几个一级列表
        Log.d("smyhvae", "-->" + groupPosition);
        return childitems[groupPosition].length;
    }

    //返回一级列表的单个item（返回的是对象）
    @Override
    public Object getGroup(int groupPosition) {
        return groups[groupPosition];
    }

    //返回二级列表中的单个item（返回的是对象）
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childitems[groupPosition][childPosition];  //不要误写成groups[groupPosition][childPosition]
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group,parent,false);
        } else {

        }
        TextView tv_group = (TextView) convertView.findViewById(R.id.tv_group);
        tv_group.setText(groups[groupPosition]);
        return convertView;
    }

    //【重要】填充二级列表
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            //convertView = getLayoutInflater().inflate(R.layout.item_child, null);
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child,parent,false);
        }

        TextView tv_child = (TextView) convertView.findViewById(R.id.tv_child);

        //iv_child.setImageResource(resId);
        tv_child.setText(childitems[groupPosition][childPosition]);

        return convertView;
    }

    //二级列表中的item是否能够被选中？可以改为true
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

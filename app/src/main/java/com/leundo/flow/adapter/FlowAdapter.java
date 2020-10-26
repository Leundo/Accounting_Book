package com.leundo.flow.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.ImageView;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.leundo.data.Bill;
import com.leundo.flow.entity.FlowChildEntity;
import com.leundo.flow.entity.FlowGroupEntity;
import com.xuexiang.templateproject.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FlowAdapter extends GroupedRecyclerViewAdapter {
    // 数据组织器
    private ArrayList<FlowGroupEntity> mGroups;


    public FlowAdapter(Context context, ArrayList<FlowGroupEntity> groups) {
        super(context);
        mGroups = groups;
    }



    @Override
    public int getGroupCount() {
        return mGroups == null ? 0 : mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //如果当前组收起，就直接返回0，否则才返回子项数。这是实现列表展开和收起的关键。
        if (!isExpand(groupPosition)) {
            return 0;
        }
        ArrayList<FlowChildEntity> children = mGroups.get(groupPosition).getChildren();
        return children == null ? 0 : children.size();
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.item_bill_dayoverview;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.item_bill;
    }

    // 绑定头部布局数据
    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        FlowGroupEntity entity = mGroups.get(groupPosition);
        holder.setText(R.id.tv_dayTime, entity.getHeaderTitle());

        // 保留两位小数
        DecimalFormat decimalformatter = new DecimalFormat("0.00");
        holder.setText(R.id.tv_moneyOut, "支出：" + decimalformatter.format(Math.abs(entity.getExpenditure())));
        holder.setText(R.id.tv_moneyIn, "收入："+ decimalformatter.format(entity.getIncome()));
        holder.setText(R.id.tv_moneyDiff, "结余：" + decimalformatter.format(entity.getIncome() + entity.getExpenditure()));

//        ImageView ivState = holder.get(R.id.iv_state);
//        if(entity.isExpand()){
//            ivState.setRotation(90);
//        } else {
//            ivState.setRotation(0);
//        }
    }

    // 绑定尾部布局数据
    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    // 绑定子项布局数据
    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        FlowChildEntity entity = mGroups.get(groupPosition).getChildren().get(childPosition);
        Bill bill = entity.getBill();

        // 保留两位小数
        DecimalFormat decimalFormatter = new DecimalFormat("0.00");
        // 忽略CNY
        if(bill.getCurrency().equals(Bill.CURRENCY_CNY_NAME)) {
            holder.setText(R.id.tv_money, decimalFormatter.format(bill.getAmount()));
        } else {
            holder.setText(R.id.tv_money, decimalFormatter.format(bill.getAmount()) + " " + bill.getCurrency());
        }

        holder.setText(R.id.tv_name, entity.getBill().getType1() + bill.getType2());

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd日 EEEE HH:mm");
        holder.setText(R.id.tv_time, formatter.format(bill.getTime()));

        String type1Name = bill.getType1();
        if (Bill.typeIcon.containsKey(type1Name)) {
            holder.setImageResource(R.id.iv_bill, Bill.typeIcon.get(type1Name));
        } else {
            holder.setImageResource(R.id.iv_bill, Bill.typeIcon.get("无"));
        }

    }

    /**
     * 判断当前组是否展开
     *
     * @param groupPosition
     * @return
     */
    public boolean isExpand(int groupPosition) {
        FlowGroupEntity entity = mGroups.get(groupPosition);
        return entity.isExpand();
    }

    /**
     * 展开一个组
     *
     * @param groupPosition
     */
    public void expandGroup(int groupPosition) {
        expandGroup(groupPosition, false);
    }

    /**
     * 展开一个组
     *
     * @param groupPosition
     * @param animate
     */
    public void expandGroup(int groupPosition, boolean animate) {
        FlowGroupEntity entity = mGroups.get(groupPosition);
        entity.setExpand(true);
        if (animate) {
            notifyChildrenInserted(groupPosition);
        } else {
            notifyDataChanged();
        }
    }

    /**
     * 收起一个组
     *
     * @param groupPosition
     */
    public void collapseGroup(int groupPosition) {
        collapseGroup(groupPosition, false);
    }

    /**
     * 收起一个组
     *
     * @param groupPosition
     * @param animate
     */
    public void collapseGroup(int groupPosition, boolean animate) {
        FlowGroupEntity entity = mGroups.get(groupPosition);
        entity.setExpand(false);
        if (animate) {
            notifyChildrenRemoved(groupPosition);
        } else {
            notifyDataChanged();
        }
    }
}



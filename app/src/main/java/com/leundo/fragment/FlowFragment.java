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

package com.leundo.fragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.leundo.flow.activity.FlowSettingActivity;
import com.leundo.flow.adapter.FlowAdapter;
import com.leundo.flow.entity.FlowGroupModel;
import com.loancold.activity.AddBillProActivity;
import com.xuexiang.templateproject.MyApp;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.xui.widget.actionbar.TitleBar;

public class FlowFragment extends BaseFragment {
    private static final String TAG = "FlowFragment";


    //    @BindView(R.id.flow_activity_title)
    private TextView flowActivityTitle;

    //    @BindView(R.id.rv_list)
    private RecyclerView rvList;

    //    @BindView(R.id.all_expenditure_title)
    private TextView allExpenditureTitle;

    //    @BindView(R.id.all_income_title)
    private TextView allIncomeTitle;

    private TextView allSurplusTitle;

    private Context context;

    private FlowAdapter adapter;

    /**
     * @return 返回为 null意为不需要导航栏
     */
    @Override
    protected TitleBar initTitle() {
        return null;
    }

    /**
     * 布局的资源id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_flow;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
//        addTestData();
        findView();
        initOverview();
        initRecyclerView();
    }

    public FlowFragment(Context aContext) {
        context = aContext;
    }

    private void findView() {
        flowActivityTitle = findViewById(R.id.flow_activity_title);
        rvList = findViewById(R.id.rv_list);
        allExpenditureTitle = findViewById(R.id.all_expenditure_title);
        allIncomeTitle = findViewById(R.id.all_income_title);
        allSurplusTitle = findViewById(R.id.all_surplus_title);
    }

    private void initRecyclerView() {
        // 设置RecyclerView
        rvList.setLayoutManager(new LinearLayoutManager(context));

        // 从FlowGroupModel的单例获取数据
        adapter = new FlowAdapter(context, FlowGroupModel.flowGroups);

        BillDao billDao = new BillDao(context);

        // headed的点击事件
        adapter.setOnHeaderClickListener(new GroupedRecyclerViewAdapter.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                      int groupPosition) {
//                Toast.makeText(ExpandableActivity.this, "组头：groupPosition = " + groupPosition,
//                        Toast.LENGTH_LONG).show();
                FlowAdapter expandableAdapter = (FlowAdapter) adapter;
                if (expandableAdapter.isExpand(groupPosition)) {
                    expandableAdapter.collapseGroup(groupPosition, false);
                } else {
                    expandableAdapter.expandGroup(groupPosition, false);
                }
            }
        });
        adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                     int groupPosition, int childPosition) {
                long id = FlowGroupModel.flowGroups.get(groupPosition).getChildren().get(childPosition).getBill().getId();
                Intent intent = new Intent(context, AddBillProActivity.class);
                intent.putExtra("id",id);
                startActivityForResult(intent, 1);

            }
        });
        rvList.setAdapter(adapter);
    }

    private void initOverview() {
        // 设置标题
        if (FlowGroupModel.isShowInYear) {
            flowActivityTitle.setText(FlowGroupModel.showedYear + "年");
        } else {
            flowActivityTitle.setText(FlowGroupModel.showedYear + "年" + FlowGroupModel.showedMonth + "月");
        }

        // 计算总收入和总支出
        FlowGroupModel.getExpenditureAndIncome();
        allExpenditureTitle.setText("" + FlowGroupModel.getExpenditureString());
        allIncomeTitle.setText("" + FlowGroupModel.getIncomeString());
        allSurplusTitle.setText("" + FlowGroupModel.getSurplusString());


//        // 设置设置按钮
//        Button setting = (Button) findViewById(R.id.flow_list_setting);
//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), FlowSettingActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    private void addTestData() {
        BillDao billDao = new BillDao(context);
        for (Bill bill: Bill.getTestData()) {
            billDao.insertBill(bill);
        }
        billDao.insertAccount("支付宝");
        billDao.insertAccount("现金");
        billDao.insertAccount("微信支付");
        billDao.insertNumber("我");
        billDao.insertNumber("父亲");
        billDao.insertNumber("母亲");
        billDao.insertNumber("妻子");
        billDao.insertFirm("饭堂");
        billDao.insertFirm("麦当劳");
        billDao.insertFirm("益田");

        billDao.insertType("餐饮");
        billDao.insertType("餐饮", "早餐");
        billDao.insertType("餐饮", "午饭");
        billDao.insertType("收入");
        billDao.insertType("收入", "工资");
        billDao.insertType("医疗");
        billDao.insertType("医疗", "手术");
        billDao.insertType("购物");
        billDao.insertType("购物", "日用品");
        billDao.insertType("娱乐");
        billDao.insertType("娱乐", "游戏厅");
    }

    public void refresh() {
        if (adapter != null) {
            adapter.notifyDataChanged();
        }


        // 设置标题
        if (flowActivityTitle != null) {
            if (FlowGroupModel.isShowInYear) {
                flowActivityTitle.setText(FlowGroupModel.showedYear + "年");
            } else {
                flowActivityTitle.setText(FlowGroupModel.showedYear + "年" + (FlowGroupModel.showedMonth + 1) + "月");
            }
        }
        if (allExpenditureTitle != null && allIncomeTitle != null && allSurplusTitle != null) {
            // 计算总收入和总支出
            FlowGroupModel.getExpenditureAndIncome();
            allExpenditureTitle.setText("" + FlowGroupModel.getExpenditureString());
            allIncomeTitle.setText("" + FlowGroupModel.getIncomeString());
            allSurplusTitle.setText("" + FlowGroupModel.getSurplusString());
        }
    }

}

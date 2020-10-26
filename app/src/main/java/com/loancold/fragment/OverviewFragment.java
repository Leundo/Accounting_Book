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

package com.loancold.fragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leundo.budget.BudgetModel;
import com.loancold.activity.AddBillProActivity;
import com.xuexiang.templateproject.MyApp;
import com.xuexiang.templateproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.loancold.activity.AddBillActivity;
import com.loancold.adapter.BillAdapter;
import com.loancold.item.BillDayOverviewItem;
import com.loancold.item.BillItem;
import com.loancold.item.ItemType;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OverviewFragment extends BaseFragment {

    ///////////////STATIC///////////////
    private static final String TAG = "OverviewFragment";
    public static final int BILL_TYPE = R.layout.item_bill;
    public static final int BILLDAYOVERVIEW_TYPE = R.layout.item_bill_dayoverview;

    private Context context;
    private MyApp myApp;
    private BillDao billDao;
    private List<ItemType> billList = new ArrayList<>();
    private int lastClickedBillPos;

    private RecyclerView recyclerView;
    BillAdapter billAdapter;

    TextView tv_DayPayValue;
    TextView tv_MonthPayValue;
    TextView tv_MonthMoneyLeftValue;
    TextView tv_MonthEarnValue;

    public OverviewFragment(Context aContext) {
        context = aContext;
    }
    @Override
    protected TitleBar initTitle() {
        return null;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_overview;
    }

    @Override
    protected void initViews() {
        //TODO:recyclerview的刷新，总览数据的设定
        billDao = new BillDao(context);
//        addTestData();
        //函数顺序之间有先后关系！
        getRecentBill();
        initOverview();
        initViewSetting();
    }

    private void addTestData() {
        Date today = Calendar.getInstance().getTime();
        today.setHours(18);
        today.setMinutes(0);
        today.setSeconds(0);
        for (Bill bill: Bill.getTestData()) {
            bill.setTime(today.getTime());
            billDao.insertBill(bill);
            today.setTime(today.getTime()-6*60*60*1000);
        }
        billDao.insertType("餐饮");
        billDao.insertType("餐饮","商场就餐");
        billDao.insertType("餐饮","外卖");
        billDao.insertType("餐饮","食堂");
        billDao.insertType("购物");
        billDao.insertType("购物","服饰");
        billDao.insertType("购物","零食");
        billDao.insertType("购物","电器");
        billDao.insertType("日用");
        billDao.insertType("日用","洗浴");
        billDao.insertType("日用","日常护理");
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
    }

    private void initOverview(){
        tv_MonthPayValue = findViewById(R.id.tv_MonthPayValue);
        tv_DayPayValue = findViewById(R.id.tv_DayPayValue);
        tv_MonthMoneyLeftValue = findViewById(R.id.tv_MonthMoneyLeftValue);
        tv_MonthEarnValue = findViewById(R.id.tv_MonthEarnValue);

        BillDayOverviewItem todayOverview = (BillDayOverviewItem)billList.get(0);
        tv_DayPayValue.setText(new DecimalFormat("0.00").format(todayOverview.getOutcome()));
        //count month
        double income =0;
        double outcome =0;
        Calendar now = Calendar.getInstance();
        Date today = now.getTime();
        now.set(now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                1,
                0,
                0,
                0
        );
        Log.e(TAG,now.toString());
        Date begin_of_month = now.getTime();

        /**
         * leundo
         * 修改income和outcome计算方法
         */
//        for(Bill bill : billDao.queryBillByTime(begin_of_month,today)){
//            double money = bill.getAmount() * Bill.EXCHANGE_RATE.get(bill.getCurrency());
//            if(money > 0)
//                income += money;
//            else
//                outcome -= money;
//        }

        for(Bill bill : billDao.queryBillByTime(begin_of_month,today)){
            // 转账不计入收入或支出
            if (bill.getCategory().equals(Bill.CATEGORY_EXPENDITURE_TITLE)) {
                outcome -= Math.abs(bill.getCNYAmount());
            } else if (bill.getCategory().equals(Bill.CATEGORY_INCOME_TITLE)) {
                income += Math.abs(bill.getCNYAmount());
            }

        }

        tv_MonthEarnValue.setText(new DecimalFormat("0.00").format(income));
        tv_MonthPayValue.setText(new DecimalFormat("0.00").format(outcome));
        myApp = (MyApp)getActivity().getApplication();
        /**
         * leundo
         * 预算来源修改
         */
//        double leftMoney = myApp.getBudget() - outcome;
        double leftMoney = BudgetModel.budget - Math.abs(outcome);
        tv_MonthMoneyLeftValue.setText(new DecimalFormat("0.00").format(leftMoney));
        if(leftMoney<0)
            tv_MonthMoneyLeftValue.setTextColor(getResources().getColor(R.color.Morandi_red));
        else
            tv_MonthMoneyLeftValue.setTextColor(getResources().getColor(R.color.Morandi_white));


    }

    private void getRecentBill(){
        //TODO: Optimize the code for better understanding and performance
        billList = new ArrayList<>();
        Date today = Calendar.getInstance().getTime();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        Date today_start = today;
        Date today_end = new Date(today.getTime() + 24*60*60*1000-1);
        Date yesterday_start = new Date(today_start.getTime()-24*60*60*1000);
        Date yesterday_end = new Date(today_end.getTime()-24*60*60*1000);
        Date dayBefYest_start = new Date(yesterday_start.getTime()-24*60*60*1000);
        Date dayBefYest_end = new Date(yesterday_end.getTime()-24*60*60*1000);

        getBillListByDay(today_start, today_end, "今日：");
        getBillListByDay(yesterday_start, yesterday_end, "昨日：");
        getBillListByDay(dayBefYest_start, dayBefYest_end, "前日：");
    }

    private void getBillListByDay(Date start, Date end, String day_s){
        //TODAY
        double income =0;
        double outcome =0;
        List<BillItem> tmpBillList = new ArrayList<BillItem>();
        for(Bill bill : billDao.queryBillByTime(start,end)){
            double money = bill.getAmount() * Bill.EXCHANGE_RATE.get(bill.getCurrency());
            if(bill.getAmount() > 0)
                income += money;
            else
                outcome -= money;
            tmpBillList.add(new BillItem(bill));
        }
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        String s_today = day_s+ format.format(start);
        BillDayOverviewItem billDayOverviewItem = new BillDayOverviewItem(s_today, income, outcome);
        billList.add(billDayOverviewItem);
        billList.addAll(tmpBillList);
    }

    private void initViewSetting(){
        FloatingActionButton floatingActionButton = findViewById(R.id.AddBill);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddBillProActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        recyclerView = findViewById(R.id.rv_overview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        billAdapter = new BillAdapter(billList);
        billAdapter.setOnItemClick(new BillAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, int position, BillItem bill) {
                Intent intent = new Intent(context, AddBillProActivity.class);
                long id = bill.getId(); // 主键
//                String account = bill.getAccount(); // 账户
//                String category = bill.getCategory(); // 支出，收入，转帐
//                String type1 = bill.getType1(); // 一级类别
//                String type2 = bill.getType2(); // 二级类别
//                double amount = bill.getAmount(); // 金额
//                long time = bill.getTime(); // 时间戳，距离1970的微秒数
//                String number = bill.getNumber(); // 成员
//                String firm = bill.getFirm(); // 商家
//                String item = bill.getItem(); // 项目
//                String remark = bill.getRemark(); // 备注
//                String currency = bill.getCurrency(); // 币种
                lastClickedBillPos = position;
                intent.putExtra("id",id);
                startActivityForResult(intent, 1);
            }
        });
        recyclerView.setAdapter(billAdapter);

    }

    //当“添加账单”活动结束后，在主活动复苏后调用此方法以刷新账单总览
    public void refreshBill(){
        //TODO:野蛮的直接刷新势必体验不好
        if(billAdapter != null){
            getRecentBill();
            initOverview();
            billAdapter.setBillList(billList);
            billAdapter.notifyDataSetChanged();
        }
    }
}

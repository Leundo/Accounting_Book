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

package com.zzn.chart.project.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.picker.wheelview.listener.OnItemSelectedListener;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.zzn.chart.project.adapter.PieChartExpandableListAdapter;
import com.zzn.chart.project.adapter.PieDataListAdapter;
import com.zzn.chart.project.commparator.SortByValue;
import com.zzn.chart.project.data.PieListData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

public class ZznPieFragment extends BaseFragment {
    // MainActivity的context
    Context context;

    private PieChart pieChart;

    SuperTextView timeTextView;

    private Spinner typeSpinner;
    //    Intent intent=getIntent();
    Calendar begin = Calendar.getInstance();
    Calendar end = Calendar.getInstance();
    BillDao billDao;
    ArrayList<Integer> colors = new ArrayList<Integer>();
    ArrayList<Bill> showedBills;
    ArrayList<PieEntry> Values = new ArrayList<>(); //每个饼块的实际数据
    PieDataSet pieDataSet ;
    PieData pieData ;
    ArrayAdapter spinnerAdapter;
    double sumForProportion = 0;
    String mode = "0";
    RecyclerView pieDataListRecyclerView;
    ArrayList<PieListData> pieList = new ArrayList<>();
    ArrayList<String> intentData = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;


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
        return R.layout.fragment_pie_chart;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        findViewAndInitBillDao();
        getDataFromBillDao();
        initExpandableList();
        initPieData();
        initPieDataList();
        initButton();
    }


    // 初始化
    public ZznPieFragment(Context aContext) {
        context = aContext;
    }

    void findViewAndInitBillDao() {
        billDao = new BillDao(context);
        pieDataListRecyclerView = findViewById(R.id.pie_data_list);
        typeSpinner = findViewById(R.id.type_spinner);
        pieChart = findViewById(R.id.pieChart);

        timeTextView = findViewById(R.id.time_textview);
    }

    void getDataFromBillDao() {
        // 该月第一天和最后一天
        begin.set(begin.get(Calendar.YEAR),begin.get(Calendar.MONTH),1,0,0,0);//需要更改
        end.set(end.get(Calendar.YEAR),end.get(Calendar.MONTH)+1,0,0,0,0);

        showedBills = billDao.queryBillByTime(begin.getTime(),end.getTime());
    }

    void initExpandableList() {
        spinnerAdapter = ArrayAdapter.createFromResource(context, R.array.pie_chart_type,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinnerAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mode = ""+position;
                upDate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    void initPieData() {
        while (showedBills.size()>0){
            ArrayList<Bill> anotherBills = new ArrayList<>(showedBills);
            Bill.filter(anotherBills,Bill.FilteredAttribute.type1,anotherBills.get(0).getType1());
            Iterator<Bill> iterator = showedBills.iterator();
            while(iterator.hasNext()){
                Bill bill = iterator.next();
                if(bill.getType1().equals(anotherBills.get(0).getType1())){
                    iterator.remove();
                }
            }
            double sum = 0;
            for(Bill i: anotherBills){
                if(i.getCNYAmount()>0){
                    sum = sum+i.getCNYAmount();
                }
            }
            if(sum!=0){
                Values.add(new PieEntry((float) sum,anotherBills.get(0).getType1()));
                sumForProportion =sumForProportion+sum;
            }
        }


        for (int i = 0; i < Values.size(); i++) {//list为数据列表
            Random random = new Random();
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            colors.add(Color.rgb(r,g,b));
        }


        Collections.sort(Values,new SortByValue());
        for (int i = 0; i < Values.size(); i++) {//list为数据列表
            PieListData pieListData = new PieListData(colors.get(i),Values.get(i).getLabel(),Values.get(i).getValue(),sumForProportion);
            pieList.add(pieListData);
        }

        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(45f);
        pieChart.setTransparentCircleRadius(48f);// 半透明圈
        //pieChart.setDrawCenterText(true);//饼状图中间可以添加文字
        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        pieChart.setUsePercentValues(true);//设置显示成比例
        pieChart.setDrawSliceText(false);//饼图上不显示文字
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
        pieChart.setCenterText("总计：\n"+sumForProportion);
        Legend mLegend = pieChart.getLegend();  //设置比例图
        /*mLegend.setFormSize(12f);//比例块字体大小
        mLegend.setXEntrySpace(8f);//设置距离饼图的距离，防止与饼图重合
        mLegend.setYEntrySpace(8f);
        mLegend.setWordWrapEnabled(true);
        mLegend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        mLegend.setForm(Legend.LegendForm.SQUARE);*/
        mLegend.setEnabled(false);
        pieDataSet = new PieDataSet(Values,"");
        pieDataSet.setSliceSpace(4f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setColors(colors);
        pieDataSet.setValueLineColor(Color.LTGRAY);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueFormatter(new PercentFormatter());
        pieDataSet.setValueTextSize(12f);
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.invalidate();

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight h) {
                PieEntry selectedEntry = (PieEntry) entry;
                if (entry == null)return;
                for(PieEntry i:Values){
                    if(selectedEntry.getLabel().equals(i.getLabel())){
                        pieChart.setCenterText(i.getLabel()+":\n"+i.getValue());
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    void initPieDataList(){
        intentData.add(""+begin.get(Calendar.YEAR));
        intentData.add(""+begin.get(Calendar.MONTH));
        intentData.add(""+begin.get(Calendar.DAY_OF_MONTH));
        intentData.add(""+end.get(Calendar.YEAR));
        intentData.add(""+end.get(Calendar.MONTH));
        intentData.add(""+end.get(Calendar.DAY_OF_MONTH));
        intentData.add(mode);
        linearLayoutManager = new LinearLayoutManager(context);
        pieDataListRecyclerView.setLayoutManager(linearLayoutManager);
        PieDataListAdapter pieDataListAdapter = new PieDataListAdapter(pieList,intentData,context,mode);
        pieDataListRecyclerView.setAdapter(pieDataListAdapter);
    }

    void initButton() {
        timeTextView.setLeftTopString("开始时间");
        timeTextView.setRightTopString("结束时间");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
//
        timeTextView.setLeftString(formatter.format(begin.getTime()));
        timeTextView.setRightString(formatter.format(end.getTime()));

        timeTextView.setLeftTopTvClickListener(textView ->  {
            Calendar calendar=Calendar.getInstance();
            new DatePickerDialog( context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String text = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                    begin.set(year,month,dayOfMonth);
                    timeTextView.setLeftString(text);
                    upDate();
                }
            }
                    ,calendar.get(Calendar.YEAR)
                    ,calendar.get(Calendar.MONTH)
                    ,calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        timeTextView.setLeftTvClickListener(textView ->  {
            Calendar calendar=Calendar.getInstance();
            new DatePickerDialog( context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String text = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                    begin.set(year,month,dayOfMonth);
                    timeTextView.setLeftString(text);
                    upDate();
                }
            }
                    ,calendar.get(Calendar.YEAR)
                    ,calendar.get(Calendar.MONTH)
                    ,calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        timeTextView.setRightTopTvClickListener(textView -> {
            Calendar calendar=Calendar.getInstance();
            new DatePickerDialog( context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String text = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                    end.set(year,month,dayOfMonth+1);//结束时间会默认设置成当日零点，应改为次日零点
                    timeTextView.setRightString(text);
                    upDate();
                }
            }
                    ,calendar.get(Calendar.YEAR)
                    ,calendar.get(Calendar.MONTH)
                    ,calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        timeTextView.setRightTvClickListener(textView -> {
            Calendar calendar=Calendar.getInstance();
            new DatePickerDialog( context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String text = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                    end.set(year,month,dayOfMonth+1);//结束时间会默认设置成当日零点，应改为次日零点
                    timeTextView.setRightString(text);
                    upDate();
                }
            }
                    ,calendar.get(Calendar.YEAR)
                    ,calendar.get(Calendar.MONTH)
                    ,calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

    }

    public void upDate(){
        billDao = new BillDao(context);
        showedBills = billDao.queryBillByTime(begin.getTime(),end.getTime());
        Values.clear();
        sumForProportion = 0;
        switch (mode){
            case "0":
                while (showedBills.size()>0) {
                    ArrayList<Bill> anotherBills = new ArrayList<>(showedBills);
                    Bill.filter(anotherBills, Bill.FilteredAttribute.type1, anotherBills.get(0).getType1());
                    Iterator<Bill> iterator = showedBills.iterator();
                    while (iterator.hasNext()) {
                        Bill bill = iterator.next();
                        if (bill.getType1().equals(anotherBills.get(0).getType1())) {
                            iterator.remove();
                        }
                    }
                    double sum = 0;
                    for (Bill i : anotherBills) {
                        if (i.getCNYAmount() > 0) {
                            sum = sum + i.getCNYAmount();
                        }
                    }
                    if (sum != 0) {
                        Values.add(new PieEntry((float) sum, anotherBills.get(0).getType1()));
                        sumForProportion =sumForProportion+sum;
                    }
                }
            case "1":
                while (showedBills.size()>0){
                    ArrayList<Bill> anotherBills = new ArrayList<>(showedBills);
                    Bill.filter(anotherBills,Bill.FilteredAttribute.type1,anotherBills.get(0).getType1());
                    Iterator<Bill> iterator = showedBills.iterator();
                    while(iterator.hasNext()){
                        Bill bill = iterator.next();
                        if(bill.getType1().equals(anotherBills.get(0).getType1())){
                            iterator.remove();
                        }
                    }
                    double sum = 0;
                    for(Bill i: anotherBills){
                        if(i.getCNYAmount()<0){
                            sum = sum-i.getCNYAmount();
                        }
                    }
                    if(sum!=0){
                        Values.add(new PieEntry((float) sum,anotherBills.get(0).getType1()));
                        sumForProportion =sumForProportion+sum;
                    }
                }
            case "2":
                while (showedBills.size()>0){
                    ArrayList<Bill> anotherBills = new ArrayList<>(showedBills);
                    Bill.filterByType2(anotherBills,anotherBills.get(0).getType1(),anotherBills.get(0).getType2());
                    Iterator<Bill> iterator = showedBills.iterator();
                    while(iterator.hasNext()){
                        Bill bill = iterator.next();
                        if(bill.getType1().equals(anotherBills.get(0).getType1()) && bill.getType2().equals(anotherBills.get(0).getType2())){
                            iterator.remove();
                        }
                    }
                    double sum = 0;
                    for(Bill i: anotherBills){
                        if(i.getCNYAmount()>0){
                            sum = sum+i.getCNYAmount();
                        }
                    }
                    if(sum!=0){
                        Values.add(new PieEntry((float) sum,anotherBills.get(0).getType1()+"/"+anotherBills.get(0).getType2()));
                        sumForProportion =sumForProportion+sum;
                    }
                }
            case "3":
                while (showedBills.size()>0){
                    ArrayList<Bill> anotherBills = new ArrayList<>(showedBills);
                    Bill.filterByType2(anotherBills,anotherBills.get(0).getType1(),anotherBills.get(0).getType2());
                    Iterator<Bill> iterator = showedBills.iterator();
                    while(iterator.hasNext()){
                        Bill bill = iterator.next();
                        if(bill.getType1().equals(anotherBills.get(0).getType1()) && bill.getType2().equals(anotherBills.get(0).getType2())){
                            iterator.remove();
                        }
                    }
                    double sum = 0;
                    for(Bill i: anotherBills){
                        if(i.getCNYAmount()<0){
                            sum = sum-i.getCNYAmount();
                        }
                    }
                    if(sum!=0){
                        Values.add(new PieEntry((float) sum,anotherBills.get(0).getType1()+"/"+anotherBills.get(0).getType2()));
                        sumForProportion =sumForProportion+sum;
                    }
                }
            case "4":
                while (showedBills.size()>0){
                    ArrayList<Bill> anotherBills = new ArrayList<>(showedBills);
                    Bill.filter(anotherBills,Bill.FilteredAttribute.number,anotherBills.get(0).getNumber());
                    Iterator<Bill> iterator = showedBills.iterator();
                    while(iterator.hasNext()){
                        Bill bill = iterator.next();
                        if(bill.getNumber().equals(anotherBills.get(0).getNumber())){
                            iterator.remove();
                        }
                    }
                    double sum = 0;
                    for(Bill i: anotherBills){
                        if(i.getCNYAmount()>0){
                            sum = sum+i.getCNYAmount();
                        }
                    }
                    if(sum!=0){
                        Values.add(new PieEntry((float) sum,anotherBills.get(0).getNumber()));
                        sumForProportion =sumForProportion+sum;
                    }
                }
            case "5":
                while (showedBills.size()>0){
                    ArrayList<Bill> anotherBills = new ArrayList<>(showedBills);
                    Bill.filter(anotherBills,Bill.FilteredAttribute.number,anotherBills.get(0).getNumber());
                    Iterator<Bill> iterator = showedBills.iterator();
                    while(iterator.hasNext()){
                        Bill bill = iterator.next();
                        if(bill.getNumber().equals(anotherBills.get(0).getNumber())){
                            iterator.remove();
                        }
                    }
                    double sum = 0;
                    for(Bill i: anotherBills){
                        if(i.getCNYAmount()<0){
                            sum = sum-i.getCNYAmount();
                        }
                    }
                    if(sum!=0){
                        Values.add(new PieEntry((float) sum,anotherBills.get(0).getNumber()));
                        sumForProportion =sumForProportion+sum;
                    }
                }
        }

        colors.clear();
        for (int i = 0; i < Values.size(); i++) {//list为数据列表
            Random random = new Random();
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            colors.add(Color.rgb(r,g,b));
        }

        Collections.sort(Values,new SortByValue());

        pieList.clear();
        for (int i = 0; i < Values.size(); i++) {//list为数据列表
            PieListData pieListData = new PieListData(colors.get(i),Values.get(i).getLabel(),Values.get(i).getValue(),sumForProportion);
            pieList.add(pieListData);
        }

        if (pieChart != null) {

            if (mode.equals("1") || mode.equals("3") || mode.equals("5")) {
                pieChart.setCenterText("总计:\n" + -sumForProportion);
            } else {
                pieChart.setCenterText("总计:\n" + sumForProportion);
            }

            pieDataSet.setValues(Values);
            pieDataSet.setColors(colors);
            pieData.setDataSet(pieDataSet);
            pieChart.clear();
            pieChart.notifyDataSetChanged();
            pieChart.setData(pieData);
            pieChart.invalidate();

            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight h) {
                PieEntry selectedEntry = (PieEntry) entry;
                if (entry == null)return;
                for(PieEntry i:Values){
                    if(selectedEntry.getLabel().equals(i.getLabel())){
                        if (mode.equals("1") || mode.equals("3") || mode.equals("5")) {
                            pieChart.setCenterText(i.getLabel()+":\n"+(-i.getValue()));
                        } else {
                            pieChart.setCenterText(i.getLabel()+":\n"+i.getValue());
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        }

        intentData.clear();
        intentData.add(""+begin.get(Calendar.YEAR));
        intentData.add(""+begin.get(Calendar.MONTH));
        intentData.add(""+begin.get(Calendar.DAY_OF_MONTH));
        intentData.add(""+end.get(Calendar.YEAR));
        intentData.add(""+end.get(Calendar.MONTH));
        intentData.add(""+end.get(Calendar.DAY_OF_MONTH));
        intentData.add(mode);

        PieDataListAdapter pieDataListAdapter = new PieDataListAdapter(pieList,intentData,context,mode);

        if (pieDataListRecyclerView != null) {
            pieDataListRecyclerView.setAdapter(pieDataListAdapter);
        }

    }

}

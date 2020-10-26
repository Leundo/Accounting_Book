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
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.xuexiang.templateproject.MyApp;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.xui.widget.actionbar.TitleBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ZznChartFragment extends BaseFragment {

    // MainActivity的context
    Context context;

    Button Btn1,Btn2,Btn3,Btn4;
    private LineChart line;
    List<Entry> list=new ArrayList<>();
    private ExpandableListView expandableListView;
    private String[] groups = {"收入"};
    private String[][] childs = {{"收入", "支出"}};
    Calendar begin = Calendar.getInstance();
    Calendar now = Calendar.getInstance();
    Calendar end = Calendar.getInstance();
    ArrayList<Bill> bills = Bill.getTestData();
    BillDao billDao;
    ArrayList<Integer> colors = new ArrayList<Integer>();
    List<Entry> xyValue =new ArrayList<>();
    List<String> dateCollectiton =new ArrayList<>();
    long x;
    float fax;
    long y;
    long z;
    LineDataSet lineDataSet;
    LineData lineData;
    //for (Bill i : bills) {
    //billDao.insertBill(i);
    //}
    ArrayList<Bill> showedBills;
    XAxis xAxis;
    YAxisRenderer yAxis;
    //Bill.filter(showedBills,Bill.FilteredAttribute.number,"我");

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
        return R.layout.fragment_chart;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initViews() {
        findViewAndInitBillDao();
        initLine();
        initExpandableList();
        initLineData();
        initBottom();
    }

    // 实例化
    public ZznChartFragment(Context aContext) {
        context = aContext;
    }


    void findViewAndInitBillDao() {
        line = (LineChart) findViewById(R.id.line_chart);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        Btn1=findViewById(R.id.btn_1);
        Btn2=findViewById(R.id.btn_2);
        Btn3=findViewById(R.id.btn_3);
        Btn4=findViewById(R.id.btn_4);

        billDao = new BillDao(context);
    }

    void initLine() {
        xAxis = line.getXAxis();
        yAxis = line.getRendererLeftYAxis();
        begin.set(2020,5,1,0,0,0);
        end.set(2020,8,30,0,0,0);
        showedBills = billDao.queryBillByTime(begin.getTime(),end.getTime());
        Log.d("zzn", "initLine: " + showedBills.toString());
        if(groups[0]==null)
        {
            groups[0]="收入";
        }
    }

    void initLineData() {

        now = begin;
        int dayCount = 0;
        while(!now.equals(end)){
            xyValue.add(new Entry(dayCount,0));
            dateCollectiton.add(""+now.get(Calendar.MONTH)+"/"+now.get(Calendar.DAY_OF_MONTH));
            //Toast.makeText(MainActivity.this, dateCollectiton.get(0), Toast.LENGTH_SHORT).show();
            //Toast.makeText(MainActivity.this, ""+l, Toast.LENGTH_SHORT).show();
            dayCount++;
            now.add(Calendar.DATE, 1);
            //Toast.makeText(MainActivity.this, ""+now.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
            //Toast.makeText(MainActivity.this, ""+now.get(Calendar.MONTH)+now.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
        }
        //H_SHORT).show();
        //Toast.makeText(MainActivity.this, ""+dateCollectiton.size(), Toast.LENGTH_SHORT).show();Toast.makeText(MainActivity.this, ""+now.get(Calendar.MONTH)+now.get(Calendar.DAY_OF_MONTH), Toast.LENGT

        //showedBills = billDao.queryBillByTime(begin.getTime(),end.getTime());
        //Bill.filterByType2(showedBills,"餐饮","早餐");
        for(Bill j:showedBills){
            if(j.getAmount()>0){
                //now.set(begin.get(Calendar.YEAR),begin.get(Calendar.MONTH),begin.get(Calendar.DAY_OF_MONTH));
                int k = (int)(j.getTime()-begin.getTimeInMillis())/1000/60/60/24;
                Toast.makeText(MyApp.getContext(), ""+k, Toast.LENGTH_SHORT).show();
                xyValue.get(k+30).setY((int)j.getAmount());
            }
        }

        list.addAll(xyValue);



        //Toast.makeText(MainActivity.this, , Toast.LENGTH_SHORT).show();
        //xAxis.setAxisMinimum((begin.getTimeInMillis()/1000/60/60/24/6));
        line.zoom(0.2f * 1 * xyValue.size(), 1f, xyValue.size() - 1, 0);
        xAxis.setAxisMaximum(xyValue.size()+1);
        xAxis.setDrawGridLines(false);
        line.setScaleXEnabled(false);
        line.setPinchZoom(false);
        line.getAxisLeft().setAxisLineColor(Color.BLUE);
        line.getAxisLeft().setDrawGridLines(false);
        xAxis.setLabelCount(xyValue.size()/60);
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter()
        {
            @Override
            public String getFormattedValue(float value, AxisBase axis)
            {
                return dateCollectiton.get((int) value);
            }
        });
        //line.setVisibleXRange(0, 5);
        line.setDragEnabled(true);
        line.setTouchEnabled(true);
        lineDataSet=new LineDataSet(list,"");
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setCubicIntensity(1f);
        lineDataSet.setDrawCircles(true);

        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setDrawValues(false);

        lineData=new LineData(lineDataSet);
        line.setData(lineData);
        //简单美化
        //   X轴所在位置   默认为上面
        //隐藏右边的Y轴
        line.getAxisRight().setEnabled(false);
    }

    void initExpandableList() {
        expandableListView.setGroupIndicator(null);

        expandableListView.setAdapter(new LineExpandableListAdapter());
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if(childPosition==0){
                    Toast.makeText(MyApp.getContext(), "更新收入数据", Toast.LENGTH_SHORT).show();
                    groups[0]="收入";
                    expandableListView.setAdapter(new LineExpandableListAdapter());
                }
                else if(childPosition==1){
                    Toast.makeText(MyApp.getContext(), "更新支出数据", Toast.LENGTH_SHORT).show();
                    groups[0]="支出";
                    expandableListView.setAdapter(new LineExpandableListAdapter());
                    List<Entry> newlist =new ArrayList<>();
                    List<Entry> newxyValue =new ArrayList<>();
                    dateCollectiton.clear();
                    now = begin;
                    /*int Count = 0;
                    while(!now.equals(end)){
                        newxyValue.add(new Entry(Count,0));
                        dateCollectiton.add(""+now.get(Calendar.MONTH)+"/"+now.get(Calendar.DAY_OF_MONTH));
                        //Toast.makeText(MainActivity.this, dateCollectiton.get(0), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(MainActivity.this, ""+l, Toast.LENGTH_SHORT).show();
                        Count++;
                        now.add(Calendar.DATE, 1);
                        //Toast.makeText(MainActivity.this, ""+now.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(MainActivity.this, ""+now.get(Calendar.MONTH)+now.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
                    }

                    for(Bill j:showedBills){
                        if(j.getAmount()<0){
                            //now.set(begin.get(Calendar.YEAR),begin.get(Calendar.MONTH),begin.get(Calendar.DAY_OF_MONTH));
                            int k = (int)(j.getTime()-begin.getTimeInMillis())/1000/60/60/24;
                            Toast.makeText(MainActivity.this, ""+k, Toast.LENGTH_SHORT).show();
                            newxyValue.get(k+30).setY((int)j.getAmount());
                        }
                    }

                    newlist .addAll(newxyValue);


                    if (line.getData()!=null&&line.getData().getDataSetCount()>0){
                        lineDataSet =(LineDataSet)line.getData().getDataSetByIndex(0);
                        lineDataSet.setValues(newlist);
                        line.getData().notifyDataChanged();
                        line.notifyDataSetChanged();
                    }*/
                    //简单美化

                }
                return true;
            }
        });
    }

    void initBottom() {
        Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                new DatePickerDialog( context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String text =  "开始时间:\n" +year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                        Btn1.setText(text);
                    }
                }
                        ,calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH)
                        ,calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                new DatePickerDialog( context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String text =  "结束时间:\n" +year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                        Btn2.setText(text);
                    }
                }
                        ,calendar.get(Calendar.YEAR)
                        ,calendar.get(Calendar.MONTH)
                        ,calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        Btn4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent=new Intent(MyApp.getContext(),pieChart.class);
//                intent.putExtra("data",groups[0]);
//                startActivity(intent);
//            }
//        });
    }


    class LineExpandableListAdapter extends BaseExpandableListAdapter {

        //返回一级列表的个数
        @Override
        public int getGroupCount() {
            return groups.length;
        }

        //返回每个二级列表的个数
        @Override
        public int getChildrenCount(int groupPosition) { //参数groupPosition表示第几个一级列表
            Log.d("smyhvae", "-->" + groupPosition);
            return childs[groupPosition].length;
        }

        //返回一级列表的单个item（返回的是对象）
        @Override
        public Object getGroup(int groupPosition) {
            return groups[groupPosition];
        }

        //返回二级列表中的单个item（返回的是对象）
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childs[groupPosition][childPosition];  //不要误写成groups[groupPosition][childPosition]
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
                convertView = getLayoutInflater().inflate(R.layout.item_group, null);
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
                convertView = getLayoutInflater().inflate(R.layout.item_child, null);
            }

            TextView tv_child = (TextView) convertView.findViewById(R.id.tv_child);

            //iv_child.setImageResource(resId);
            tv_child.setText(childs[groupPosition][childPosition]);

            return convertView;
        }

        //二级列表中的item是否能够被选中？可以改为true
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


}

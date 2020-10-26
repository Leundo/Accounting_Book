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

package com.zzn.chart.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.xuexiang.templateproject.R;
import com.zzn.chart.project.adapter.BrowseListViewAdapter;
import com.zzn.chart.project.data.BrowseGroupEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

//ZZn
public class BrowseActivity extends AppCompatActivity {
    Calendar begin = Calendar.getInstance();
    Calendar end = Calendar.getInstance();
    BillDao billDao;
    ArrayList<Bill> showedBills;
    ArrayList<BrowseGroupEntity> BillList = new ArrayList<>();
    ImageView return_button;
    private ExpandableListView browseListView;
    TextView browse_title;
    int begin_year;
    int begin_mouth;
    int begin_day;
    int end_year;
    int end_mouth;
    int end_day;
    String mode;
    String Label;
    SimpleDateFormat formatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        请不要直接插入自己的fragment相关的逻辑，请先封装成一个函数，方便区别代码的编写者。
        另外fragment的设置逻辑请不要放在这，应该在fragment的构建函数内实现。
         */
        getIntentData();
        initViewAndData();
        initBrowseText();
        initBottom();
    }

    void getIntentData(){
        Intent intent=getIntent();
        begin_year = Integer.parseInt(intent.getStringExtra("begin_year"));
        begin_mouth = Integer.parseInt(intent.getStringExtra("begin_mouth"));
        begin_day = Integer.parseInt(intent.getStringExtra("begin_day"));
        end_year = Integer.parseInt(intent.getStringExtra("end_year"));
        end_mouth = Integer.parseInt(intent.getStringExtra("end_mouth"));
        end_day = Integer.parseInt(intent.getStringExtra("end_day"));
        mode = intent.getStringExtra("mode");
        Label = intent.getStringExtra("Label");
    }

    void initViewAndData(){
        setContentView(R.layout.activity_browse);
        browse_title = findViewById(R.id.browse_data_type);
        begin.set(begin_year,begin_mouth,begin_day,0,0,0);
        end.set(end_year,end_mouth,end_day,0,0,0);
        billDao = new BillDao(this);
        showedBills = billDao.queryBillByTime(begin.getTime(),end.getTime());
        formatter = new SimpleDateFormat("yyyy.MM.dd EEEE");
    }

    void initBrowseText(){
        switch (mode){
            case "0":
            case "1":
                browse_title.setText("一级类别:"+Label);
                Bill.filter(showedBills, Bill.FilteredAttribute.type1,Label);
                break;
            case "2":
            case "3":
                browse_title.setText("二级类别:"+Label);
                String[] split_Label = Label.split("\\/");
                Bill.filterByType2(showedBills,split_Label[0],split_Label[1]);
                break;
            case "4":
            case "5":
                browse_title.setText("成员:"+Label);
                Bill.filter(showedBills, Bill.FilteredAttribute.number,Label);
                break;
        }
        while (showedBills.size()>0) {
            ArrayList<Bill> anotherBills = new ArrayList<>(showedBills);
            Iterator<Bill> iterator = showedBills.iterator();
            while(iterator.hasNext()){
                Bill bill = iterator.next();
                if(formatter.format(bill.getTime()).equals(formatter.format(anotherBills.get(0).getTime()))){
                    iterator.remove();
                }
            }
            ArrayList<Bill> Bills = new ArrayList<>();
            for(Bill i: anotherBills){
                if(formatter.format(i.getTime()).equals(formatter.format(anotherBills.get(0).getTime())) && AddOrNot(i.getCNYAmount())){
                    Bills.add(i);
                }
            }
            if(Bills.size()!=0){
                BrowseGroupEntity BrowseChildItems =new BrowseGroupEntity(formatter.format(anotherBills.get(0).getTime()),Bills);
                BillList.add(BrowseChildItems);
            }
        }
        browseListView = findViewById(R.id.browse_ListView);
        browseListView.setGroupIndicator(null);
        BrowseListViewAdapter browseListViewAdapter = new BrowseListViewAdapter(BillList);
        browseListView.setAdapter(browseListViewAdapter);
        for(int i = 0; i < browseListViewAdapter.getGroupCount(); i++){
            browseListView.expandGroup(i);
        }
    }

    void initBottom(){
        return_button = (ImageView) findViewById(R.id.return_button);
        return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    Boolean AddOrNot(double amount){
        Boolean flag = false;
        switch (mode){
            case "0":
            case "2":
            case "4":
                if(amount>0){
                    flag = true;
                }
                break;
            case "1":
            case "3":
            case "5":
                if(amount<0){
                    flag = true;
                }
                break;
        }
        return flag;
    }
}


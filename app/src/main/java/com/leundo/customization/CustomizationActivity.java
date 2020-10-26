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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leundo.data.BillDao;
import com.leundo.exchange.ExchangeAdapter;
import com.leundo.flow.activity.FlowSettingActivity;
import com.leundo.flow.adapter.FlowSettingAdapter;
import com.xuexiang.templateproject.R;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectListener;

import java.util.ArrayList;
import java.util.List;

public class CustomizationActivity extends AppCompatActivity {

    Button backButton;
    Button addButton;
    Button deleteButton;

    LinearLayout funcLinearLayout;
    LinearLayout attrLinearLayout;
    LinearLayout iconLinearLayout;
    LinearLayout type1LinearLayout;
    LinearLayout nameLinearLayout;

    TextView funcTextView;
    TextView funcResultTextView;
    TextView attrTextView;
    TextView attrResultTextView;
    TextView iconTextView;
    TextView iconResultTextView;
    TextView type1TextView;
    TextView type1ResultTextView;
    TextView nameTextView;
    TextView nameResultTextView;

    OptionsPickerView showFunc;
    OptionsPickerView showAttr;
    OptionsPickerView showType1;
    OptionsPickerView showName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);
        findView();
        initPickerView();
        initLinearLayout();
        initButton();

        refreshDataAndUI();
        PickIconModel.doPick = false;
    }

    private void findView() {
        backButton = (Button) findViewById(R.id.customization_back);
        addButton = (Button) findViewById(R.id.customization_add);
        deleteButton = (Button) findViewById(R.id.customization_delete);

        funcLinearLayout = (LinearLayout) findViewById(R.id.func_block);
        funcTextView = (TextView) findViewById(R.id.func_name);
        funcResultTextView = (TextView) findViewById(R.id.func_result);

        attrLinearLayout = (LinearLayout) findViewById(R.id.attr_block);
        attrTextView = (TextView) findViewById(R.id.attr_name);
        attrResultTextView = (TextView) findViewById(R.id.attr_result);

        iconLinearLayout = (LinearLayout) findViewById(R.id.icon_block);
        iconTextView = (TextView) findViewById(R.id.icon_name);
        iconResultTextView = (TextView) findViewById(R.id.icon_result);

        type1LinearLayout = (LinearLayout) findViewById(R.id.type1_block);
        type1TextView = (TextView) findViewById(R.id.type1_name);
        type1ResultTextView = (TextView) findViewById(R.id.type1_result);

        nameLinearLayout = (LinearLayout) findViewById(R.id.name_block);
        nameTextView = (TextView) findViewById(R.id.name_name);
        nameResultTextView = (TextView) findViewById(R.id.name_result);
    }

    private void initLinearLayout() {
        funcLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFunc.show();
            }
        });

        attrLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAttr.show();
            }
        });

        type1LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showType1.show();
            }
        });

        nameLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!funcResultTextView.getText().toString().equals("添加")) {
                    return;
                } else if (attrResultTextView.getText().toString().equals("二级类别") && (type1ResultTextView.getText().toString().equals(""))) {
                    return;
                }
                showName.show();
            }
        });

        iconLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(this, PickIconActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(CustomizationActivity.this, PickIconActivity.class);
                startActivity(intent);
            }
        });


    }

    private void initPickerView() {
        showFunc = getShowFuncPickerView();
        showAttr = getShowAttrPickerView();
        showType1 = getShowType1PickerView();

    }

    private void initButton() {
        // 设置返回按钮
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                moveTaskToBack(true);
            }
        });

    }



    private void refreshDataAndUI() {
        showName = getShowNamePickerView(attrResultTextView.getText().toString());
        nameResultTextView.setText("");

        if (attrResultTextView.getText().toString().equals("一级类别")) {
            iconLinearLayout.setVisibility(View.VISIBLE);
        } else {
            iconLinearLayout.setVisibility(View.GONE);
        }
        if (attrResultTextView.getText().toString().equals("二级类别")) {
            type1LinearLayout.setVisibility(View.VISIBLE);
        } else {
            type1LinearLayout.setVisibility(View.GONE);
        }

    }


    // 功能选择器
    private OptionsPickerView getShowFuncPickerView() {
        final ArrayList<String> optionsItems = new ArrayList<>();

        optionsItems.add("添加");
        optionsItems.add("删除");

        OptionsPickerView pv = new OptionsPickerBuilder(CustomizationActivity.this, new OnOptionsSelectListener() {

            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String item = optionsItems.get(options1);
                funcResultTextView.setText(item);
                refreshDataAndUI();
                return false;
            }
        }).build();
        pv.setPicker(optionsItems);
        return pv;
    }


    private OptionsPickerView getShowAttrPickerView() {
        final ArrayList<String> optionsItems = new ArrayList<>();

        optionsItems.add("账户");
        optionsItems.add("一级类别");
        optionsItems.add("二级类别");
        optionsItems.add("成员");
        optionsItems.add("商家");

        OptionsPickerView pv = new OptionsPickerBuilder(CustomizationActivity.this, new OnOptionsSelectListener() {

            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String item = optionsItems.get(options1);
                attrResultTextView.setText(item);
                refreshDataAndUI();
                return false;
            }
        }).build();
        pv.setPicker(optionsItems);
        return pv;
    }

    private OptionsPickerView getShowType1PickerView() {
        final ArrayList<String> optionsItems = new ArrayList<>();

        BillDao billDao = new BillDao(this);
        List<String> type1List = billDao.queryType();

        for (String type1: type1List) {
            optionsItems.add(type1);
        }

        OptionsPickerView pv = new OptionsPickerBuilder(CustomizationActivity.this, new OnOptionsSelectListener() {

            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String item = optionsItems.get(options1);
                type1ResultTextView.setText(item);
                refreshDataAndUI();
                return false;
            }
        }).build();
        pv.setPicker(optionsItems);
        return pv;
    }

    private OptionsPickerView getShowNamePickerView(String attr) {
        final ArrayList<String> optionsItems = new ArrayList<>();

        BillDao billDao = new BillDao(this);
        List<String> itemList = null;

        if (attr.equals("一级类别")) {
            itemList = billDao.queryType();
        } else if (attr.equals("二级类别")) {
            itemList = billDao.queryType(type1ResultTextView.getText().toString());
        } else if (attr.equals("账户")) {
            itemList = billDao.queryAccount();
        } else if (attr.equals("成员")) {
            itemList = billDao.queryNumber();
        } else if (attr.equals("商家")) {
            itemList = billDao.queryFirm();
        }

        for (String item: itemList) {
            optionsItems.add(item);
        }

        OptionsPickerView pv = new OptionsPickerBuilder(CustomizationActivity.this, new OnOptionsSelectListener() {

            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String item = optionsItems.get(options1);
                nameResultTextView.setText(item);
                refreshDataAndUI();
                return false;
            }
        }).build();
        pv.setPicker(optionsItems);
        return pv;
    }

}
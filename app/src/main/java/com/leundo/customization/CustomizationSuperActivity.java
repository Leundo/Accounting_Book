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
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.xuexiang.templateproject.MyApp;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectListener;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import java.util.ArrayList;
import java.util.List;

public class CustomizationSuperActivity extends AppCompatActivity {

    ImageView backButton;
    Button addButton;

    LinearLayout mainLayout;

    SuperTextView funcTextView;
    SuperTextView attrTextView;
    SuperTextView iconTextView;
    SuperTextView type1TextView;
    SuperTextView nameTextView;
    SuperTextView inputTextView;

    OptionsPickerView showFunc;
    OptionsPickerView showAttr;
    OptionsPickerView showType1;
    OptionsPickerView showName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization_supertextview);
        findView();
        initButton();

        initPickerView();
        initSpuerTextViewListener();

        refreshDataAndUI();
        PickIconModel.doPick = false;

        BillDao billDao = new BillDao(this);
        Log.d("TAG", "onCreate: " + billDao.queryTypeIcon());
    }

    private void findView() {
        backButton = (ImageView) findViewById(R.id.customization_back);
        addButton = (Button) findViewById(R.id.customization_add);

        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        funcTextView = (SuperTextView) findViewById(R.id.func_textview);
        attrTextView = (SuperTextView) findViewById(R.id.attr_textview);
        iconTextView = (SuperTextView) findViewById(R.id.icon_textview);
        type1TextView = (SuperTextView) findViewById(R.id.type1_textview);
        nameTextView = (SuperTextView) findViewById(R.id.name_textview);
        inputTextView = (SuperTextView) findViewById(R.id.input_textview);
    }

    private void initSpuerTextViewListener() {
        funcTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.hideSoftInputFromWindow(CustomizationSuperActivity.this);
                funcTextView.requestFocus();
                showFunc.show();
//                funcTextView.requestFocus();
            }
        });

        attrTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.hideSoftInputFromWindow(CustomizationSuperActivity.this);
                attrTextView.requestFocus();
                showAttr.show();
            }
        });

        type1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.hideSoftInputFromWindow(CustomizationSuperActivity.this);
                type1TextView.requestFocus();
                showType1.show();
            }
        });

        nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.hideSoftInputFromWindow(CustomizationSuperActivity.this);
                nameTextView.requestFocus();
                if (!funcTextView.getRightString().equals("删除")) {
                    return;
                } else if (attrTextView.getRightString().equals("二级类别") && (attrTextView.getRightString().equals(""))) {
                    return;
                }
                showName.show();
            }
        });

        iconTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.hideSoftInputFromWindow(CustomizationSuperActivity.this);
                iconTextView.requestFocus();
                Intent intent = new Intent(CustomizationSuperActivity.this, PickIconActivity.class);
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
                MyApp.hideSoftInputFromWindow(CustomizationSuperActivity.this);

                finish();
//                moveTaskToBack(true);
            }
        });

        // 设置确定按钮
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApp.hideSoftInputFromWindow(CustomizationSuperActivity.this);

                if (attrTextView.getRightString().equals("二级类别") && type1TextView.getRightString().equals("")) {
                    DialogLoader.getInstance().showTipDialog(
                            CustomizationSuperActivity.this,
                            "提示",
                            "请选择一级类别",
                            "确定");
                    return;
                }

                if (funcTextView.getRightString().equals("删除")) {
                    if (nameTextView.getRightString().equals("")) {
                        DialogLoader.getInstance().showTipDialog(
                                CustomizationSuperActivity.this,
                                "提示",
                                "请选择名称",
                                "确定");
                        return;
                    }

                    DialogLoader.getInstance().showConfirmDialog(
                            CustomizationSuperActivity.this,
                            "是否要删除该属性？相应的账单数据也会删除，此步骤不可撤销！",
                            "确定",
                            (dialog, which) -> {
                                deleteAttr();
                                dialog.dismiss();
                            },
                            "取消",
                            (dialog, which) -> {
                                dialog.dismiss();
                            }
                    );
                } else if (funcTextView.getRightString().equals("添加")) {
                    if (inputTextView.getCenterEditValue().equals("")) {
                        DialogLoader.getInstance().showTipDialog(
                                CustomizationSuperActivity.this,
                                "提示",
                                "请输入名称",
                                "确定");
                        return;
                    }
                    

                    if (attrTextView.getRightString().equals("一级类别") && !PickIconModel.doPick) {
                        DialogLoader.getInstance().showTipDialog(
                                CustomizationSuperActivity.this,
                                "提示",
                                "请选择图标",
                                "确定");
                        return;
                    }
                    insertAttr();
                }
            }
        });

    }

    private void deleteAttr() {
        BillDao billDao = new BillDao(CustomizationSuperActivity.this);
        if (attrTextView.getRightString().equals("账户")) {
            billDao.deleteBillByAccount(nameTextView.getRightString());
            billDao.deleteAccount(nameTextView.getRightString());
        } else if (attrTextView.getRightString().equals("一级类别")) {
            billDao.deleteBillByType(nameTextView.getRightString());
            billDao.deleteType(nameTextView.getRightString());
        } else if (attrTextView.getRightString().equals("二级类别")) {
            billDao.deleteBillByType(type1TextView.getRightString(), nameTextView.getRightString());
            billDao.deleteType(type1TextView.getRightString(), nameTextView.getRightString());
        } else if (attrTextView.getRightString().equals("成员")) {
            billDao.deleteBillByNumber(nameTextView.getRightString());
            billDao.deleteNumber(nameTextView.getRightString());
        } else if (attrTextView.getRightString().equals("商家")) {
            billDao.deleteBillByFirm(nameTextView.getRightString());
            billDao.deleteFirm(nameTextView.getRightString());
        }

        nameTextView.setRightString("");
        DialogLoader.getInstance().showTipDialog(
                CustomizationSuperActivity.this,
                "提示",
                "删除成功",
                "确定");

    }

    private void insertAttr() {
        MyApp.hideSoftInputFromWindow(CustomizationSuperActivity.this);
        BillDao billDao = new BillDao(CustomizationSuperActivity.this);
        if (attrTextView.getRightString().equals("账户")) {
            if (billDao.insertAccount(inputTextView.getCenterEditValue()) == -1) {
                showRepetitionDialog(inputTextView.getCenterEditValue(), "账户");
                inputTextView.setCenterEditString("");
                return;
            }
        } else if (attrTextView.getRightString().equals("一级类别")) {
            if (billDao.insertType(inputTextView.getCenterEditValue()) == -1) {
                showRepetitionDialog(inputTextView.getCenterEditValue(), "一级类别");
                inputTextView.setCenterEditString("");
                return;
            }
            billDao.insertTypeIcon(inputTextView.getCenterEditValue(), PickIconModel.iconResource[PickIconModel.pickResourceRow]);
            Bill.updateTypeIcon();
        } else if (attrTextView.getRightString().equals("二级类别")) {
            if (billDao.insertType(type1TextView.getRightString(), inputTextView.getCenterEditValue()) == -1) {
                showRepetitionDialog(inputTextView.getCenterEditValue(), "二级类别");
                inputTextView.setCenterEditString("");
                return;
            }
        } else if (attrTextView.getRightString().equals("成员")) {
            if (billDao.insertNumber(inputTextView.getCenterEditValue()) == -1) {
                showRepetitionDialog(inputTextView.getCenterEditValue(), "成员");
                inputTextView.setCenterEditString("");
                return;
            }
        } else if (attrTextView.getRightString().equals("商家")) {
            if (billDao.insertFirm(inputTextView.getCenterEditValue()) == -1) {
                showRepetitionDialog(inputTextView.getCenterEditValue(), "商家");
                inputTextView.setCenterEditString("");
                return;
            }
        }
        inputTextView.setCenterEditString("");
        DialogLoader.getInstance().showTipDialog(
                CustomizationSuperActivity.this,
                "提示",
                "添加成功",
                "确定");

    }

    private void showRepetitionDialog(String name, String attr) {
        DialogLoader.getInstance().showTipDialog(
                CustomizationSuperActivity.this,
                "添加失败",
                "已存在名称为“" + name + "”的" + attr,
                "确定");
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (PickIconModel.doPick) {
            Drawable icon = getResources().getDrawable(PickIconModel.iconResource[PickIconModel.pickResourceRow]);
            icon.setColorFilter(0xFF99CC, PorterDuff.Mode.SRC_ATOP);
            iconTextView.setRightTvDrawableRight(icon);
        }
    }

    private void refreshDataAndUI() {
        showName = getShowNamePickerView(attrTextView.getRightString());

        if (attrTextView.getRightString().equals("一级类别") && funcTextView.getRightString().equals("添加")) {
            iconTextView.setVisibility(View.VISIBLE);
        } else {
            iconTextView.setVisibility(View.GONE);
        }
        if (attrTextView.getRightString().equals("二级类别")) {
            type1TextView.setVisibility(View.VISIBLE);
        } else {
            type1TextView.setVisibility(View.GONE);
        }
        if (funcTextView.getRightString().equals("添加")) {
            nameTextView.setVisibility(View.GONE);
            inputTextView.setVisibility(View.VISIBLE);
        } else if(funcTextView.getRightString().equals("删除")) {
            nameTextView.setVisibility(View.VISIBLE);
            inputTextView.setVisibility(View.GONE);
        }

    }


    // 功能选择器
    private OptionsPickerView getShowFuncPickerView() {
        final ArrayList<String> optionsItems = new ArrayList<>();

        optionsItems.add("添加");
        optionsItems.add("删除");

        OptionsPickerView pv = new OptionsPickerBuilder(CustomizationSuperActivity.this, new OnOptionsSelectListener() {

            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String item = optionsItems.get(options1);
                funcTextView.setRightString(item);
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

        OptionsPickerView pv = new OptionsPickerBuilder(CustomizationSuperActivity.this, new OnOptionsSelectListener() {

            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String item = optionsItems.get(options1);
                attrTextView.setRightString(item);

                refreshDataAndUI();
                nameTextView.setRightString("");
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

        OptionsPickerView pv = new OptionsPickerBuilder(CustomizationSuperActivity.this, new OnOptionsSelectListener() {

            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String item = optionsItems.get(options1);
                type1TextView.setRightString(item);

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
            itemList = billDao.queryType(type1TextView.getRightString());
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

        OptionsPickerView pv = new OptionsPickerBuilder(CustomizationSuperActivity.this, new OnOptionsSelectListener() {

            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String item = optionsItems.get(options1);
                nameTextView.setRightString(item);

                refreshDataAndUI();
                return false;
            }
        }).build();
        pv.setPicker(optionsItems);
        return pv;
    }

}
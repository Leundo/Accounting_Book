package com.leundo.flow.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.leundo.flow.adapter.FlowSettingAdapter;
import com.leundo.flow.entity.FlowGroupModel;
import com.xuexiang.templateproject.MyApp;
import com.xuexiang.templateproject.R;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FlowSettingActivity extends AppCompatActivity {

    public enum ShowMode {
        year, month
    }

    RecyclerView rvList;
    ImageView backImage;

    public static ShowMode showMode = ShowMode.year;
    // 选取数据的时间
    public static int year = Calendar.getInstance().get(Calendar.YEAR);
    public static int month = Calendar.getInstance().get(Calendar.MONTH);

    FlowSettingAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findView();
        initButton();


        // 设置RecyclerView
        rvList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new FlowSettingAdapter(this, showMode, year, month);

//        Calendar calendar = Calendar.getInstance();
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH);

//        adapter.setResult(FlowSettingAdapter.MODE_POSITION, "年");
//        adapter.setResult(FlowSettingAdapter.TIME_POSITION, year + "年");

        // 显示模式选择器
        final OptionsPickerView showModePV = getShowModePickerView();
        // 时间选择器
        final TimePickerView yearPV = getYearPickerView();
        final TimePickerView monthPV = getMonthPickerView();
        // 账户选择器
        final OptionsPickerView accountPV = getAccountModePickerView();
        // 成员选择器
        final OptionsPickerView numberPV = getNumberModePickerView();
        // 商家选择器
        final OptionsPickerView firmPV = getFirmModePickerView();


        adapter.setOnItemClick(new FlowSettingAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                switch (position) {
                    case 0:
                        showModePV.show();
                        break;
                    case 1:
                        if (showMode == ShowMode.year) {
                            yearPV.show();
                        } else {
                            monthPV.show();
                        }
                        break;
                    case 2:
                        accountPV.show();
                        break;
                    case 3:
                        numberPV.show();
                        break;
                    case 4:
                        firmPV.show();
                        break;
                    default:
                        break;
                }
            }
        });

        rvList.setAdapter(adapter);

        // 防止数据不匹配
        refreshDataAndUI();


    }

    private void findView() {
        setContentView(R.layout.activity_flow_setting);
        rvList = (RecyclerView) findViewById(R.id.flow_setting_recycler);
        backImage = (ImageView) findViewById(R.id.flow_setting_back);

    }

    private void initButton() {
        // 设置返回按钮
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                moveTaskToBack(true);
            }
        });
    }


    // 模式选择器
    private OptionsPickerView getShowModePickerView() {
        final ArrayList<String> optionsItems = new ArrayList<>();
        optionsItems.add("年");
        optionsItems.add("月");

        OptionsPickerView pv = new OptionsPickerBuilder(FlowSettingActivity.this, new OnOptionsSelectListener() {

            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String item = optionsItems.get(options1);
                Calendar calendar = Calendar.getInstance();
                if(item.equals("年")) {
                    showMode = ShowMode.year;
                    adapter.setResult(FlowSettingAdapter.MODE_POSITION, "年");
                    adapter.setResult(FlowSettingAdapter.TIME_POSITION, calendar.get(Calendar.YEAR) + "年");
                } else {
                    showMode = ShowMode.month;
                    adapter.setResult(FlowSettingAdapter.MODE_POSITION, "月");
                    adapter.setResult(FlowSettingAdapter.TIME_POSITION, calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
                }

                refreshDataAndUI();

                return false;
            }
        }).build();
        pv.setPicker(optionsItems);
        return pv;
    }

    // 年选择器
    private TimePickerView getYearPickerView() {

        return new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelected(Date date, View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                adapter.setResult(FlowSettingAdapter.TIME_POSITION, calendar.get(Calendar.YEAR) + "年");
                year = calendar.get(Calendar.YEAR);
                refreshDataAndUI();
            }
        })
                .setType(new boolean[]{true, false, false, false, false, false})
                .build();
    }

    // 月选择器
    private TimePickerView getMonthPickerView() {

        return new TimePickerBuilder(this, new OnTimeSelectListener() {

            @Override
            public void onTimeSelected(Date date, View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

                adapter.setResult(FlowSettingAdapter.TIME_POSITION, calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月");
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);

                refreshDataAndUI();
            }
        })
                .setType(new boolean[]{true, true, false, false, false, false})
                .build();
    }

    // 账户选择器
    private OptionsPickerView getAccountModePickerView() {
        final ArrayList<String> optionsItems = new ArrayList<>();
        BillDao billDao = new BillDao(this);
        List<String> accountList = billDao.queryAccount();
        optionsItems.add("所有");
        for (String account : accountList ) {
            optionsItems.add(account);
        }


        OptionsPickerView pv = new OptionsPickerBuilder(FlowSettingActivity.this, new OnOptionsSelectListener() {

            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String item = optionsItems.get(options1);
                adapter.setResult(FlowSettingAdapter.ACCOUNT_POSITION, item);

                refreshDataAndUI();

                return false;
            }
        }).build();
        pv.setPicker(optionsItems);
        return pv;
    }

    // 成员选择器
    private OptionsPickerView getNumberModePickerView() {
        final ArrayList<String> optionsItems = new ArrayList<>();
        BillDao billDao = new BillDao(this);
        List<String> numberList = billDao.queryNumber();
        optionsItems.add("所有");
        for (String number : numberList ) {
            optionsItems.add(number);
        }


        OptionsPickerView pv = new OptionsPickerBuilder(FlowSettingActivity.this, new OnOptionsSelectListener() {

            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String item = optionsItems.get(options1);
                adapter.setResult(FlowSettingAdapter.NUMBER_POSITION, item);

                refreshDataAndUI();
                return false;
            }

        }).build();
        pv.setPicker(optionsItems);
        return pv;
    }

    // 商家选择器
    private OptionsPickerView getFirmModePickerView() {
        final ArrayList<String> optionsItems = new ArrayList<>();
        BillDao billDao = new BillDao(this);
        List<String> firmList = billDao.queryFirm();
        optionsItems.add("所有");
        for (String firm : firmList ) {
            optionsItems.add(firm);
        }


        OptionsPickerView pv = new OptionsPickerBuilder(FlowSettingActivity.this, new OnOptionsSelectListener() {

            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String item = optionsItems.get(options1);
                adapter.setResult(FlowSettingAdapter.FIRM_POSITION, item);

                refreshDataAndUI();
                return false;
            }
        }).build();
        pv.setPicker(optionsItems);
        return pv;
    }

    public static void refreshData() {
        // 修改FlowGroupModel的数据
        BillDao billDao = new BillDao(MyApp.getContext());
        ArrayList<Bill> bills;

        // 时间设置
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        if(showMode == ShowMode.year) {
            start.set(year,0,1,0,0,0);
            end.set(year + 1,0,1,0,0,0);
        } else {
            start.set(year,month,1,0,0,0);
            end.set(year,month + 1,1,0,0,0);
        }

        String accountResult = FlowSettingAdapter.getResult(FlowSettingAdapter.ACCOUNT_POSITION);

        String numberResult = FlowSettingAdapter.getResult(FlowSettingAdapter.NUMBER_POSITION);
        String firmResult = FlowSettingAdapter.getResult(FlowSettingAdapter.FIRM_POSITION);


        if (accountResult == FlowSettingAdapter.ACCOUNT_ALL) {
            bills = billDao.queryBillByTime(start.getTime(), end.getTime());
        } else {
            bills = billDao.queryBillByTime(start.getTime(), end.getTime(), accountResult);
        }


        FlowGroupModel.flowGroups.clear();
        if(showMode == ShowMode.year) {
            FlowGroupModel.flowGroups.addAll(FlowGroupModel.getFlowGroupsFromBillsByYear(bills, year));
        } else {
            FlowGroupModel.flowGroups.addAll(FlowGroupModel.getFlowGroupsFromBillsByMonth(bills, year, month));
        }

        // 过滤
        FlowGroupModel.filter(FlowGroupModel.FilteredAttribute.number, numberResult);
        FlowGroupModel.filter(FlowGroupModel.FilteredAttribute.firm, firmResult);


    }

    private void refreshDataAndUI() {
        // 修改FlowGroupModel的数据
        BillDao billDao = new BillDao(this);
        ArrayList<Bill> bills;

        // 时间设置
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        if(showMode == ShowMode.year) {
            start.set(year,0,1,0,0,0);
            end.set(year + 1,0,1,0,0,0);
        } else {
            start.set(year,month,1,0,0,0);
            end.set(year,month + 1,1,0,0,0);
        }

        String accountResult = adapter.getResult(FlowSettingAdapter.ACCOUNT_POSITION);

        String numberResult = adapter.getResult(FlowSettingAdapter.NUMBER_POSITION);
        String firmResult = adapter.getResult(FlowSettingAdapter.FIRM_POSITION);


        if (accountResult == FlowSettingAdapter.ACCOUNT_ALL) {
            bills = billDao.queryBillByTime(start.getTime(), end.getTime());
        } else {
            bills = billDao.queryBillByTime(start.getTime(), end.getTime(), accountResult);
        }


        FlowGroupModel.flowGroups.clear();
        if(showMode == ShowMode.year) {
            FlowGroupModel.flowGroups.addAll(FlowGroupModel.getFlowGroupsFromBillsByYear(bills, year));
        } else {
            FlowGroupModel.flowGroups.addAll(FlowGroupModel.getFlowGroupsFromBillsByMonth(bills, year, month));
        }

        // 过滤
        FlowGroupModel.filter(FlowGroupModel.FilteredAttribute.number, numberResult);
        FlowGroupModel.filter(FlowGroupModel.FilteredAttribute.firm, firmResult);


        adapter.notifyDataSetChanged();


    }

}
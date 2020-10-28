package com.loancold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leundo.budget.BudgetModel;
import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.xuexiang.templateproject.MyApp;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.activity.MainActivity;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * leundo:
 * AddBillProActivity 是对 AddBillActivity 的魔改, 仅仅改变UI
 */

public class AddBillProActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = "AddBillActivity";
    public static String DELETEBILL = "删除账单";
    public static String ADDBILL = "添加账单";
    public static String MODIFYBILL = "修改账单";
    public static String NONE = "无操作";

    private List<String> options1Category = new ArrayList<>();
    private List<String> options1Type = new ArrayList<>();
    private List<List<String>> options2Type = new ArrayList<>();
//    private ArrayList<String> options1AccountTime = new ArrayList<>();
    private List<String> options1Member = new ArrayList<>();
    private List<String> options1Account = new ArrayList<>();
    private List<String> options1Currency = new ArrayList<>();
    // options1Currency对应的中文币种列表
    private List<String> adOptions1CurrencyName = new ArrayList<>();

    private List<String> options1Firm = new ArrayList<>();

    private OptionsPickerView pvCategory;
    private OptionsPickerView pvType;
    private TimePickerView pvTime;
    private OptionsPickerView pvMember;
    private OptionsPickerView pvAccount;
    private OptionsPickerView pvCurrency;
    private OptionsPickerView pvFirm;

    private SuperTextView etMoney;
    private SuperTextView tvTime;
    private SuperTextView tvCategory;
    private SuperTextView tvType;
    private SuperTextView tvMember;
    private SuperTextView tvAccount;
    private SuperTextView tvCurrency;
    private SuperTextView tvFirm;
    private SuperTextView etItem;



    private ImageView ivBack;

    private Button btCommit;
    private Button btDelete;

    private Bill bill;
    private BillDao billDao = new BillDao(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill_pro);

        initView();
        initOptions();
        setValueOfView();

        //TODO:picker将会默认转到bill的内容对应的条目
        initIncomeExpensePicker();
        initTypePicker();
        initAccountTimePicker();
        initAccountMemberPicker();
        initAccountClassPicker();
        initCurrencyPicker();
        initFirmPicker();

        // SuperTextView监听器
        initSuperTextListener();
    }

    private void initView(){

        etMoney = (SuperTextView) findViewById(R.id.etMoney);
        tvCategory = (SuperTextView) findViewById(R.id.tvCategory);
        tvType = (SuperTextView) findViewById(R.id.tvType);
        tvMember = (SuperTextView) findViewById(R.id.tvMember);
        tvAccount = (SuperTextView) findViewById(R.id.tvAccount);
        tvCurrency = (SuperTextView) findViewById(R.id.tvCurrency);
        tvFirm = (SuperTextView) findViewById(R.id.tvFirm);
        etItem = (SuperTextView) findViewById(R.id.etItem);
        tvTime = (SuperTextView) findViewById(R.id.tvTime);

        ivBack = findViewById(R.id.ivBack);
        etMoney = findViewById(R.id.etMoney);
        etItem = findViewById(R.id.etItem);
        btCommit = findViewById(R.id.btCommit);
        btDelete = findViewById(R.id.btDelete);


        ivBack.setOnClickListener(this);
        btCommit.setOnClickListener(this);
        btDelete.setOnClickListener(this);
    }

    private void setValueOfView(){
        //TODO:小心options为空的情况，现在没有做任何异常处理
        Intent intent = getIntent();
        long id = -1;
        if(intent != null){
            id = intent.getLongExtra("id",-1);
        }
        if(id != -1){
            BillDao billDao = new BillDao(this);
            bill = billDao.queryBillById(id);
        }
        else{
            MyApp myApp = (MyApp)getApplication(); 
            bill = new Bill(myApp.getAccount());
//            Log.e(TAG,"现在bill的所有者为："+bill.getAccount());
            bill.setCategory(options1Category.get(0));
            bill.setType1(options1Type.get(0));
            bill.setType2(options2Type.get(0).get(0));
            bill.setTime(new Date().getTime());
            // leundo: bill的number可以为null
//            bill.setNumber(options1Member.get(0));
            bill.setAccount(options1Account.get(0));
            bill.setCurrency(options1Currency.get(0));
            // leundo: bill的firm可以为null
//            bill.setFirm(options1Firm.get(0));
            btCommit.setText("添加");
            btDelete.setText("取消");
        }

        etMoney.setCenterEditString(""+Math.abs(bill.getAmount()));
        tvCategory.setRightString(bill.getCategory());
        tvType.setRightString(bill.getType1()+"/"+bill.getType2());
        tvTime.setRightString(getTime(new Date(bill.getTime())));

//        tvMember.setRightString(bill.getNumber());
        if (bill.getNumber() != null) {
            tvMember.setRightString(bill.getNumber());
        } else {
            tvMember.setRightString("无");
        }

        tvAccount.setRightString(bill.getAccount());
        tvCurrency.setRightString(Bill.CURRENCY_NAME_TRANSLATION.get(bill.getCurrency()));

//        tvFirm.setRightString(bill.getFirm());
        if (bill.getFirm() != null) {
            tvFirm.setRightString(bill.getFirm());
        } else {
            tvFirm.setRightString("无");
        }

        if(bill.getItem() != null)
            etItem.setCenterEditString(bill.getItem());
    }

    private void initOptions(){
        //TODO:小心options为空的情况，现在没有做任何异常处理
        //账单类型：固定三项
//        options1Category.add("支出");
//        options1Category.add("收入");
//        options1Category.add("转账");
        options1Category.add(Bill.CATEGORY_EXPENDITURE_TITLE);
        options1Category.add(Bill.CATEGORY_INCOME_TITLE);
        options1Category.add(Bill.CATEGORY_TRANSFER_TITLE);

        //TODO:以账单类型三类分别列出账单的类别，现在采用的均为统一。
        //账单类别
        options1Type = billDao.queryType();
        for(String type1: options1Type){
            options2Type.add(billDao.queryType(type1));
        }
        //成员
        options1Member = billDao.queryNumber();
        options1Member.add(0, "无");

        //账户
        options1Account = billDao.queryAccount();
        //币种
        options1Currency = Bill.CURRENCY_LIST;
        for (String name : options1Currency) {
            adOptions1CurrencyName.add(Bill.CURRENCY_NAME_TRANSLATION.get(name));
        }


        //商家
        options1Firm = billDao.queryFirm();
        options1Firm.add(0, "无");


//        options1Type.add("餐饮");
//        options1Type.add("购物");
//        options1Type.add("日用");
//        ArrayList<String> options2Items_01 = new ArrayList<>();
//        options2Items_01.add("商场就餐");
//        options2Items_01.add("外卖");
//        options2Items_01.add("食堂");
//        ArrayList<String> options2Items_02 = new ArrayList<>();
//        options2Items_02.add("服饰");
//        options2Items_02.add("零食");
//        options2Items_02.add("电器");
//        ArrayList<String> options2Items_03 = new ArrayList<>();
//        options2Items_03.add("洗浴");
//        options2Items_03.add("日常护理");
//        options2Type.add(options2Items_01);
//        options2Type.add(options2Items_02);
//        options2Type.add(options2Items_03);
//
//        options1Member.add("本人");
//        options1Member.add("父亲");
//        options1Member.add("儿子");
//
//        options1Account.add("微信");
//        options1Account.add("支付宝");
//        options1Account.add("银行卡");
    }

    private void initIncomeExpensePicker(){
        pvCategory = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            // @return true：拦截，不消失；false：不拦截，消失
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Category.get(options1);
                bill.setCategory(options1Category.get(options1));
                tvCategory.setRightString(tx);
                return false;
            }

        })
                .setTitleText("收支类别")
                .setContentTextSize(20)//设置滚轮文字大小
//                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
//                .setSelectOptions(0, 1)//默认选中项
//                .setBgColor(ContextCompat.getColor(this,R.color.Morandi_white))
//                .setTitleBgColor(Color.DKGRAY)
//                .setTitleColor(Color.LTGRAY)
//                .setCancelColor(Color.YELLOW)
//                .setSubmitColor(Color.YELLOW)
//                .setTextColorCenter(Color.LTGRAY)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
//                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                .setLabels("省", "市", "区")
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                    }
                })
                .build();

//        pvOptions.setSelectOptions(1,1);

        pvCategory.setPicker(options1Category);

    }

    private void initTypePicker(){

        pvType = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Type.get(options1)+" / "+options2Type.get(options1).get(options2);
                bill.setType1(options1Type.get(options1));
                bill.setType2(options2Type.get(options1).get(options2));
                tvType.setRightString(tx);
                return false;
            }
        })
                .setTitleText("账单类别")
                .setContentTextSize(20)//设置滚轮文字大小
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                    }
                })
                .build();

        pvType.setPicker(options1Type,options2Type);

    }

    private void initAccountTimePicker(){

        Calendar start = Calendar.getInstance();
        Date start_d = new Date(100,0,0,0,0,0);
        start.setTime(start_d);
        Calendar end = Calendar.getInstance();

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelected(Date date, View v) {
//                Toast.makeText(AddBillProActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
//                Log.i("pvTime", "onTimeSelect");
                bill.setTime(date.getTime());
                tvTime.setRightString(getTime(date));

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
//                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setRangDate(start, end)
                .setDate(Calendar.getInstance())
                .setType(new boolean[]{true, true, true, true, false, false})
                .isDialog(false) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .setLineSpacingMultiplier(2.0f)
                .build();

//        Dialog mDialog = pvTime.getDialog();
//        if (mDialog != null) {
//
//            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.WRAP_CONTENT,
//                    Gravity.BOTTOM);
//            Log.e(TAG,"mDialog is not null");
//            params.leftMargin = 0;
//            params.rightMargin = 0;
//            pvTime.getDialogContainerLayout().setLayoutParams(params);
//
////            Window dialogWindow = mDialog.getWindow();
////            if (dialogWindow != null) {
//////                dialogWindow.setWindowAnimations(com.xuexiang.pickerview.R.style.picker_view_slide_anim);//修改动画样式
//////                dialogWindow.setLayout();
////                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
////                dialogWindow.setDimAmount(0.3f);
////            }
//        }
    }

    private void initAccountMemberPicker(){

        pvMember = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Member.get(options1);

                if (options1 == 0) {
                    bill.setNumber(null);
                } else {
                    bill.setNumber(options1Member.get(options1));
                }

                tvMember.setRightString(tx);
                return false;
            }
        })
                .setTitleText("成员")
                .setContentTextSize(20)//设置滚轮文字大小
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//                        Toast.makeText(AddBillProActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        pvMember.setPicker(options1Member);

    }

    private void initAccountClassPicker(){

        pvAccount = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Account.get(options1);
                bill.setAccount(options1Account.get(options1));
                tvAccount.setRightString(tx);
                return false;
            }
        })
                .setTitleText("成员选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//                        Toast.makeText(AddBillProActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        pvAccount.setPicker(options1Account);

    }

    private void initCurrencyPicker(){
        pvCurrency = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Currency.get(options1);
                bill.setCurrency(options1Currency.get(options1));
                tvCurrency.setRightString(Bill.CURRENCY_NAME_TRANSLATION.get(tx));
                return false;
            }
        })
                .setTitleText("币种选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//                        Toast.makeText(AddBillProActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        pvCurrency.setPicker(adOptions1CurrencyName);
    }

    private void initFirmPicker(){
        pvFirm = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Firm.get(options1);

                if (options1 == 0) {
                    bill.setFirm(null);
                } else {
                    bill.setFirm(options1Firm.get(options1));
                }


                tvFirm.setRightString(tx);
                return false;
            }
        })
                .setTitleText("币种选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
//                        Toast.makeText(AddBillProActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        pvFirm.setPicker(options1Firm);
    }

    @Override
    public void onClick(View view) {
        //TODO:Add more parameter
        boolean isNewBill;
        Log.d(TAG, "onClick: " + view);
        Log.d(TAG, "onClick: " + view.getClass());
        Log.d(TAG, "onClick: " + view.getId());
        switch (view.getId()){
            case R.id.tvCategory:
                pvCategory.show();
                break;
            case R.id.tvType:
                pvType.show();
                break;
            case R.id.tvTime:
                pvTime.show();
                break;
            case R.id.tvMember:
                pvMember.show();
                break;
            case R.id.tvAccount:
                pvAccount.show();
                break;
            case R.id.tvCurrency:
                pvCurrency.show();
                break;
            case R.id.tvFirm:
                pvFirm.show();
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.btCommit:
                isNewBill = (bill.getId() == -1);
                bill.setItem(etItem.getCenterEditValue());
                if (bill.getCategory().equals(Bill.CATEGORY_INCOME_TITLE)) {
                    bill.setAmount(Math.abs(Double.valueOf(etMoney.getCenterEditValue())));
                } else if (bill.getCategory().equals(Bill.CATEGORY_EXPENDITURE_TITLE)) {
                    bill.setAmount(-Math.abs(Double.valueOf(etMoney.getCenterEditValue())));
                } else {
                    bill.setAmount(Double.valueOf(etMoney.getCenterEditValue()));
                }
                Log.d(TAG, "onClick: " + bill.toString());
                if(isNewBill)
                    billDao.insertBill(bill);
                else
                    billDao.updateBill(bill);
                // 键盘放下
                MyApp.hideSoftInputFromWindow(AddBillProActivity.this);
                finish();
                break;
            case R.id.btDelete:
//                isNewBill = (bill.getId() == -1);
//                if(!isNewBill)
//                    billDao.deleteBill(bill);
//                // 键盘放下
//                MyApp.hideSoftInputFromWindow(AddBillProActivity.this);
                new MaterialDialog.Builder(AddBillProActivity.this)
                        .iconRes(R.drawable.ic_budget)
                        .title("删除")
                        .content("确定删除该账单？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive((dialog, which) -> deleteBillAndBack())
                        .cancelable(false)
                        .show();
                break;
        }
    }

    private void deleteBillAndBack(){
        Boolean isNewBill = (bill.getId() == -1);
        if(!isNewBill)
            billDao.deleteBill(bill);
        finish();
    }

    /** leundo:
     * SuperTextView 在 onClick 下无反应, 可测试
     * 因此设置监听器, 可能没有写在 onClick 那么优美, 但贵在有效
     */
    private void initSuperTextListener() {
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 键盘放下
                MyApp.hideSoftInputFromWindow(AddBillProActivity.this);
                // 夺取焦点
                tvTime.requestFocus();
                pvTime.show();
            }
        });

        tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 键盘放下
                MyApp.hideSoftInputFromWindow(AddBillProActivity.this);
                // 夺取焦点
                tvType.requestFocus();
                pvType.show();
            }
        });

        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 键盘放下
                MyApp.hideSoftInputFromWindow(AddBillProActivity.this);
                // 夺取焦点
                tvCategory.requestFocus();
                pvCategory.show();
            }
        });

        tvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 键盘放下
                MyApp.hideSoftInputFromWindow(AddBillProActivity.this);
                // 夺取焦点
                tvAccount.requestFocus();
                pvAccount.show();
            }
        });

        tvCurrency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 键盘放下
                MyApp.hideSoftInputFromWindow(AddBillProActivity.this);
                // 夺取焦点
                tvCurrency.requestFocus();
                pvCurrency.show();
            }
        });

        tvMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 键盘放下
                MyApp.hideSoftInputFromWindow(AddBillProActivity.this);
                // 夺取焦点
                tvMember.requestFocus();
                pvMember.show();
            }
        });

        tvFirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 键盘放下
                MyApp.hideSoftInputFromWindow(AddBillProActivity.this);
                // 夺取焦点
                tvFirm.requestFocus();
                pvFirm.show();
            }
        });
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH时");
        return format.format(date);
    }
}
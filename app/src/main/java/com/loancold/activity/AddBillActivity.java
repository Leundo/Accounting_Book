package com.loancold.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.loancold.item.BillItem;
import com.xuexiang.templateproject.MyApp;
import com.xuexiang.templateproject.R;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddBillActivity extends AppCompatActivity implements View.OnClickListener {

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
    private List<String> options1Firm = new ArrayList<>();

    private OptionsPickerView pvCategory;
    private OptionsPickerView pvType;
    private TimePickerView pvTime;
    private OptionsPickerView pvMember;
    private OptionsPickerView pvAccount;
    private OptionsPickerView pvCurrency;
    private OptionsPickerView pvFirm;

    private TextView tvCategory;
    private TextView tvType;
    private TextView tvTime;
    private TextView tvMember;
    private TextView tvAccount;
    private TextView tvCurrency;
    private TextView tvFirm;
    private RelativeLayout rlCategory;
    private RelativeLayout rlType;
    private RelativeLayout rlTime;
    private RelativeLayout rlMember;
    private RelativeLayout rlAccount;
    private RelativeLayout rlCurrency;
    private RelativeLayout rlFirm;
    private ImageView ivBack;
    private EditText  etMoney;
    private EditText  etItem;
    private Button btCommit;
    private Button btDelete;

    private Bill bill;
    private BillDao billDao = new BillDao(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);

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
    }

    private void initView(){
        tvCategory = findViewById(R.id.tvCategory);
        tvType = findViewById(R.id.tvType);
        tvTime = findViewById(R.id.tvTime);
        tvMember = findViewById(R.id.tvMember);
        tvAccount = findViewById(R.id.tvAccount);
        tvCurrency = findViewById(R.id.tvCurrency);
        tvFirm = findViewById(R.id.tvFirm);
        rlCategory = findViewById(R.id.rlCategory);
        rlType = findViewById(R.id.rlType);
        rlTime = findViewById(R.id.rlTime);
        rlMember = findViewById(R.id.rlMember);
        rlAccount = findViewById(R.id.rlAccount);
        rlCurrency = findViewById(R.id.rlCurrency);
        rlFirm = findViewById(R.id.rlFirm);
        ivBack = findViewById(R.id.ivBack);
        etMoney = findViewById(R.id.etMoney);
        etItem = findViewById(R.id.etItem);
        btCommit = findViewById(R.id.btCommit);
        btDelete = findViewById(R.id.btDelete);


        rlCategory.setOnClickListener(this);
        rlType.setOnClickListener(this);
        rlTime.setOnClickListener(this);
        rlMember.setOnClickListener(this);
        rlAccount.setOnClickListener(this);
        rlCurrency.setOnClickListener(this);
        rlFirm.setOnClickListener(this);
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
            bill.setNumber(options1Member.get(0));
            bill.setAccount(options1Account.get(0));
            bill.setCurrency(options1Currency.get(0));
            bill.setFirm(options1Firm.get(0));
            btCommit.setText("添加");
            btDelete.setText("取消");
        }
        etMoney.setText(""+bill.getAmount());
        tvCategory.setText(bill.getCategory());
        tvType.setText(bill.getType1()+"/"+bill.getType2());
        tvTime.setText(bill.getTimeAsDateString());
        tvMember.setText(bill.getNumber());
        tvAccount.setText(bill.getAccount());
        tvCurrency.setText(bill.getCurrency());
        tvFirm.setText(bill.getFirm());
        if(bill.getItem() != null)
            etItem.setText(bill.getItem());
    }

    private void initOptions(){
        //TODO:小心options为空的情况，现在没有做任何异常处理
        //账单类型：固定三项
        options1Category.add("支出");
        options1Category.add("收入");
        options1Category.add("转账");
        //TODO:以账单类型三类分别列出账单的类别，现在采用的均为统一。
        //账单类别
        options1Type = billDao.queryType();
        for(String type1: options1Type){
            options2Type.add(billDao.queryType(type1));
        }
        //成员
        options1Member = billDao.queryNumber();
        //账户
        options1Account = billDao.queryAccount();
        //币种
        options1Currency = Bill.CURRENCY_LIST;
        //商家
        options1Firm = billDao.queryFirm();


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
                tvCategory.setText(tx);
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
                        Toast.makeText(AddBillActivity.this, str, Toast.LENGTH_SHORT).show();
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
                tvType.setText(tx);
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
                        Toast.makeText(AddBillActivity.this, str, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AddBillActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                Log.i("pvTime", "onTimeSelect");
                bill.setTime(date.getTime());
                tvTime.setText(getTime(date));

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setRangDate(start, end)
                .setDate(Calendar.getInstance())
                .setType(new boolean[]{true, true, true, true, true, true})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .setLineSpacingMultiplier(2.0f)
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
//                dialogWindow.setWindowAnimations(com.xuexiang.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    private void initAccountMemberPicker(){

        pvMember = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Member.get(options1);
                bill.setNumber(options1Member.get(options1));
                tvMember.setText(tx);
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
                        Toast.makeText(AddBillActivity.this, str, Toast.LENGTH_SHORT).show();
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
                tvAccount.setText(tx);
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
                        Toast.makeText(AddBillActivity.this, str, Toast.LENGTH_SHORT).show();
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
                tvCurrency.setText(tx);
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
                        Toast.makeText(AddBillActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        pvCurrency.setPicker(options1Currency);
    }

    private void initFirmPicker(){
        pvFirm = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public boolean onOptionsSelect(View view, int options1, int options2, int options3) {
                //返回的分别是三个级别的选中位置
                String tx = options1Firm.get(options1);
                // TODO 添加null选项
                bill.setFirm(options1Firm.get(options1));

                tvCurrency.setText(tx);
                return false;
            }
        })
                .setTitleText("商家选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                        Toast.makeText(AddBillActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

        pvFirm.setPicker(options1Firm);
    }

    @Override
    public void onClick(View view) {
        //TODO:Add more parameter
        boolean isNewBill;
        switch (view.getId()){
            case R.id.rlCategory:
                pvCategory.show();
                break;
            case R.id.rlType:
                pvType.show();
                break;
            case R.id.rlTime:
                pvTime.show();
                break;
            case R.id.rlMember:
                pvMember.show();
                break;
            case R.id.rlAccount:
                pvAccount.show();
                break;
            case R.id.rlCurrency:
                pvCurrency.show();
                break;
            case R.id.rlFirm:
                pvFirm.show();
                break;
            case R.id.ivBack:
                finish();
                break;
            case R.id.btCommit:
                isNewBill = (bill.getId() == -1);
                bill.setItem(etItem.getText().toString());
                bill.setAmount( Double.valueOf(etMoney.getText().toString()) );
                if(isNewBill)
                    billDao.insertBill(bill);
                else
                    billDao.updateBill(bill);
                finish();
                break;
            case R.id.btDelete:
                isNewBill = (bill.getId() == -1);
                if(!isNewBill)
                    billDao.deleteBill(bill);
                finish();
                break;
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
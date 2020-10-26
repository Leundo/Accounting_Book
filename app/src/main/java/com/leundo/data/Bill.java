package com.leundo.data;

import com.xuexiang.templateproject.MyApp;
import com.xuexiang.templateproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * 存储记账记录的实例
 */

public class Bill {

    public static final String TYPE_DEFAULT_TITLE = "default";
    public static final String CATEGORY_EXPENDITURE_TITLE = "支出";
    public static final String CATEGORY_INCOME_TITLE = "收入";
    public static final String CATEGORY_TRANSFER_TITLE = "转帐";

    public static final String CURRENCY_CNY_NAME = "CNY"; // 人民币
    public static final String CURRENCY_USD_NAME = "USD"; // 美元
    public static final String CURRENCY_EUR_NAME = "EUR"; // 欧元
    public static final String CURRENCY_JPY_NAME = "JPY"; // 日元
    public static final String CURRENCY_GBP_NAME = "GBP"; // 英镑
    public static final String CURRENCY_AUD_NAME = "AUD"; // 澳元
    public static final String CURRENCY_CAD_NAME = "CAD"; // 加元
    public static final String CURRENCY_CHF_NAME = "CHF"; // 瑞士法郎
    public static final String CURRENCY_NZD_NAME = "NZD"; // 新西兰元
    public static final String CURRENCY_HKD_NAME = "HKD"; // 港元
    public static final String CURRENCY_KRW_NAME = "KRW"; // 韩元
    public static final String CURRENCY_INR_NAME = "INR"; // 印度卢比
    public static final String CURRENCY_KPW_NAME = "KPW"; // 朝鲜元
    public static final String CURRENCY_MOP_NAME = "MOP"; // 澳门元
    public static final String CURRENCY_TWD_NAME = "TWD"; // 新台币
    public static final String CURRENCY_RUB_NAME = "RUB"; // 俄罗斯卢布
    public static final String CURRENCY_SGD_NAME = "SGD"; // 新加坡元
    public static final String CURRENCY_THB_NAME = "THB"; // 泰铢
    public static final String CURRENCY_TRY_NAME = "TRY"; // 新土耳其里拉
    public static final String CURRENCY_VND_NAME = "VND"; // 越南盾
    public static final String CURRENCY_MYR_NAME = "MYR"; // 马来西亚林吉特
    public static final String CURRENCY_ZAR_NAME = "ZAR"; // 南非兰特
    public static final String CURRENCY_AED_NAME = "AED"; // 阿联酋迪拉姆
    public static final String CURRENCY_SAR_NAME = "SAR"; // 沙特里亚尔
    public static final String CURRENCY_HUF_NAME = "HUF"; // 匈牙利福林
    public static final String CURRENCY_PLN_NAME = "PLN"; // 波兰兹罗提
    public static final String CURRENCY_DKK_NAME = "DKK"; // 丹麦克朗
    public static final String CURRENCY_SEK_NAME = "SEK"; // 瑞典克朗
    public static final String CURRENCY_NOK_NAME = "NOK";// 挪威克朗
    public static final String CURRENCY_MXN_NAME = "MXN"; // 墨西哥比索



    // amount * EXCHANGE_RATE[currency] 为对应货币以人民币衡量的价值
    // 现在请使用直接的 getCNYAmount(), 内含错误管理
    public static final Map<String, Double> EXCHANGE_RATE = initExchangeRate();
    public static final List<String> CURRENCY_LIST = getCurrencyList();
//    public static final List<String> CURRENCY_CHINESE_LIST = getCurrencyChineseList();
    public static final Map<String, String> CURRENCY_NAME_TRANSLATION = getCurrencyNameList();

    // icon资源表
    public static Map<String, Integer> typeIcon = initTypeIcon();
    public static int typeDefaultIcon = R.drawable.pc_money;

    private static Map<String, Integer> initTypeIcon() {
        BillDao billDao = new BillDao(MyApp.getContext());
        return billDao.queryTypeIcon();
    }

    // 自定义插入后调用刷新icon表, leundo外的人一般不用管
    public static void updateTypeIcon() {
        BillDao billDao = new BillDao(MyApp.getContext());
        typeIcon = billDao.queryTypeIcon();
    }


    private static Map<String, Double> initExchangeRate() {
        Map<String, Double> exchangeRate = new HashMap<String, Double>();
        exchangeRate.put(CURRENCY_CNY_NAME,1.0);
        exchangeRate.put(CURRENCY_USD_NAME,6.7675);
        exchangeRate.put(CURRENCY_EUR_NAME,8.0107);
        exchangeRate.put(CURRENCY_JPY_NAME,0.0647);
        exchangeRate.put(CURRENCY_GBP_NAME,8.7395);
        exchangeRate.put(CURRENCY_AUD_NAME,4.9328);
        exchangeRate.put(CURRENCY_CAD_NAME,5.1253);
        exchangeRate.put(CURRENCY_CHF_NAME,7.4238);
        exchangeRate.put(CURRENCY_NZD_NAME,4.4731);
        exchangeRate.put(CURRENCY_HKD_NAME,0.8625);
        exchangeRate.put(CURRENCY_KRW_NAME,0.0059);
        exchangeRate.put(CURRENCY_INR_NAME,0.0905);
        exchangeRate.put(CURRENCY_KPW_NAME,0.0075);
        exchangeRate.put(CURRENCY_MOP_NAME,0.8373);
        exchangeRate.put(CURRENCY_TWD_NAME,0.2336);
        exchangeRate.put(CURRENCY_RUB_NAME,0.0878);
        exchangeRate.put(CURRENCY_SGD_NAME,4.9247);
        exchangeRate.put(CURRENCY_THB_NAME,0.2134);
        exchangeRate.put(CURRENCY_TRY_NAME,0.8396);
        exchangeRate.put(CURRENCY_VND_NAME,0.00029);
        exchangeRate.put(CURRENCY_MYR_NAME,1.6087);
        exchangeRate.put(CURRENCY_ZAR_NAME,0.4129);
        exchangeRate.put(CURRENCY_AED_NAME,1.8199);
        exchangeRate.put(CURRENCY_SAR_NAME,1.7824);
        exchangeRate.put(CURRENCY_HUF_NAME,0.0217);
        exchangeRate.put(CURRENCY_PLN_NAME,1.7367);
        exchangeRate.put(CURRENCY_DKK_NAME,1.0655);
        exchangeRate.put(CURRENCY_SEK_NAME,0.7642);
        exchangeRate.put(CURRENCY_NOK_NAME,0.7237);
        exchangeRate.put(CURRENCY_MXN_NAME,0.3205);


        return exchangeRate;
    }

    private static List<String> getCurrencyList() {
        List<String>  currencyList = new ArrayList<>();
        currencyList.add(CURRENCY_CNY_NAME);
        currencyList.add(CURRENCY_USD_NAME);
        currencyList.add(CURRENCY_EUR_NAME);
        currencyList.add(CURRENCY_JPY_NAME);
        currencyList.add(CURRENCY_GBP_NAME);
        currencyList.add(CURRENCY_AUD_NAME);
        currencyList.add(CURRENCY_CAD_NAME);
        currencyList.add(CURRENCY_CHF_NAME);
        currencyList.add(CURRENCY_NZD_NAME);
        currencyList.add(CURRENCY_HKD_NAME);
        currencyList.add(CURRENCY_KRW_NAME);
        currencyList.add(CURRENCY_INR_NAME);
        currencyList.add(CURRENCY_KPW_NAME);
        currencyList.add(CURRENCY_MOP_NAME);
        currencyList.add(CURRENCY_TWD_NAME);
        currencyList.add(CURRENCY_RUB_NAME);
        currencyList.add(CURRENCY_SGD_NAME);
        currencyList.add(CURRENCY_THB_NAME);
        currencyList.add(CURRENCY_TRY_NAME);
        currencyList.add(CURRENCY_VND_NAME);
        currencyList.add(CURRENCY_MYR_NAME);
        currencyList.add(CURRENCY_ZAR_NAME);
        currencyList.add(CURRENCY_AED_NAME);
        currencyList.add(CURRENCY_SAR_NAME);
        currencyList.add(CURRENCY_HUF_NAME);
        currencyList.add(CURRENCY_PLN_NAME);
        currencyList.add(CURRENCY_DKK_NAME);
        currencyList.add(CURRENCY_SEK_NAME);
        currencyList.add(CURRENCY_NOK_NAME);
        currencyList.add(CURRENCY_MXN_NAME);

        return currencyList;
    }

//    private static List<String> getCurrencyChineseList() {
//        List<String>  currencyNameList = new ArrayList<>();
//        currencyNameList.add("人民币");
//        currencyNameList.add(CURRENCY_USD_NAME,"美元");
//        currencyNameList.add(CURRENCY_EUR_NAME,"欧元");
//        currencyNameList.add(CURRENCY_JPY_NAME,"日元");
//        currencyNameList.add(CURRENCY_GBP_NAME,"英镑");
//        currencyNameList.add(CURRENCY_AUD_NAME,"澳元");
//        currencyNameList.add(CURRENCY_CAD_NAME,"加元");
//        currencyNameList.add(CURRENCY_CHF_NAME,"瑞士法郎");
//        currencyNameList.add(CURRENCY_NZD_NAME,"新西兰元");
//        currencyNameList.add(CURRENCY_HKD_NAME,"港元");
//        currencyNameList.add(CURRENCY_KRW_NAME,"韩元");
//        currencyNameList.add(CURRENCY_INR_NAME,"印度卢比");
//        currencyNameList.add(CURRENCY_KPW_NAME,"朝鲜元");
//        currencyNameList.add(CURRENCY_MOP_NAME,"澳门元");
//        currencyNameList.add(CURRENCY_TWD_NAME,"新台币");
//        currencyNameList.add(CURRENCY_RUB_NAME,"俄罗斯卢布");
//        currencyNameList.add(CURRENCY_SGD_NAME,"新加坡元");
//        currencyNameList.add(CURRENCY_THB_NAME,"泰铢");
//        currencyNameList.add(CURRENCY_TRY_NAME,"新土耳其里拉");
//        currencyNameList.add(CURRENCY_VND_NAME,"越南盾");
//        currencyNameList.add(CURRENCY_MYR_NAME,"马来西亚林吉特");
//        currencyNameList.add(CURRENCY_ZAR_NAME,"南非兰特");
//        currencyNameList.add(CURRENCY_AED_NAME,"阿联酋迪拉姆");
//        currencyNameList.add(CURRENCY_SAR_NAME,"沙特里亚尔");
//        currencyNameList.add(CURRENCY_HUF_NAME,"匈牙利福林");
//        currencyNameList.add(CURRENCY_PLN_NAME,"波兰兹罗提");
//        currencyNameList.add(CURRENCY_DKK_NAME,"丹麦克朗");
//        currencyNameList.add(CURRENCY_SEK_NAME,"瑞典克朗");
//        currencyNameList.add(CURRENCY_NOK_NAME,"挪威克朗");
//        currencyNameList.add(CURRENCY_MXN_NAME,"墨西哥比索");
//
//        return currencyName;
//    }

    private static Map<String, String> getCurrencyNameList() {
        Map<String, String> currencyName = new HashMap<String, String>();
        currencyName.put(CURRENCY_CNY_NAME,"人民币");
        currencyName.put(CURRENCY_USD_NAME,"美元");
        currencyName.put(CURRENCY_EUR_NAME,"欧元");
        currencyName.put(CURRENCY_JPY_NAME,"日元");
        currencyName.put(CURRENCY_GBP_NAME,"英镑");
        currencyName.put(CURRENCY_AUD_NAME,"澳元");
        currencyName.put(CURRENCY_CAD_NAME,"加元");
        currencyName.put(CURRENCY_CHF_NAME,"瑞士法郎");
        currencyName.put(CURRENCY_NZD_NAME,"新西兰元");
        currencyName.put(CURRENCY_HKD_NAME,"港元");
        currencyName.put(CURRENCY_KRW_NAME,"韩元");
        currencyName.put(CURRENCY_INR_NAME,"印度卢比");
        currencyName.put(CURRENCY_KPW_NAME,"朝鲜元");
        currencyName.put(CURRENCY_MOP_NAME,"澳门元");
        currencyName.put(CURRENCY_TWD_NAME,"新台币");
        currencyName.put(CURRENCY_RUB_NAME,"俄罗斯卢布");
        currencyName.put(CURRENCY_SGD_NAME,"新加坡元");
        currencyName.put(CURRENCY_THB_NAME,"泰铢");
        currencyName.put(CURRENCY_TRY_NAME,"新土耳其里拉");
        currencyName.put(CURRENCY_VND_NAME,"越南盾");
        currencyName.put(CURRENCY_MYR_NAME,"马来西亚林吉特");
        currencyName.put(CURRENCY_ZAR_NAME,"南非兰特");
        currencyName.put(CURRENCY_AED_NAME,"阿联酋迪拉姆");
        currencyName.put(CURRENCY_SAR_NAME,"沙特里亚尔");
        currencyName.put(CURRENCY_HUF_NAME,"匈牙利福林");
        currencyName.put(CURRENCY_PLN_NAME,"波兰兹罗提");
        currencyName.put(CURRENCY_DKK_NAME,"丹麦克朗");
        currencyName.put(CURRENCY_SEK_NAME,"瑞典克朗");
        currencyName.put(CURRENCY_NOK_NAME,"挪威克朗");
        currencyName.put(CURRENCY_MXN_NAME,"墨西哥比索");

        return currencyName;
    }

    private long id; // 主键
    private String account; // 账户
    private String category; // 支出，收入，转帐

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    private String type1; // 一级类别
    private String type2; // 二级类别
    private double amount; // 金额
    private long time; // 时间戳，距离1970的微秒数
    private String number; // 成员
    private String firm; // 商家
    private String item; // 项目
    private String remark; // 备注
    private String currency; // 币种

    public Bill(long id, String account, String category, String type1, String type2, double amount, long time, String number, String firm, String item, String remark, String currency) {
        this.id = id;
        this.account = account;
        this.category = category;
        this.type1 = type1;
        this.type2 = type2;
        this.amount = amount;
        this.time = time;
        this.number = number;
        this.firm = firm;
        this.item = item;
        this.remark = remark;
        this.currency = currency;

    }


//    public Bill(long id, String account, String category, String type1, String type2, double amount, long time, String currency) {
//        this.id = id;
//        this.account = account;
//        this.category = category;
//        this.type1 = type1;
//        this.type2 = type2;
//        this.amount = amount;
//        this.time = time;
//        this.number = null;
//        this.firm = null;
//        this.item = null;
//        this.remark = null;
//        this.currency = currency;
//
//    }
//
//    public Bill(String account, double amount) {
//        this.id = 0;
//        this.account = account;
//        this.category = Bill.CATEGORY_EXPENDITURE_TITLE;
//        this.type1 = Bill.TYPE_DEFAULT_TITLE;
//        this.type2 = Bill.TYPE_DEFAULT_TITLE;
//        this.amount = amount;
//        this.time = new Date().getTime();
//        this.number = null;
//        this.firm = null;
//        this.item = null;
//        this.remark = null;
//        this.currency = Bill.CURRENCY_CNY_NAME;
//
//    }

    public Bill(String account) {
        this.id = -1;
        this.account = account;
        this.category = Bill.CATEGORY_EXPENDITURE_TITLE;
        this.type1 = Bill.TYPE_DEFAULT_TITLE;
        this.type2 = Bill.TYPE_DEFAULT_TITLE;
        this.amount = 0;
        this.time = new Date().getTime();
        this.number = null;
        this.firm = null;
        this.item = null;
        this.remark = null;
        this.currency = Bill.CURRENCY_CNY_NAME;
    }

    public long getId() {
        return id;
    }

    public String getAccount() {
        return account;
    }

    public String getCategory() {
        return category;
    }

    public String getType1() {
        return type1;
    }

    public String getType2() {
        return type2;
    }

    public double getAmount() {
        return amount;
    }

    public long getTime() {
        return time;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNumber() {
        return number;
    }

    public String getFirm() {
        return firm;
    }

    public String getItem() {
        return item;
    }

    public String getRemark() {
        return remark;
    }

    // 返回yyyy-MM-dd HH:mm:ss形式的日期
    public String getTimeAsDateString() {

        Date date = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return formatter.format(date);
    }


    public String toString() {
        String string = "{id: " + id +
                ", account: " + account +
                ", category: " + category +
                ", type1: " + type1 +
                ", type2: " + type2 +
                ", amount: " + amount +
                ", time: " + this.getTimeAsDateString() +
                ", number: " + number +
                ", firm: " + firm +
                ", item: " + item +
                ", currency: " + currency +
                "}";
        return string;
    }

    public String toSimpleString() {
        String string = "{id: " + id +
                ", amount: " + amount +
                ", time: " + this.getTimeAsDateString() +
                ", currency: " + currency +
                "}";
        return string;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTime(long time) {
        this.time = time;
    }

    // 返回转化为人民币的金额
    public double getCNYAmount() {
        try {
            if(!Bill.EXCHANGE_RATE.containsKey(currency)) {
                throw new Exception("不存在 " + currency + " 的汇率");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount * Bill.EXCHANGE_RATE.get(currency);
    }

    // 测试数据
    public static ArrayList<Bill> getTestData() {
        ArrayList<Bill> data = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        // calendar.set的month参数是从零开始的，即0为1月，年和日是从1开始
        calendar.set(2020, 8, 18);
        data.add(new Bill(1,"支付宝", Bill.CATEGORY_EXPENDITURE_TITLE, "餐饮", "早餐", -20.00, calendar.getTimeInMillis(), "山姆", "饭堂", null, null, Bill.CURRENCY_CNY_NAME));

        calendar.set(2020, 8, 18);
        data.add(new Bill(1,"支付宝", Bill.CATEGORY_INCOME_TITLE, "收入", "工资", 5000.00, calendar.getTimeInMillis(), "父亲", null, null, null, Bill.CURRENCY_USD_NAME));

        calendar.set(2020, 8, 13);
        data.add(new Bill(2,"支付宝", Bill.CATEGORY_EXPENDITURE_TITLE, "餐饮", "午饭", -20.00, calendar.getTimeInMillis(), "妻子", "麦当劳", null, null, Bill.CURRENCY_CNY_NAME));

        calendar.set(2020, 7, 25);
        data.add(new Bill(3,"支付宝", Bill.CATEGORY_EXPENDITURE_TITLE, "医疗", "手术", -500.50, calendar.getTimeInMillis(), "山姆", null, null, null, Bill.CURRENCY_CNY_NAME));

        calendar.set(2020, 7, 21);
        data.add(new Bill(4,"支付宝", Bill.CATEGORY_EXPENDITURE_TITLE, "餐饮", "早餐", -5.00, calendar.getTimeInMillis(), "母亲", null, null, null, Bill.CURRENCY_CNY_NAME));

        calendar.set(2020, 7, 18);
        data.add(new Bill(1,"支付宝", Bill.CATEGORY_INCOME_TITLE, "收入", "工资", 4800.00, calendar.getTimeInMillis(), "山姆", null, null, null, Bill.CURRENCY_USD_NAME));


        calendar.set(2020, 7, 17);
        data.add(new Bill(4,"支付宝", Bill.CATEGORY_EXPENDITURE_TITLE, "餐饮", "早餐", -6.00, calendar.getTimeInMillis(), "山姆", null, null, null, Bill.CURRENCY_CNY_NAME));

        calendar.set(2020, 7, 16);
        data.add(new Bill(4,"微信支付", Bill.CATEGORY_EXPENDITURE_TITLE, "购物", "日用品", -66.00, calendar.getTimeInMillis(), "母亲", "益田", null, null, Bill.CURRENCY_CNY_NAME));

        calendar.set(2020, 7, 15);
        data.add(new Bill(4,"现金", Bill.CATEGORY_EXPENDITURE_TITLE, "娱乐", "游戏厅", -45.00, calendar.getTimeInMillis(), "妻子", null, null, null, Bill.CURRENCY_CNY_NAME));

        calendar.set(2020, 6, 25);
        data.add(new Bill(4,"支付宝", Bill.CATEGORY_EXPENDITURE_TITLE, "医疗", "手术", -1050.00, calendar.getTimeInMillis(), "山姆", null, null, null, Bill.CURRENCY_CNY_NAME));

        return data;
    }

    public enum FilteredAttribute {
        number, firm, category, currency, item, type1, account
    }

    // 过滤后，账单中attribute不为filteredName的都被删除(为null的亦会被删除)
    // filter(bills, .number, "父亲")，运行后只会留下number为"父亲"的账单记录
    // 不能过滤type2
    public static void filter(ArrayList<Bill> bills, FilteredAttribute attribute, String filteredName) {
        Iterator<Bill> iterator = bills.iterator();
        while (iterator.hasNext()) {
            Bill bill = iterator.next();
            switch (attribute) {
                case firm:
                    if (bill.getFirm() == null || !bill.getFirm().equals(filteredName)) {
                        iterator.remove();
                    }
                    break;
                case item:
                    if (bill.getItem() == null || !bill.getItem().equals(filteredName)) {
                        iterator.remove();
                    }
                    break;
                case type1:
                    if (bill.getType1() == null || !bill.getType1().equals(filteredName)) {
                        iterator.remove();
                    }
                    break;
                case number:
                    if (bill.getNumber() == null || !bill.getNumber().equals(filteredName)) {
                        iterator.remove();
                    }
                    break;
                case category:
                    if (bill.getCategory() == null || !bill.getCategory().equals(filteredName)) {
                        iterator.remove();
                    }
                    break;
                case currency:
                    if (bill.getCurrency() == null || !bill.getCurrency().equals(filteredName)) {
                        iterator.remove();
                    }
                    break;
                case account:
                    if (bill.getAccount() == null || bill.getAccount() != filteredName) {
                        iterator.remove();
                    }
                    break;
            }
        }
    }

    // 过滤type2用这个
    public static void filterByType2(ArrayList<Bill> bills, String type1FilteredName, String type2FilteredName) {
        Iterator<Bill> iterator = bills.iterator();
        while (iterator.hasNext()) {
            Bill bill = iterator.next();
            if (!bill.getType1().equals(type1FilteredName) || !bill.getType2().equals(type2FilteredName)) {
                iterator.remove();
            }
        }
    }

}


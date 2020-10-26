/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
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

package com.xuexiang.templateproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import androidx.multidex.MultiDex;

import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.xuexiang.templateproject.utils.sdkinit.ANRWatchDogInit;
import com.xuexiang.templateproject.utils.sdkinit.UMengInit;
import com.xuexiang.templateproject.utils.sdkinit.XBasicLibInit;
import com.xuexiang.templateproject.utils.sdkinit.XUpdateInit;

import java.util.Calendar;
import java.util.Date;

/**
 * @author xuexiang
 * @since 2018/11/7 下午1:12
 */
public class MyApp extends Application {

    private static Context context;

    private SharedPreferences userInfo;
    private SharedPreferences.Editor userInfoEditor;

    private boolean firstLogin;
    private String account;


    private double budget;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //解决4.x运行崩溃的问题
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLibs();
        context = getApplicationContext();

        initUserData();
        if(firstLogin){
            addTestData();
            addDefaultIcon();
        }
    }

    /**
     * 初始化基础库
     */
    private void initLibs() {
        XBasicLibInit.init(this);

        XUpdateInit.init(this);

        //运营统计数据运行时不初始化
        if (!MyApp.isDebug()) {
            UMengInit.init(this);
        }

        //ANR监控
        ANRWatchDogInit.init();
    }

    public void initUserData(){
        userInfo = getSharedPreferences("user",MODE_PRIVATE );
        userInfoEditor = userInfo.edit();
        account = userInfo.getString("account", "山姆");
        budget = userInfo.getFloat("budget", 2000);
        firstLogin = userInfo.getBoolean("firstLogin", true);
        userInfoEditor.putBoolean("firstLogin", false);
        userInfoEditor.apply();
    }

    // 编写人: leundo
    private void addDefaultIcon() {
        BillDao billDao = new BillDao(getContext());
        billDao.insertTypeIcon("餐饮", R.drawable.pc_food);
        billDao.insertTypeIcon("购物", R.drawable.pc_shopping);
        billDao.insertTypeIcon("日用", R.drawable.pc_home);
        billDao.insertTypeIcon("医疗", R.drawable.pc_hospital);
        billDao.insertTypeIcon("收入", R.drawable.pc_hundred);
        billDao.insertTypeIcon("娱乐", R.drawable.pc_entertainment);
        billDao.insertTypeIcon("默认", R.drawable.pc_money);
    }

    private void addTestData() {
        BillDao billDao = new BillDao(getContext());
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
        billDao.insertType("日常");
        billDao.insertType("日常","洗浴");
        billDao.insertType("日常","日常护理");
        billDao.insertType("医疗");
        billDao.insertType("医疗","手术");
        billDao.insertType("娱乐");
        billDao.insertType("娱乐","游戏厅");
        billDao.insertAccount("支付宝");
        billDao.insertAccount("现金");
        billDao.insertAccount("微信支付");
        billDao.insertNumber("山姆");
        billDao.insertNumber("父亲");
        billDao.insertNumber("母亲");
        billDao.insertNumber("妻子");
        billDao.insertFirm("饭堂");
        billDao.insertFirm("麦当劳");
        billDao.insertFirm("益田");
    }



    /**
     * @return 当前app是否是调试开发模式
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public static Context getContext(){
        return context;
    }


    public void setAccount(String account) {
        this.account = account;
        userInfoEditor.putString("account", account);
        userInfoEditor.commit();
    }
    public String getAccount() {
        return account;
    }

    public double getBudget() {
        return budget;
    }

    public static boolean hideSoftInputFromWindow(Activity activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                return imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
            }
        }
        return false;
    }
}

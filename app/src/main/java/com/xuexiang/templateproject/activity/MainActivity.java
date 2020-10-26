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

package com.xuexiang.templateproject.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.leundo.account.AccountActivity;
import com.leundo.budget.BudgetModel;
import com.leundo.customization.CustomizationSuperActivity;
import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.leundo.exchange.ExchangeActivity;
import com.leundo.flow.activity.FlowSettingActivity;
import com.leundo.fragment.FlowFragment;
import com.leundo.secure.SetCipherActivity;
import com.loancold.fragment.OverviewFragment;
import com.xuexiang.templateproject.core.BaseActivity;
import com.xuexiang.templateproject.core.BaseFragment;
import com.xuexiang.templateproject.fragment.AboutFragment;
import com.xuexiang.templateproject.fragment.SettingsFragment;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.utils.Utils;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.utils.ThemeUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.ClickUtils;
import com.xuexiang.xutil.common.CollectionUtils;
import com.xuexiang.xutil.display.Colors;
import com.zzn.chart.project.fragment.ZznPieFragment;

import butterknife.BindView;

/**
 * 程序主页面,只是一个简单的Tab例子
 *
 * @author xuexiang
 * @since 2019-07-07 23:53
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener, ClickUtils.OnClick2ExitListener, Toolbar.OnMenuItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    /**
     * 底部导航栏
     */
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;
    /**
     * 侧边栏
     */
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private String[] mTitles;

    private OverviewFragment overviewFragment;
    private FlowFragment flowFragment;
    private ZznPieFragment zznPieFragment;

    /**
     * leundo
     * drawer的位置，用来做监听
     */
    private int drawerPosition = 0;
    private final int OVERVIEW_POSITION = 0;
    private final int FLOW_POSITION = 1;
    private final int CHART_POSITION = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        请不要直接插入自己的fragment相关的逻辑，请先封装成一个函数，方便区别代码的编写者。
        另外fragment的设置逻辑请不要放在这，应该在fragment的构建函数内实现。
         */

        initViews();

        initListeners();

        Bill.updateTypeIcon();

    }

    @Override
    protected boolean isSupportSlideBack() {
        return false;
    }

    /*
    编写者: 模版
    新建fragment请在这里实现
    例如 flowFragment = new FlowFragment(this);
     */
    private void initViews() {
        // mTitles = {"主页","流水","图表"}
        mTitles = ResUtils.getStringArray(R.array.home_titles);
        title.setText(mTitles[0]);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.menu_main);
        /**
         * leundo
         * 设置按钮, 预算按钮
         */
        toolbar.inflateMenu(R.menu.meun_setting);
        toolbar.inflateMenu(R.menu.menu_budget);
        toolbar.setOnMenuItemClickListener(this);

        // 设置左侧菜单的头
//        initHeader();

        /*
        这里插入我们的fragment初始化代码
        flowFragment: 流水
         */
        overviewFragment = new OverviewFragment(this);

        flowFragment = new FlowFragment(this);

        zznPieFragment = new ZznPieFragment(this);


        //主页内容填充
        BaseFragment[] fragments = new BaseFragment[]{
                overviewFragment,
                flowFragment,
                zznPieFragment,
        };
        FragmentAdapter<BaseFragment> adapter = new FragmentAdapter<>(getSupportFragmentManager(), fragments);
        viewPager.setOffscreenPageLimit(mTitles.length - 1);
        viewPager.setAdapter(adapter);

        // 更新toolbar按钮, 动态显示
        updateMenuButton(OVERVIEW_POSITION);
    }

    /*
    编写者: 模版
     */
    private void initHeader() {
        navView.setItemIconTintList(null);
        View headerView = navView.getHeaderView(0);
        LinearLayout navHeader = headerView.findViewById(R.id.nav_header);
        RadiusImageView ivAvatar = headerView.findViewById(R.id.iv_avatar);
        TextView tvAvatar = headerView.findViewById(R.id.tv_avatar);
        TextView tvSign = headerView.findViewById(R.id.tv_sign);

        if (Utils.isColorDark(ThemeUtils.resolveColor(this, R.attr.colorAccent))) {
            tvAvatar.setTextColor(Colors.WHITE);
            tvSign.setTextColor(Colors.WHITE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar.setImageTintList(ResUtils.getColors(R.color.xui_config_color_white));
            }
        } else {
            tvAvatar.setTextColor(ThemeUtils.resolveColor(this, R.attr.xui_config_color_title_text));
            tvSign.setTextColor(ThemeUtils.resolveColor(this, R.attr.xui_config_color_explain_text));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar.setImageTintList(ResUtils.getColors(R.color.xui_config_color_gray_3));
            }
        }

        // TODO: 2019-10-09 初始化数据
        ivAvatar.setImageResource(R.drawable.ic_default_head);
        tvAvatar.setText(R.string.app_name);
        tvSign.setText("这个家伙很懒，什么也没有留下～～");
        navHeader.setOnClickListener(this);
    }

    /*
    编写者: 模版
     */
    protected void initListeners() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //侧边栏点击事件
        navView.setNavigationItemSelectedListener(menuItem -> {
            if (menuItem.isCheckable()) {
                drawerLayout.closeDrawers();
                return handleNavigationItemSelected(menuItem);
            } else {
                switch (menuItem.getItemId()) {
                    case R.id.nav_key:
//                        openNewPage(SettingsFragment.class);
                        Intent intent4 = new Intent(this, SetCipherActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.nav_about:
                        openNewPage(AboutFragment.class);
                        break;
                    case R.id.nav_exchange:
                        Intent intent = new Intent(this, ExchangeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_customization:
                        Intent intent2 = new Intent(this, CustomizationSuperActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.nav_account:
                        Intent intent3 = new Intent(this, AccountActivity.class);
                        startActivity(intent3);
                        break;
                    default:
                        XToastUtils.toast("点击了:" + menuItem.getTitle());
                        break;
                }
            }
            return true;
        });

        //主页事件监听
        viewPager.addOnPageChangeListener(this);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        请不要直接插入自己的fragment相关的逻辑，请先封装成一个函数，方便区别代码的编写者。
        如 FlowFragmentOnResume: 流水相关的onResume
         */
        OverviewFragmentOnResume();
        FlowFragmentOnResume();
        zznPieFragmentOnResume();
    }

    /*
    编写者: loancold
     */
    private void OverviewFragmentOnResume() {
        if (overviewFragment != null) {
            overviewFragment.refreshBill();
        }

    }

    /*
    编写者: leundo
     */
    private void FlowFragmentOnResume() {
        if (flowFragment != null) {
            FlowSettingActivity.refreshData();
            flowFragment.refresh();
        }

    }

    /*
    编写者: zzn
     */
    private void zznPieFragmentOnResume() {
        if (zznPieFragment != null) {
            zznPieFragment.upDate();
        }

    }

    /**
     * 处理侧边栏点击事件
     *
     * @param menuItem
     * @return
     */
    private boolean handleNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(mTitles, menuItem.getTitle());
        if (index != -1) {
//            toolbar.setTitle(menuItem.getTitle());
            title.setText(menuItem.getTitle());
            viewPager.setCurrentItem(index, false);
            return true;
        }
        return false;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_privacy:
//                Utils.showPrivacyDialog(this, null);
                Toast.makeText(this,"隐私协议暂未创建",Toast.LENGTH_SHORT);
                break;
            case R.id.action_settings:
                /**
                 * leundo
                 * 仅在流水界面才进入流水设置
                 */
                if (drawerPosition == FLOW_POSITION) {
                    Intent intent = new Intent(MainActivity.this, FlowSettingActivity.class);
                    this.startActivityForResult(intent, 1);
                }
            case R.id.action_budget:
                if (drawerPosition == OVERVIEW_POSITION) {
                    showBudgetDialog();
                }
            default:
                break;
        }
        return false;
    }

    // 设置预算对话框
    private void showBudgetDialog() {
        new MaterialDialog.Builder(MainActivity.this)
                .iconRes(R.drawable.ic_budget)
                .title("设置预算")
                .content("设置您的月支出预算")
                .inputType(
                        InputType.TYPE_CLASS_NUMBER
                                | InputType.TYPE_NUMBER_FLAG_DECIMAL)
                .input(
                        "0",
                        String.valueOf(BudgetModel.budget),
                        false,
                        ((dialog, input) -> setBudget(input.toString())))
                .positiveText("确定")
                .negativeText("取消")
//                .onPositive((dialog, which) -> XToastUtils.toast("你输入了:" + dialog.getInputEditText().getText().toString()))
                .cancelable(false)
                .show();
    }

    private void setBudget(String input) {
        BillDao billDao = new BillDao(MainActivity.this);
        double budget = 0;
        if (input.equals("")) {

        } else {
            budget = Double.parseDouble(input);
        }
        billDao.insertOrUpdateNum(BudgetModel.NUM_TABLE_BUDGET_TITLE, budget);
        BudgetModel.updateBudget();

        // 刷新UI
        OverviewFragmentOnResume();
        DialogLoader.getInstance().showTipDialog(
                MainActivity.this,
                "提示",
                "设置预算成功",
                "确定");

    }

    @SingleClick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header:
                XToastUtils.toast("点击头部！");
                break;
            default:
                break;
        }
    }

    //=============ViewPager===================//

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        MenuItem item = bottomNavigation.getMenu().getItem(position);
        title.setText(item.getTitle());
//        toolbar.setTitle(item.getTitle());
        item.setChecked(true);
        // NOTICE: 10/25/2020 李程浩 增加了逻辑，在页面通过滑动移动的时候会改变icon
        drawerPosition = position;
        updateMenuButton(position);
        Log.e("main","scroll position:"+position);

        updateSideNavStatus(item);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    //================Navigation================//

    /**
     * 底部导航栏点击事件
     *
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int index = CollectionUtils.arrayIndexOf(mTitles, menuItem.getTitle());
        if (index != -1) {
            title.setText(menuItem.getTitle());
//            toolbar.setTitle(menuItem.getTitle());
            viewPager.setCurrentItem(index, false);

            drawerPosition = index;
            updateSideNavStatus(menuItem);
            updateMenuButton(index);
            return true;
        }
        return false;
    }

    /**
     * leundo
     */
    private void updateMenuButton(int index) {
        Menu menu = toolbar.getMenu();
        if (index == FLOW_POSITION) {
            menu.findItem(R.id.action_privacy).setVisible(false);
            menu.findItem(R.id.action_budget).setVisible(false);
            menu.findItem(R.id.action_settings).setVisible(true);
        } else if (index == OVERVIEW_POSITION) {
            menu.findItem(R.id.action_privacy).setVisible(false);
            menu.findItem(R.id.action_budget).setVisible(true);
            menu.findItem(R.id.action_settings).setVisible(false);
        } else if (index == CHART_POSITION) {
            menu.findItem(R.id.action_privacy).setVisible(false);
            menu.findItem(R.id.action_budget).setVisible(false);
            menu.findItem(R.id.action_settings).setVisible(false);
        }
    }

    /**
     * 更新侧边栏菜单选中状态
     *
     * @param menuItem
     */
    private void updateSideNavStatus(MenuItem menuItem) {
        MenuItem side = navView.getMenu().findItem(menuItem.getItemId());
        if (side != null) {
            side.setChecked(true);
        }
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click(2000, this);
        }
        return true;
    }

    @Override
    public void onRetry() {
        SnackbarUtils.Short(toolbar, "再按一次退出程序").info().show();
    }

    @Override
    public void onExit() {
        XUtil.exitApp();
    }


}

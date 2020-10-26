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

package com.leundo.secure;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.edittext.verify.VerifyCodeEditText;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.ClickUtils;

public class SetNumCipherActivity extends AppCompatActivity implements VerifyCodeEditText.OnInputListener, ClickUtils.OnClick2ExitListener {

    ImageView backButton;
    RelativeLayout block;
    LinearLayout linearLayout;
    private TextView tipTextView;
    private VerifyCodeEditText vcet;
    private int state = STATE_VERIFICATION;
    static private final int STATE_VERIFICATION = 0;
    static private final int STATE_FIRST_INPUT = 1;
    static private final int STATE_SECOND_INPUT = 2;
    static private final int STATE_END = 3;


    private String cipher = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setnumcipher);
        findView();
        initTextView();
        initButton();
        firstSet();
    }

    @Override
    public void onComplete(String input) {
        if (state == STATE_VERIFICATION) {
            if (CipherModel.isCipherCorrect(CipherModel.NUMBER_CIPHER_TITLE, input)) {
                tipTextView.setText("输入新的密码");
                state = STATE_FIRST_INPUT;
            } else {
                tipTextView.setText("密码错误，请再次输入");
            }
        } else if (state == STATE_FIRST_INPUT) {
            cipher = input;
            tipTextView.setText("再次输入密码");
            state = STATE_SECOND_INPUT;
        } else if (state == STATE_SECOND_INPUT) {
            if (input.equals(cipher)) {
                CipherModel.insertCipher(CipherModel.NUMBER_CIPHER_TITLE, cipher);
                tipTextView.setText("密码设置成功");
                SnackbarUtils.Short(tipTextView, "密码设置成功").info().show();
                state = STATE_END;
                finish();
            } else {
                tipTextView.setText("两次密码不同，请重新输入");
                state = STATE_FIRST_INPUT;
            }
        }
        vcet.clearInputValue();
    }

    private void findView() {
        tipTextView = findViewById(R.id.tipTextView);
        vcet = findViewById(R.id.vcet);
        vcet.setOnInputListener(this);
        backButton = findViewById(R.id.setnumcipher_back);
        block = findViewById(R.id.setnumcipher_block);
        linearLayout = findViewById(R.id.num_linear);
    }

    @Override
    public void onChange(String input) {
//        XToastUtils.toast("onChange:" + input);
    }

    @Override
    public void onClear() {
//        XToastUtils.toast("onClear");
    }

    private void initTextView() {
        if (!CipherModel.isExistedCipher(CipherModel.NUMBER_CIPHER_TITLE)) {
            tipTextView.setText("输入新密码");
            state = STATE_FIRST_INPUT;
        } else {
            tipTextView.setText("输入旧密码");
            state = STATE_VERIFICATION;
        }
    }

    private void initButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void firstSet() {
        if(!CipherModel.isFirstSetting) {
            return;
        }
        block.setVisibility(View.GONE);
        linearLayout.setPadding(0,14,0,0);
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (CipherModel.isFirstSetting) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                ClickUtils.exitBy2Click(2000, SetNumCipherActivity.this);
            }
            return true;
        } else {
            finish();
            return true;
        }

    }

    @Override
    public void onRetry() {
        if (CipherModel.isFirstSetting) {
            SnackbarUtils.Short(tipTextView, "再按一次退出程序").info().show();
        }
    }

    @Override
    public void onExit() {
        if (CipherModel.isFirstSetting) {
            XUtil.exitApp();
        }
    }
}
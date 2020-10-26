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

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;
import com.andrognito.rxpatternlockview.RxPatternLockView;
import com.andrognito.rxpatternlockview.events.PatternLockCompleteEvent;
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent;
import com.xuexiang.templateproject.R;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.edittext.verify.VerifyCodeEditText;
import com.xuexiang.xutil.XUtil;
import com.xuexiang.xutil.common.ClickUtils;

import java.util.List;

import io.reactivex.functions.Consumer;

public class SetPatternCipherActivity extends AppCompatActivity implements ClickUtils.OnClick2ExitListener {

    ImageView backButton;
    RelativeLayout block;
    LinearLayout linearLayout;
    private TextView tipTextView;
    private PatternLockView patternLockView;
    private int state = STATE_VERIFICATION;
    static private final int STATE_VERIFICATION = 0;
    static private final int STATE_FIRST_INPUT = 1;
    static private final int STATE_SECOND_INPUT = 2;
    static private final int STATE_END = 3;


    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
//            Log.d(getClass().getName(), "Pattern progress: " +
//                    PatternLockUtils.patternToString(patternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
//            Log.d(getClass().getName(), "Pattern complete: " +
//                    PatternLockUtils.patternToString(patternLockView, pattern));
            String dotPattern = PatternLockUtils.patternToString(patternLockView, pattern);

            if (state == STATE_VERIFICATION) {
                if (CipherModel.isCipherCorrect(CipherModel.PATTERN_CIPHER_TITLE, dotPattern)) {
                    tipTextView.setText("绘制新密码");
                    state = STATE_FIRST_INPUT;
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                } else {
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    tipTextView.setText("密码错误，请再次绘制");
                }
            } else if (state == STATE_FIRST_INPUT) {
                if (dotPattern.length() < 4) {
                    tipTextView.setText("需至少绘制4个点");
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                    return;
                } else {
                    cipher = dotPattern;
                    tipTextView.setText("再次绘制密码");
                    state = STATE_SECOND_INPUT;
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                }
            } else if (state == STATE_SECOND_INPUT) {
                if (dotPattern.equals(cipher)) {
                    CipherModel.insertCipher(CipherModel.PATTERN_CIPHER_TITLE, cipher);
                    tipTextView.setText("密码设置成功");
                    SnackbarUtils.Short(tipTextView, "密码设置成功").info().show();
                    state = STATE_END;
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                    LockActivity.flag = true;
                    finish();
                } else {
                    tipTextView.setText("两次密码不同，请重新绘制");
                    state = STATE_FIRST_INPUT;
                    patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                }
            }
        }

        @Override
        public void onCleared() {
            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };

    private String cipher = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpatterncipher);
        findView();
        initPatternLockView();
        initTextView();
        initButton();
        firstSet();
    }

    private void findView() {
        tipTextView = findViewById(R.id.tipTextView);
        patternLockView = findViewById(R.id.patter_lock_view);
        backButton = findViewById(R.id.sepatterncipher_back);
        block = findViewById(R.id.sepatterncipher_block);
        linearLayout = findViewById(R.id.pattern_linear);
    }

    private void initTextView() {
        if (!CipherModel.isExistedCipher(CipherModel.PATTERN_CIPHER_TITLE)) {
            tipTextView.setText("绘制新密码");
            state = STATE_FIRST_INPUT;
        } else {
            tipTextView.setText("绘制旧密码");
            state = STATE_VERIFICATION;
        }
    }

    private void initPatternLockView() {
        patternLockView.setDotCount(3);
        patternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        patternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        patternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        patternLockView.setAspectRatioEnabled(true);
        patternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        patternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        patternLockView.setDotAnimationDuration(150);
        patternLockView.setPathEndAnimationDuration(100);
        patternLockView.setInStealthMode(false);
        patternLockView.setTactileFeedbackEnabled(true);
        patternLockView.setInputEnabled(true);
        patternLockView.addPatternLockListener(mPatternLockViewListener);

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
                ClickUtils.exitBy2Click(2000, SetPatternCipherActivity.this);
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
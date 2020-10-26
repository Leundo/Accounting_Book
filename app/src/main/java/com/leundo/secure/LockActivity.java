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

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.andrognito.patternlockview.utils.ResourceUtils;
import com.andrognito.rxpatternlockview.RxPatternLockView;
import com.andrognito.rxpatternlockview.events.PatternLockCompleteEvent;
import com.andrognito.rxpatternlockview.events.PatternLockCompoundEvent;
import com.leundo.data.BillDao;
import com.leundo.exchange.ExchangeActivity;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.activity.MainActivity;
import com.xuexiang.templateproject.utils.XToastUtils;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.edittext.verify.PwdTextView;
import com.xuexiang.xui.widget.edittext.verify.VerifyCodeEditText;
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar;

import java.lang.reflect.Field;
import java.security.Security;
import java.util.List;
import java.util.concurrent.Executor;

import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class LockActivity extends AppCompatActivity implements VerifyCodeEditText.OnInputListener {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private BillDao billDao;

    private ImageView biometricLoginButton;

    private PatternLockView mPatternLockView;

    private VerifyCodeEditText vcet;

    public static boolean flag = false;

    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
//            Log.d(getClass().getName(), "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
//            Log.d(getClass().getName(), "Pattern progress: " +
//                    PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
//            Log.d(getClass().getName(), "Pattern complete: " +
//                    PatternLockUtils.patternToString(mPatternLockView, pattern));
//

            if (CipherModel.isCipherCorrect(CipherModel.PATTERN_CIPHER_TITLE, PatternLockUtils.patternToString(mPatternLockView, pattern))) {
                Intent intent = new Intent(LockActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                mPatternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                CookieBar.builder(LockActivity.this)
                        .setTitle("密码错误")
                        .setMessage("")
                        .show();
            }

        }

        @Override
        public void onCleared() {
//            Log.d(getClass().getName(), "Pattern has been cleared");
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lock);

        billDao = new BillDao(this);
        CipherModel.isFirstSetting = true;

        findView();
        initFingerPrint();
        initPatternLockView();
        initVect();

        setPatternCipher();

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (flag) {
            setNumCipher();
        }
    }

    private void initPatternLockView() {
        mPatternLockView.setDotCount(3);
        mPatternLockView.setDotNormalSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_size));
        mPatternLockView.setDotSelectedSize((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_dot_selected_size));
        mPatternLockView.setPathWidth((int) ResourceUtils.getDimensionInPx(this, R.dimen.pattern_lock_path_width));
        mPatternLockView.setAspectRatioEnabled(true);
        mPatternLockView.setAspectRatio(PatternLockView.AspectRatio.ASPECT_RATIO_HEIGHT_BIAS);
        mPatternLockView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
        mPatternLockView.setDotAnimationDuration(150);
        mPatternLockView.setPathEndAnimationDuration(100);
        mPatternLockView.setInStealthMode(false);
        mPatternLockView.setTactileFeedbackEnabled(true);
        mPatternLockView.setInputEnabled(true);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);

    }

    private void initFingerPrint() {
        executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(LockActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
//                Toast.makeText(getApplicationContext(),
//                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LockActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "指纹认证失败",
                        Toast.LENGTH_SHORT)
                        .show();

            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("验证您的身份")
                .setSubtitle("使用指纹解锁")
                .setNegativeButtonText("返回")
                .build();

        // Prompt appears when user clicks "Log in".
        // Consider integrating with the keystore to unlock cryptographic operations,
        // if needed by your app.
        biometricLoginButton.setOnClickListener(view -> {
            biometricPrompt.authenticate(promptInfo);
        });
    }

    private void initVect() {
        vcet.setOnInputListener(LockActivity.this);
    }

    //============ VerifyCodeEditText.OnInputListener Begin ============



    @Override
    public void onComplete(String input) {
//        XToastUtils.toast("onComplete:" + input);

        if (CipherModel.isCipherCorrect(CipherModel.NUMBER_CIPHER_TITLE, input)) {
            Intent intent = new Intent(LockActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            vcet.clearInputValue();
//            setVcetTextColor(0x000000);
            CookieBar.builder(LockActivity.this)
                    .setTitle("密码错误")
                    .setMessage("")
                    .show();
        }
    }

    @Override
    public void onChange(String input) {

//        XToastUtils.toast("onChange:" + input);
    }

    @Override
    public void onClear() {

//        XToastUtils.toast("onClear");
    }

//    @OnClick(R.id.btn_clear)
//    public void onViewClicked() {
//        vcet.clearInputValue();
//    }




    //============ VerifyCodeEditText.OnInputListener End ============

    private void findView() {
        biometricLoginButton = findViewById(R.id.biometric_login);
        mPatternLockView = (PatternLockView) findViewById(R.id.patter_lock_view);
        vcet = findViewById(R.id.vcet);
    }

    private void setNumCipher() {
        if (!CipherModel.isExistedCipher(CipherModel.NUMBER_CIPHER_TITLE)) {
            Intent intent = new Intent(LockActivity.this, SetNumCipherActivity.class);
//            intent.putExtra("isFirstSetting", 1);
            startActivity(intent);
        }
    }

    private void setPatternCipher() {
        if (!CipherModel.isExistedCipher(CipherModel.PATTERN_CIPHER_TITLE)) {
            Intent intent = new Intent(LockActivity.this, SetPatternCipherActivity.class);
//            intent.putExtra("isFirstSetting", 1);
            startActivity(intent);
        } else {
            flag = true;
        }
    }

    private void setVcetTextColor(int color){
        Field field = null;
        Drawable mBackgroundNormal = null;
        PwdTextView[] mPwdTextViews = null;

        try {
            field = vcet.getClass().getDeclaredField("mBackgroundNormal");
            field.setAccessible(true);
            mBackgroundNormal = (Drawable) field.get(vcet);

            field = vcet.getClass().getDeclaredField("mPwdTextViews");
            field.setAccessible(true);
            mPwdTextViews = (PwdTextView[]) field.get(vcet);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        for (PwdTextView pwdTextView: mPwdTextViews) {
//
//            pwdTextView.setTextColor(color);
//        }

        mBackgroundNormal.setColorFilter(0xfe9778, PorterDuff.Mode.SRC_ATOP);

        for (PwdTextView pwdTextView : mPwdTextViews) {
            pwdTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_budget));
        }


    }

}
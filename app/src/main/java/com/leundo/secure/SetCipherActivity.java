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
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.leundo.customization.CustomizationSuperActivity;
import com.xuexiang.templateproject.MyApp;
import com.xuexiang.templateproject.R;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

public class SetCipherActivity extends AppCompatActivity {

    ImageView backButton;

    SuperTextView numTextView;
    SuperTextView patternTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setcipher);
        findView();
        initButton();
        CipherModel.isFirstSetting = false;
    }

    private void findView() {
        backButton = findViewById(R.id.setCipher_back);
        numTextView = findViewById(R.id.num_textview);
        patternTextView = findViewById(R.id.pattern_textview);
    }

    private void initButton() {
        // 设置返回按钮
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        numTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetCipherActivity.this, SetNumCipherActivity.class);
//                intent.putExtra("isFirstSetting", 0);
                startActivity(intent);
            }
        });

        patternTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetCipherActivity.this, SetPatternCipherActivity.class);
//                intent.putExtra("isFirstSetting", 0);
                startActivity(intent);
            }
        });

    }


}
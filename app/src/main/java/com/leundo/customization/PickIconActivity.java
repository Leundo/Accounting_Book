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

package com.leundo.customization;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.donkingliang.groupedadapter.layoutmanger.GroupedGridLayoutManager;
import com.leundo.exchange.ExchangeAdapter;
import com.xuexiang.templateproject.R;

public class PickIconActivity extends AppCompatActivity {

    ImageView backButton;
    RecyclerView recyclerView;

    PickIconAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickicon);

        findView();
        initButton();
        initRecyclerView();
    }

    private void findView() {
        backButton = (ImageView) findViewById(R.id.pickicon_back);
        recyclerView = (RecyclerView) findViewById(R.id.pickicon_recycler);
    }

    private void initButton() {
        // 设置返回按钮
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                moveTaskToBack(true);
            }
        });
    }

    private void initRecyclerView() {
        adapter = new PickIconAdapter(this);
        adapter.setOnChildClickListener(new GroupedRecyclerViewAdapter.OnChildClickListener() {
            @Override
            public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder,
                                     int groupPosition, int childPosition) {
                PickIconModel.doPick = true;
                PickIconModel.pickResourceRow = childPosition;
                finish();
            }
        });

        GroupedGridLayoutManager gridLayoutManager = new GroupedGridLayoutManager(this, 4, adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }


}
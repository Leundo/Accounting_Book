package com.leundo.exchange;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.umeng.commonsdk.debug.I;
import com.xuexiang.templateproject.R;

public class ExchangeActivity extends AppCompatActivity {

    ExchangeAdapter adapter = null;
    RecyclerView recyclerView;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        findView();
        initView();
    }

    private void findView() {
        recyclerView = (RecyclerView) findViewById(R.id.exchange_recycler);
        backButton = (ImageView) findViewById(R.id.exchange_back);


    }

    private void initView() {
        // 设置返回按钮
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                moveTaskToBack(true);
            }
        });

        // 设置RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExchangeAdapter(this);
        recyclerView.setAdapter(adapter);
    }

}
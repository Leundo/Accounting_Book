package com.leundo.account;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.templateproject.R;

public class AccountActivity extends AppCompatActivity {

    ImageView accountBack;
    RecyclerView recyclerView;
    AccountAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        findView();
        initImageView();
        initRecyclerView();
    }

    private void findView() {
        accountBack = (ImageView) findViewById(R.id.account_back);
        recyclerView = (RecyclerView) findViewById(R.id.account_recycler);
    }

    private void initImageView() {
        // 设置返回按钮
        accountBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                moveTaskToBack(true);
            }
        });
    }

    private void initRecyclerView() {
        // 设置RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AccountAdapter(this);
        recyclerView.setAdapter(adapter);
    }

}
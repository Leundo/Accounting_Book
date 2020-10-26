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

package com.leundo.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leundo.data.Bill;
import com.leundo.data.BillDao;
import com.leundo.exchange.ExchangeAdapter;
import com.xuexiang.templateproject.R;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {

    private Context context;
    private List<String> accounts = null;

    public AccountAdapter(Context context) {
        super();
        this.context = context;
        BillDao billDao = new BillDao(context);
        accounts = billDao.queryAccount();
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        String account = accounts.get(position);
        holder.superTextView.setLeftString(account);

        BillDao billDao = new BillDao(context);
        ArrayList<Bill> bills = billDao.queryBillByAccount(account);
        double leftover = 0;
        for (Bill bill : bills) {
            if (bill.getCategory().equals(Bill.CATEGORY_INCOME_TITLE)) {
                leftover += Math.abs(bill.getCNYAmount());
            } else if (bill.getCategory().equals(Bill.CATEGORY_EXPENDITURE_TITLE)) {
                leftover -= Math.abs(bill.getCNYAmount());
            } else if (bill.getCategory().equals(Bill.CATEGORY_TRANSFER_TITLE)) {
                leftover += bill.getCNYAmount();
            }
        }
        DecimalFormat decimalFormatter = new DecimalFormat("0.00");
        holder.superTextView.setRightString(decimalFormatter.format(leftover));
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_super_text_no_arrow, parent, false);
        AccountViewHolder holder = new AccountViewHolder(view);
        return holder;
    }

    public class AccountViewHolder extends RecyclerView.ViewHolder {
        public SuperTextView superTextView;

        public AccountViewHolder(View itemView) {
            super(itemView);
            superTextView = itemView.findViewById(R.id.stv);
        }
    }
}

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

package com.leundo.exchange;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.leundo.budget.BudgetModel;
import com.leundo.data.Bill;
import com.xuexiang.templateproject.R;
import com.xuexiang.templateproject.activity.MainActivity;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;

import java.text.DecimalFormat;
import java.util.List;

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.ExchangeViewHolder> {

    Context context;

    private List<String> currency = Bill.CURRENCY_LIST;

    private String[] currencyName = {
            "人民币",
            "美元",
            "欧元",
            "日元",
            "英镑",
            "澳元",
            "加元",
            "瑞士法郎",
            "新西兰元",
            "港元",
            "韩元",
            "印度卢比",
            "朝鲜元",
            "澳门元",
            "新台币",
            "俄罗斯卢布",
            "新加坡元",
            "泰铢",
            "新土耳其里拉",
            "越南盾",
            "马来西亚林吉特",
            "南非兰特",
            "阿联酋迪拉姆",
            "沙特里亚尔",
            "匈牙利福林",
            "波兰兹罗提",
            "丹麦克朗",
            "瑞典克朗",
            "挪威克朗",
            "墨西哥比索"
    };

    public ExchangeAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return currency.size() - 1;
    }

    @Override
    public void onBindViewHolder(@NonNull ExchangeViewHolder holder, int position) {
        holder.superTextView.setLeftString(currencyName[position + 1]);
        holder.superTextView.setRightString(Bill.EXCHANGE_RATE.get(currency.get(position + 1)) + "");
        holder.superTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(context)
                        .iconRes(R.drawable.ic_exchange)
                        .title("兑人民币计算")
                        .content("请输入" + currencyName[position+1] + "金额")
                        .inputType(
                                InputType.TYPE_CLASS_NUMBER
                                        | InputType.TYPE_NUMBER_FLAG_DECIMAL)
                        .input(
                                "0",
                                "",
                                false,
                                ((dialog, input) -> calculateCNY(input.toString(), position)))
                        .positiveText("确定")
                        .negativeText("取消")
//                .onPositive((dialog, which) -> XToastUtils.toast("你输入了:" + dialog.getInputEditText().getText().toString()))
                        .cancelable(false)
                        .show();


            }
        });
    }

    private void calculateCNY(String foreignCurrencyAmount, int position) {
        double amount = 0;
        if (foreignCurrencyAmount.equals("")) {
            foreignCurrencyAmount = "0";
        } else {
            amount = Double.parseDouble(foreignCurrencyAmount);
        }
        DecimalFormat decimalFormatter = new DecimalFormat("0.00");
        DialogLoader.getInstance().showTipDialog(
                context,
                foreignCurrencyAmount + " " + currencyName[position + 1] + "能兑换",
                decimalFormatter.format(amount * Bill.EXCHANGE_RATE.get(currency.get(position + 1))) +" 人民币",
                "确定");
    }

    @NonNull
    @Override
    public ExchangeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_super_text_no_arrow, parent, false);
        ExchangeViewHolder holder = new ExchangeViewHolder(view);
        return holder;
    }

    public class ExchangeViewHolder extends RecyclerView.ViewHolder {
        public SuperTextView superTextView;

        public ExchangeViewHolder(View itemView) {
            super(itemView);
            superTextView = itemView.findViewById(R.id.stv);
        }
    }
}

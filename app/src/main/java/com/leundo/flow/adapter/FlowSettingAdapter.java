package com.leundo.flow.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.donkingliang.groupedadapter.adapter.GroupedRecyclerViewAdapter;
import com.donkingliang.groupedadapter.holder.BaseViewHolder;
import com.leundo.flow.activity.FlowSettingActivity;
import com.loancold.adapter.BillAdapter;
import com.loancold.item.BillItem;
import com.xuexiang.templateproject.R;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;


public class FlowSettingAdapter extends RecyclerView.Adapter<FlowSettingAdapter.FlowSettingViewHolder> {

    public static final String ACCOUNT_ALL = "所有";

    public interface OnItemClickListener{
        public void OnItemClick(int position);
    }

    FlowSettingAdapter.OnItemClickListener listener;

    public static final int MODE_POSITION = 0;
    public static final int TIME_POSITION = 1;
    public static final int ACCOUNT_POSITION = 2;
    public static final int NUMBER_POSITION = 3;
    public static final int FIRM_POSITION = 4;

    private Context context;

    // 修改内容同时修改 XX_POSITION
    public static String[] settingName =  {"显示模式", "时间", "账户", "成员", "商家"};
    public static String[] settingResult = {"年", "TODO", "所有", "所有", "所有"};

    public boolean setResult(int position, String result) {
        if (position >= 0 && position < settingResult.length) {
            settingResult[position] = result;
            return true;
        }
        return false;
    }

    public static String getResult(int position) {
        return settingResult[position];
    }

    public FlowSettingAdapter(Context context, FlowSettingActivity.ShowMode showMode, int year, int month) {
        super();
        this.context = context;
        if (showMode == FlowSettingActivity.ShowMode.year) {
            setResult(FlowSettingAdapter.MODE_POSITION, "年");
            setResult(FlowSettingAdapter.TIME_POSITION, year + "年");
        } else {
            setResult(FlowSettingAdapter.MODE_POSITION, "月");
            setResult(FlowSettingAdapter.TIME_POSITION, year + "年" + (month + 1) + "月");
        }
    }

    @NonNull
    @Override
    public FlowSettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_super_text, parent, false);
        FlowSettingViewHolder holder = new FlowSettingViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FlowSettingViewHolder holder, int position) {
        holder.superTextView.setLeftString(settingName[position]);
        holder.superTextView.setRightString(settingResult[position]);
        holder.superTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.OnItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return settingName.length;
    }



    public void setOnItemClick(FlowSettingAdapter.OnItemClickListener listener){
        this.listener = listener;
    }

    public class FlowSettingViewHolder extends RecyclerView.ViewHolder {
        public SuperTextView superTextView;

        public FlowSettingViewHolder(View itemView) {
            super(itemView);
            superTextView = itemView.findViewById(R.id.stv);
        }
    }
}

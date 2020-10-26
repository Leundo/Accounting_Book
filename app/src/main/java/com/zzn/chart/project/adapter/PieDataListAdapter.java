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

package com.zzn.chart.project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.templateproject.R;
import com.zzn.chart.project.activity.BrowseActivity;
import com.zzn.chart.project.data.PieListData;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class PieDataListAdapter extends RecyclerView.Adapter<PieDataListAdapter.ViewHolder> {
    private ArrayList<PieListData> pieList;
    private ArrayList<String> intentData;
    private String mode;
    Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View listView;
        Button color;
        TextView pieDataType;
        TextView pieDataAmount;
        TextView progressBarText;
        ProgressBar progressBar;
        ImageButton browseButton;

        public ViewHolder(View view) {
            super(view);
            listView = view;
            color = view.findViewById(R.id.color_type);
            pieDataType = view.findViewById(R.id.pie_data_type);
            pieDataAmount = view.findViewById(R.id.pie_data_amount);
            progressBarText = view.findViewById(R.id.progressBar_text);
            progressBar = view.findViewById(R.id.progressBar);
            browseButton = view.findViewById(R.id.check_browse_button);
        }
    }

    public PieDataListAdapter(ArrayList<PieListData> pieList, ArrayList<String> intentData, Context context,String mode){
        this.pieList = pieList;
        this.intentData = intentData;
        this.context = context;
        this.mode = mode;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pie_data,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PieDataListAdapter.ViewHolder holder, int position) {
        PieListData pieListData = pieList.get(position);
        holder.color.setBackgroundColor(pieListData.getColor());
        holder.pieDataType.setText(pieListData.getPieDataType());
        if(mode.equals("1") || mode.equals("3") || mode.equals("5")){
            holder.pieDataAmount.setText("-"+pieListData.getPieDataAmount());
        }
        else {
            holder.pieDataAmount.setText(""+pieListData.getPieDataAmount());
        }
        DecimalFormat decimalformatter = new DecimalFormat("0.0");
        holder.progressBarText.setText(decimalformatter.format(pieListData.getProportion())+"%");
        holder.progressBar.setProgress((int)pieListData.getProportion());
        holder.listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrowseActivity.class);
                intent.putExtra("begin_year",intentData.get(0));
                intent.putExtra("begin_mouth",intentData.get(1));
                intent.putExtra("begin_day",intentData.get(2));
                intent.putExtra("end_year",intentData.get(3));
                intent.putExtra("end_mouth",intentData.get(4));
                intent.putExtra("end_day",intentData.get(5));
                intent.putExtra("mode", intentData.get(6));
                intent.putExtra("Label",holder.pieDataType.getText());
                context.startActivity(intent);
            }
        });

        holder.browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrowseActivity.class);
                intent.putExtra("begin_year",intentData.get(0));
                intent.putExtra("begin_mouth",intentData.get(1));
                intent.putExtra("begin_day",intentData.get(2));
                intent.putExtra("end_year",intentData.get(3));
                intent.putExtra("end_mouth",intentData.get(4));
                intent.putExtra("end_day",intentData.get(5));
                intent.putExtra("mode", intentData.get(6));
                intent.putExtra("Label",holder.pieDataType.getText());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pieList.size();
    }
}

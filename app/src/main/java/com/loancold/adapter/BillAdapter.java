package com.loancold.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leundo.data.BillDao;
import com.loancold.item.BillDayOverviewItem;
import com.loancold.item.BillItem;
import com.loancold.item.ItemType;
import com.xuexiang.templateproject.R;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ///////////////////////////Interface/////////////////////////////////////
    OnItemClickListener listener;
    public interface OnItemClickListener{
        public void OnItemClick(View v, int position, BillItem bill);
    }
    public void setOnItemClick(OnItemClickListener listener){
        this.listener = listener;
    }

    ///////////////////////////Init/////////////////////////////////////
    private static final String TAG = "OverviewFragment";

    private List<ItemType> billList;//账单视图列表，包括有账单总览视图
//    private int position_yesterday;
//    private int position_dayBefYest;

    public static final int BILL_TYPE = R.layout.item_bill;
    public static final int BILLDAYOVERVIEW_TYPE = R.layout.item_bill_dayoverview;
    public BillAdapter(List<ItemType> billListOut){
        this.billList = billListOut;
        //!!!!!!!!!TEST!!!!!
        for(int position=0;position<billList.size();position++){
            int type = billList.get(position).getItemType();
            switch (type){
                case BILL_TYPE:{
                    BillItem billItem = (BillItem)billList.get(position);
                    Log.e(TAG,"BillItem"+"("+position+")"+billItem.getTimeHHMM());
                    break;
                }
                case BILLDAYOVERVIEW_TYPE:{
                    BillDayOverviewItem billDOItem = (BillDayOverviewItem)billList.get(position);
                    Log.e(TAG,"BillDayOverviewItem"+"("+position+")"+billDOItem.getTime());
                    break;
                }
            }
        }


    }

    public void setBillList(List<ItemType> billList) {
        this.billList = billList;
    }


    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////View Holder/////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    static class BillViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout rl_bill;
        ImageView iv_bill;
        TextView tv_name;
        TextView tv_time;
        TextView tv_money;

        public BillViewHolder(View itemView) {
            super(itemView);
            rl_bill = itemView.findViewById(R.id.rl_bill);
            iv_bill = itemView.findViewById(R.id.iv_bill);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_money = itemView.findViewById(R.id.tv_money);
        }
    }

    static class BillDayOverviewViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout rl_bill_dayoverview;
        public TextView tv_dayTime;
        public TextView tv_moneyOut;
        public TextView tv_moneyIn;
        public TextView tv_moneyDiff;

        public BillDayOverviewViewHolder(View itemView) {
            super(itemView);
            rl_bill_dayoverview = itemView.findViewById(R.id.rl_bill_dayoverview);
            tv_dayTime = itemView.findViewById(R.id.tv_dayTime);
            tv_moneyOut = itemView.findViewById(R.id.tv_moneyOut);
            tv_moneyIn = itemView.findViewById(R.id.tv_moneyIn);
            tv_moneyDiff = itemView.findViewById(R.id.tv_moneyDiff);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////Override Adapter////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public int getItemViewType(int position) {
        return billList.get(position).getItemType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case BILL_TYPE:{
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill,parent,false);
                return new BillViewHolder(view);
            }
            case BILLDAYOVERVIEW_TYPE:{
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_dayoverview,parent,false);
                return new BillDayOverviewViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type){
            case BILL_TYPE:{
                BillItem billItem = (BillItem)billList.get(position);
                BillViewHolder billViewHolder = (BillViewHolder) holder;
                billViewHolder.iv_bill.setImageResource(billItem.getBillTypeImgSourceId());
                billViewHolder.tv_time.setText(billItem.getTimeHHMM());
                billViewHolder.tv_name.setText(billItem.getRemark());
                billViewHolder.tv_money.setText(billItem.getAmountString());
                billViewHolder.rl_bill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(listener != null){
                            listener.OnItemClick(view, position, billItem);
                        }
                    }
                });
                break;
            }
            case BILLDAYOVERVIEW_TYPE:{
                Log.e(TAG,""+position);
                BillDayOverviewItem billDayOverviewItem = (BillDayOverviewItem)billList.get(position);
                BillDayOverviewViewHolder billDayOverviewViewHolder = (BillDayOverviewViewHolder) holder;
                Log.e(TAG,billDayOverviewViewHolder.toString());
                billDayOverviewViewHolder.tv_dayTime.setText(billDayOverviewItem.getTime());
                billDayOverviewViewHolder.tv_moneyOut.setText(billDayOverviewItem.getOutcomeString());
                billDayOverviewViewHolder.tv_moneyIn.setText(billDayOverviewItem.getIncomeString());
                billDayOverviewViewHolder.tv_moneyDiff.setText("");
                break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return billList.size();
    }


    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////Data Changed////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////


//    //@return false-> modify failed
//    public boolean modifyData(int position){
//        if(position == 0 || position == position_yesterday ||
//                position == position_dayBefYest)
//            return false;
//        BillItem billItem = (BillItem)billList.get(position);
//
//    }

}

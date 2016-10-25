package com.rimi.schoolteacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.activity.DealActivity;
import com.rimi.schoolteacher.bean.PijiaData;
import com.rimi.schoolteacher.utils.CommonUtils;

import java.util.List;

/**
 * Created by Android on 2016/7/19.
 */
public class PijiaAdapter extends RecyclerView.Adapter<PijiaAdapter.MyViewHolder> {

    private Context mContext;
    private int type;
    private Resources res;
    private List<PijiaData> mList;

    public PijiaAdapter(Context mContext, List<PijiaData> mList,int type){
        this.mContext = mContext;
        this.type = type;
        this.mList = mList;
        res = mContext.getResources();
    }
    @Override
    public PijiaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_pijia, null);
        MyViewHolder holder = new MyViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(PijiaAdapter.MyViewHolder holder, final int position) {
        String appTime = mList.get(position).getAskForLeaveApplicationTime();
        String dealTime = mList.get(position).getAskForLeaveStartTime();
        holder.nameTv.setText(mList.get(position).getStudent().getStudentName());
        holder.typeTv.setText(mList.get(position).getAskForLeaveType());
        holder.applicationTimeTv.setText(TextUtils.isEmpty(appTime) ? "" : CommonUtils.transforTimeTwo(appTime));
        holder.dealTimeTv.setText(TextUtils.isEmpty(dealTime) ? "" : CommonUtils.transforTimeTwo(dealTime));
        if (type == 1){
            holder.undealTv.setTextColor(res.getColor(R.color.write));
            String state = mList.get(position).getAskForLeaveStatus();
            holder.undealTv.setText(state);
            if (state.equals("同意")){
                holder.undealTv.setBackgroundColor(res.getColor(R.color.sign_bg));
            }else {
                holder.undealTv.setBackgroundColor(res.getColor(R.color.refuse));
            }
//            holder.undealTv.setClickable(false);
        }else {

        }
        holder.commonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DealActivity.class);
                intent.putExtra("code",type);
                intent.putExtra("askForLeaveId",mList.get(position).getAskForLeaveId());
                intent.putExtra("studentName",mList.get(position).getStudent().getStudentName());
                intent.putExtra("startTime",mList.get(position).getAskForLeaveStartTime());
                intent.putExtra("endTime",mList.get(position).getAskForLeaveEndTime());
                intent.putExtra("content", mList.get(position).getAskForLeaveContent());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder{

         private TextView undealTv;
         private TextView typeTv;
         private TextView applicationTimeTv;
         private TextView dealTimeTv;
         private TextView nameTv;
         private ViewGroup commonLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            undealTv = (TextView) itemView.findViewById(R.id.state_tv);
            typeTv = (TextView) itemView.findViewById(R.id.pijia_type_tv);
            applicationTimeTv = (TextView) itemView.findViewById(R.id.application_time_tv);
            dealTimeTv = (TextView) itemView.findViewById(R.id.deal_time_tv);
            nameTv = (TextView) itemView.findViewById(R.id.name_tv);
            commonLayout = (ViewGroup) itemView.findViewById(R.id.undeal_state);
        }
    }
}

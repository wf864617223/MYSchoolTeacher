package com.rimi.schoolteacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.bean.NotifyData;

import java.util.List;

/**
 * Created by Android on 2016/7/6.
 */
public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.MyViewHolder>{

    private Context mContext;
    private List<NotifyData> list;
    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public NotifyAdapter(Context mContext, List<NotifyData> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_notify, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.titleTv.setText(list.get(position).getNoticeTitle());
        holder.timeTv.setText(list.get(position).getNoticeDate());
        holder.commonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerItemClickListener != null){
                    onRecyclerItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ViewGroup commonLayout;

        public TextView titleTv;

        public TextView timeTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            commonLayout = (ViewGroup) itemView.findViewById(R.id.notify_layout);
            titleTv = (TextView) itemView.findViewById(R.id.title_tv);
            timeTv = (TextView) itemView.findViewById(R.id.time_tv);
        }
    }
    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener listener){
        this.onRecyclerItemClickListener = listener;
    }
    public interface OnRecyclerItemClickListener{
        void onItemClick(int position);
    }
}

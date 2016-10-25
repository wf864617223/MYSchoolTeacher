package com.rimi.schoolteacher.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.bean.TongjiCommonData;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/8/3.
 */
public class TongjiCommonAdapter extends RecyclerView.Adapter<TongjiCommonAdapter.ViewHolder> {

    private Context mContext;
    private List<TongjiCommonData> mList;

    public TongjiCommonAdapter(Context mContext,List<TongjiCommonData> mList){
        this.mContext = mContext;
        this.mList = mList;
    }
    @Override
    public TongjiCommonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_tongji_common, null);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(TongjiCommonAdapter.ViewHolder holder, final int position) {
        holder.nameTv.setText(mList.get(position).getStudent().getStudentName());
        holder.stateTv.setText(mList.get(position).getRollcallStudentType());
        holder.timeTv.setText(mList.get(position).getRollcallStudentTime());
        String sex = mList.get(position).getStudent().getStudentSex();
        if (sex.equals("男")){
            holder.sexImg.setImageResource(R.mipmap.men);
        }else {
            holder.sexImg.setImageResource(R.mipmap.women);
        }
        holder.telImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle("提示")
                        .setMessage("是否拨号")
                        .setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String phone = mList.get(position).getStudent().getStudentTelphone();
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);
                            }
                        })
                        .setPositiveButton("否",null)
                        .create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.sex_img)
        ImageView sexImg;
        @Bind(R.id.tel_img)
        ImageView telImg;
        @Bind(R.id.sign_time_tv)
        TextView timeTv;
        @Bind(R.id.state_tv)
        TextView stateTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

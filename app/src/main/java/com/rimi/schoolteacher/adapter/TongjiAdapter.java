package com.rimi.schoolteacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.activity.TongJiCommonActivity;
import com.rimi.schoolteacher.bean.PieHelper;
import com.rimi.schoolteacher.bean.TongjiData;
import com.rimi.schoolteacher.widget.PieView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/8/2.
 */
public class TongjiAdapter extends RecyclerView.Adapter<TongjiAdapter.ViewHolder> {

    private Context mContext;
    private List<TongjiData> mList;
    private Resources res;
    private String className;

    public TongjiAdapter(Context mContext, List<TongjiData> mList, String className) {
        this.mContext = mContext;
        this.mList = mList;
        res = mContext.getResources();
        this.className  = className;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_tongji,null);
        ViewHolder holder = new ViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.classNameTv.setText(mList.get(position).getCurriculumName());
        holder.startClassTimeTv.setText(mList.get(position).getCurriculumStartTime());
        holder.teacherNameTv.setText(mList.get(position).getTeacherName());
        init(holder.mPieChart, position);
        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TongJiCommonActivity.class);
                intent.putExtra("curriculumId", mList.get(position).getCurriculumId());
                intent.putExtra("className", className);
                intent.putExtra("time", mList.get(position).getCurriculumStartTime());
                mContext.startActivity(intent);
            }
        });
    }
    private void init(PieView mPieChart, int position){
        float sum = mList.get(position).getSum();
        float late = mList.get(position).getLate();
        float ask = mList.get(position).getAsk();
        float leaveEarly = mList.get(position).getLeaveEarly();
        float absent = mList.get(position).getAbsent();
        float rollcall = mList.get(position).getRollcall();
        final ArrayList<PieHelper> list = new ArrayList<PieHelper>();
        PieHelper helper = new PieHelper((late/sum) * 100 , "",res.getColor(R.color._red));//迟到
        PieHelper helper1 = new PieHelper((ask/sum) * 100, "",res.getColor(R.color._cheng));//请假
        PieHelper helper2 = new PieHelper((leaveEarly/sum) * 100, "",res.getColor(R.color._yellow));//早退
        PieHelper helper3 = new PieHelper((absent/sum) * 100, "",res.getColor(R.color._green));//旷课
        PieHelper helper4 = new PieHelper((rollcall/sum) * 100, "",res.getColor(R.color._blue));//正常
        list.add(helper);
        list.add(helper1);
        list.add(helper2);
        list.add(helper3);
        list.add(helper4);
        mPieChart.setDate(list);
        mPieChart.selectedPie(1); //optional
        mPieChart.showPercentLabel(true);
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.piechart)
        PieView mPieChart;
        @Bind(R.id.classname_tv)
        TextView classNameTv;
        @Bind(R.id.start_class_time_tv)
        TextView startClassTimeTv;
        @Bind(R.id.teacher_name_tv)
        TextView teacherNameTv;
        @Bind(R.id.click_layout)
        View clickLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

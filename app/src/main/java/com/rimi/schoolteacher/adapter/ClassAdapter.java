package com.rimi.schoolteacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.bean.ClassData;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Android on 2016/7/12.
 */
public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyViewHolder>  {

    private Context mContext;
    private OnItemChildClickListener listener;
    private List<ClassData> mList;
    public ClassAdapter(Context mContext, List<ClassData> mList){
        this.mContext = mContext;
        this.mList = mList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.item_class, null);
        MyViewHolder holder = new MyViewHolder(rootView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.classNameTv.setText(mList.get(position).getClassName());
        holder.studentBtn.setText(mList.get(position).getClassSum());
        try {
            JSONArray array = new JSONArray(mList.get(position).getStaffList().toString());
            holder.teacherBtn.setText(array.length() + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onItemChildClick(v, holder, position);
                }
            }
        };
        holder.setBtn.setOnClickListener(onClickListener);
        holder.studentBtn.setOnClickListener(onClickListener);
        holder.teacherBtn.setOnClickListener(onClickListener);
        holder.signBtn.setOnClickListener(onClickListener);
        holder.signoutBtn.setOnClickListener(onClickListener);
        holder.tongjiBtn.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView setBtn;
        public Button studentBtn;
        public Button teacherBtn;
        public Button signBtn;
        public Button signoutBtn;
        public Button tongjiBtn;
        public TextView classNameTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            setBtn = (ImageView) itemView.findViewById(R.id.class_set_btn);
            studentBtn = (Button) itemView.findViewById(R.id.item_students_btn);
            teacherBtn = (Button) itemView.findViewById(R.id.item_teacher_btn);
            signBtn = (Button) itemView.findViewById(R.id.item_sign_btn);
            signoutBtn = (Button) itemView.findViewById(R.id.item_signout_btn);
            tongjiBtn = (Button) itemView.findViewById(R.id.item_tongji_btn);
            classNameTv = (TextView) itemView.findViewById(R.id.classname_tv);
        }
    }


    public void setOnItemChildClickListener(OnItemChildClickListener listener){
        this.listener = listener;
    }
    public interface OnItemChildClickListener{
        void onItemChildClick(View v, MyViewHolder holder, int position);
    }
}

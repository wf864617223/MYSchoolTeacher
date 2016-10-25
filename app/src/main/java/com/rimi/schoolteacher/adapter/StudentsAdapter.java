package com.rimi.schoolteacher.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.bean.InsideTeacherList;
import com.rimi.schoolteacher.bean.StudentListData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/7/18.
 */
public class StudentsAdapter extends BaseAdapter {

    private Context mContext;
    private List<StudentListData> mStudengList;
    private List<InsideTeacherList> mTeacherList;
    private int type;
    private Resources res;

    public StudentsAdapter(Context mContext, List<StudentListData> mStudengList, List<InsideTeacherList> mTeacherList, int type){
        this.mContext = mContext;
        this.mStudengList = mStudengList;
        this.mTeacherList = mTeacherList;
        this.type = type;
        res = mContext.getResources();
    }

    @Override
    public int getCount() {
        int length ;
        if (type == 1){
            length = (mStudengList == null ? 0 : mStudengList.size());
        }else {
            length = (mTeacherList == null ? 0 : mTeacherList.size());
        }
        return length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_students_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (type == 2){//教师
            holder.nameTv.setText(mTeacherList.get(position).getStaffName());
            String sex = mTeacherList.get(position).getStaffSex();
            holder.sexImg.setImageResource(sex.equals("男") ? R.mipmap.men : R.mipmap.women);
            try {
                JSONObject curriculum = new JSONObject(mTeacherList.get(position).getCurriculum().toString());
                holder.typeTv.setText(curriculum.getString("curriculumName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {//学生
            holder.nameTv.setText(mStudengList.get(position).getStudentName());
            String sex = mStudengList.get(position).getStudentSex();
            holder.sexImg.setImageResource(sex.equals("男") ? R.mipmap.men : R.mipmap.women);
            holder.typeTv.setText(mStudengList.get(position).getScoll().getScoll());
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
                                String phone;
                                if (type == 1){
                                    phone = mStudengList.get(position).getStudentTelphone();
                                }else {
                                    phone = mTeacherList.get(position).getStaffTelphone();
                                }
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);
                            }
                        })
                        .setPositiveButton("否",null)
                        .create().show();
            }
        });

        return convertView;
    }
    class ViewHolder{
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.sex_img)
        ImageView sexImg;
        @Bind(R.id.type_tv)
        TextView typeTv;
        @Bind(R.id.tel_img)
        ImageView telImg;
        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}

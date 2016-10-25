package com.rimi.schoolteacher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.rimi.schoolteacher.BaseFragment;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.activity.ClassSetActivity;
import com.rimi.schoolteacher.activity.StudentsListActivity;
import com.rimi.schoolteacher.activity.TeacherListActivity;
import com.rimi.schoolteacher.activity.TongJiActivity;
import com.rimi.schoolteacher.adapter.ClassAdapter;
import com.rimi.schoolteacher.bean.ClassData;
import com.rimi.schoolteacher.customs.ItemDivider;
import com.rimi.schoolteacher.customs.MapSignDialog;
import com.rimi.schoolteacher.customs.MapSignOutDialog;
import com.rimi.schoolteacher.https.HttpUrls;
import com.rimi.schoolteacher.https.MyCallBack;
import com.rimi.schoolteacher.https.XHttpRequest;
import com.rimi.schoolteacher.utils.CommonUtils;
import com.rimi.schoolteacher.utils.GsonUtils;
import com.rimi.schoolteacher.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/7/6.
 * 班级
 */
public class ClassFragment extends BaseFragment implements ClassAdapter.OnItemChildClickListener {

    @Bind(R.id.class_recyclerview)
    RecyclerView mRecyclerView;
    @Bind(R.id.no_data_layout)
    LinearLayout noDataLayout;

    private List<ClassData> mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_class, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void setAdapter(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ClassAdapter adapter = new ClassAdapter(getActivity(), mList);
        adapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new ItemDivider(getActivity(), R.drawable.item_divider));
    }
    private void loadData(){
        RequestParams params = new RequestParams(HttpUrls.GET_CLASS_LIST);
        params.addBodyParameter("staffId", getStaffId());
        params.addBodyParameter("teacher",getLoginData().getStaffPost().equals("老师") ? "0" : "1");
        params.addBodyParameter("token",getToken());
        XHttpRequest.getInstance().httpGet(getActivity(), params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    JSONArray result = response.getJSONArray("result");
                    mList = GsonUtils.createGson().fromJson(result.toString(), new TypeToken<List<ClassData>>(){}.getType());
                    if (mList == null || mList.size() == 0){
                        noDataLayout.setVisibility(View.VISIBLE);
                    }
                    setAdapter();
                    SPUtils.setObject(getActivity(),SAVE_CLASS_NAME,CLASS_LIST,mList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onItemChildClick(View v, ClassAdapter.MyViewHolder holder, int position) {
        switch (v.getId()){
            case R.id.class_set_btn:
                Intent classIntent = new Intent(getActivity(), ClassSetActivity.class);
                startActivity(classIntent);
                break;
            case R.id.item_students_btn:
                Intent studentIntent = new Intent(getActivity(), StudentsListActivity.class);
                studentIntent.putExtra("className", mList.get(position).getClassName());
                startActivity(studentIntent);
                break;
            case R.id.item_teacher_btn:
                Intent teacherIntent = new Intent(getActivity(), TeacherListActivity.class);
                teacherIntent.putExtra("teacherData", mList.get(position).getStaffList().toString());
                teacherIntent.putExtra("classId", mList.get(position).getClassId());
                teacherIntent.putExtra("staffId", mList.get(position).getStaffId());
                startActivity(teacherIntent);
                break;
            case R.id.item_sign_btn:
                judgeState(HttpUrls.JUDGE_IS_SIGN, mList.get(position).getClassName(), true);
                break;
            case R.id.item_signout_btn:
                judgeState(HttpUrls.JUDGE_IS_SIGNOUT, mList.get(position).getClassName(), false);
                break;
            case R.id.item_tongji_btn:
                Intent tongjiIntent = new Intent(getActivity(), TongJiActivity.class);
                tongjiIntent.putExtra("className",mList.get(position).getClassName());
                startActivity(tongjiIntent);
                break;
        }
    }
    //判断是否可以签到OR签退
    private void judgeState(String url, String className, final boolean isSign){
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("staffId", getStaffId());
        params.addBodyParameter("className", className);
        params.addBodyParameter("time", CommonUtils.getNowDateCommon());
        params.addBodyParameter("token",getToken());
        XHttpRequest.getInstance().httpGet(getActivity(), params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    JSONObject result = response.getJSONObject("result");
                    boolean isTrue;
                    if (isSign){
                        isTrue = result.getBoolean("ifSignIn");
                        MapSignDialog dialog = new MapSignDialog(getActivity());
                        if (isTrue){
                            dialog.show();
                            dialog.setSignTime(CommonUtils.getNowDateCommon());
                            dialog.setClassName(result.getString("curriculumName"));
                            dialog.setSignCode(result.getString("rollcallCode"));
                            dialog.setCurriculumId(result.getString("curriculumId"));
                        }else {
                            if (!result.isNull("curriculumName")){
                                dialog.show();
                                dialog.setSignTime(CommonUtils.getNowDateCommon());
                                dialog.setClassName(result.getString("curriculumName"));
                                dialog.setSignCode(result.getString("rollcallCode"));
                                dialog.setCurriculumId(result.getString("curriculumId"));
                                Toast.makeText(getActivity(),"已签到", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getActivity(),"当前无课程", Toast.LENGTH_LONG).show();
                            }
                        }
                    }else {
                        isTrue = result.getBoolean("ifSignOff");
                        MapSignOutDialog dialog1 = new MapSignOutDialog(getActivity());
                        if (isTrue){
                            dialog1.show();
                            dialog1.setSignoutTime(CommonUtils.getNowDateCommon());
                            dialog1.setClassName(result.getString("curriculumName"));
                            dialog1.setSignoutCode(result.getString("rollcallCode"));
                            dialog1.setCurriculumId(result.getString("curriculumId"));
                        }else {
                            if (!result.isNull("curriculumName")){
                                dialog1.show();
                                dialog1.setSignoutTime(CommonUtils.getNowDateCommon());
                                dialog1.setClassName(result.getString("curriculumName"));
                                dialog1.setSignoutCode(result.getString("rollcallCode"));
                                dialog1.setCurriculumId(result.getString("curriculumId"));
                                Toast.makeText(getActivity(),"已签退", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getActivity(),"当前时间不可签退", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

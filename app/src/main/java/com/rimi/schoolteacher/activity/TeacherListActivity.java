package com.rimi.schoolteacher.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.reflect.TypeToken;
import com.rimi.schoolteacher.BaseActivity;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.adapter.StudentsAdapter;
import com.rimi.schoolteacher.bean.InsideTeacherList;
import com.rimi.schoolteacher.https.HttpUrls;
import com.rimi.schoolteacher.https.MyCallBack;
import com.rimi.schoolteacher.https.XHttpRequest;
import com.rimi.schoolteacher.utils.GsonUtils;

import org.xutils.http.RequestParams;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/7/8.
 */
public class TeacherListActivity extends BaseActivity implements View.OnClickListener, SwipeMenuListView.OnMenuItemClickListener {

    @Bind(R.id.mlistview)
    SwipeMenuListView mListView;

    private static final int TEACHER = 2;
    List<InsideTeacherList> mList;
    private String classId;
    private String staffId;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherlist);
        ButterKnife.bind(this);
        String data = getIntent().getStringExtra("teacherData");
        classId = getIntent().getStringExtra("classId");
        staffId = getIntent().getStringExtra("staffId");
        mList = GsonUtils.createGson().fromJson(data, new TypeToken<List<InsideTeacherList>>(){}.getType());
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(180);
                deleteItem.setTitle(getResources().getString(R.string.delete));
                deleteItem.setTitleSize(16);
                deleteItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(this);
        setAdapter();
    }
    private void deleteTeacher(final int position){//maybe存在问题
        RequestParams params = new RequestParams(HttpUrls.DELETE_TEACHER_DATA);
        params.addBodyParameter("classId", classId);
        params.addBodyParameter("staffId", String.valueOf(mList.get(position).getStaffId()));
        params.addBodyParameter("token",getToken());
        XHttpRequest.getInstance().httpGet(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    return;
                }
                mList.remove(position);
                setAdapter();
            }
        });
    }
    private void setAdapter(){
        mListView.setAdapter(new StudentsAdapter(this, null,mList, TEACHER));
    }
    @OnClick({R.id.back_img})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_img:
                finish();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index){
            case 0:
                deleteTeacher(position);
                break;
        }
        return false;
    }
}

package com.rimi.schoolteacher.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.reflect.TypeToken;
import com.rimi.schoolteacher.BaseActivity;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.adapter.StudentsAdapter;
import com.rimi.schoolteacher.bean.StudentListData;
import com.rimi.schoolteacher.https.HttpUrls;
import com.rimi.schoolteacher.https.MyCallBack;
import com.rimi.schoolteacher.https.XHttpRequest;
import com.rimi.schoolteacher.utils.CommonUtils;
import com.rimi.schoolteacher.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/7/8.
 */
public class StudentsListActivity extends BaseActivity implements View.OnClickListener, SwipeMenuListView.OnMenuItemClickListener {

    @Bind(R.id.mlistview)
    SwipeMenuListView mListView;
    @Bind(R.id.search_edt)
    EditText searchEdt;

    private List<StudentListData> mlist;
    private static final int STUDENT = 1;
    private String className;
    private StudentsAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlist);
        ButterKnife.bind(this);
        className = getIntent().getStringExtra("className");
        loadData(className);
        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    String searchName = searchEdt.getText().toString().trim();
                    if (searchName.equals("")){
                        Toast.makeText(StudentsListActivity.this,"请输入您想要查看的病人名或手机号",Toast.LENGTH_LONG).show();
                        return false;
                    }
                    searchStudent(searchName);
                    return true;
                }
                return false;
            }
        });
    }
    private void loadData(String className){
        RequestParams params = new RequestParams(HttpUrls.GET_STUDENT_LIST);
        params.addBodyParameter("staffId", getStaffId());
        params.addBodyParameter("className", className);
        params.addBodyParameter("token",getToken());
        XHttpRequest.getInstance().httpGet(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    JSONArray result = response.getJSONArray("result");
                    mlist = GsonUtils.createGson().fromJson(result.toString(), new TypeToken<List<StudentListData>>(){}.getType());
                    setAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void deleteStudent(String classId, String studentId, final int positions){
        RequestParams params = new RequestParams(HttpUrls.DELETE_STUDENT_DATA);
        params.addBodyParameter("classId", classId);
        params.addBodyParameter("studentId", studentId);
        params.addBodyParameter("token",getToken());
        XHttpRequest.getInstance().httpGet(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    return;
                }
                mlist.remove(positions);
                adapter.notifyDataSetChanged();
            }
        });
    }
    private void searchStudent(String studentName){
        RequestParams params = new RequestParams(HttpUrls.SEARCH_STUDENT);
        params.addBodyParameter("staffId", getStaffId());
        params.addBodyParameter("className", className);
        params.addBodyParameter("studentName", studentName);
        params.addBodyParameter("token",getToken());
        XHttpRequest.getInstance().httpGet(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                CommonUtils.dismiss();
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    JSONArray result = response.getJSONArray("result");
                    mlist = GsonUtils.createGson().fromJson(result.toString(), new TypeToken<List<StudentListData>>(){}.getType());
                    setAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void setAdapter(){
        adapter = new StudentsAdapter(this, mlist, null, STUDENT);
        mListView.setAdapter(adapter);
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
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
        switch (index){
            case 0:
                deleteStudent(mlist.get(position).getClassId(), mlist.get(position).getStudentId(),position);
                break;
        }
        return false;
    }
}

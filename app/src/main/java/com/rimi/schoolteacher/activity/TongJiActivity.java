package com.rimi.schoolteacher.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.rimi.schoolteacher.BaseActivity;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.adapter.TongjiAdapter;
import com.rimi.schoolteacher.bean.TongjiData;
import com.rimi.schoolteacher.customs.ItemDivider;
import com.rimi.schoolteacher.https.HttpUrls;
import com.rimi.schoolteacher.https.MyCallBack;
import com.rimi.schoolteacher.https.XHttpRequest;
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
 * Created by Android on 2016/7/13.
 */
public class TongJiActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.class_name_tv)
    TextView classNameTv;

    private List<TongjiData> mList ;
    private String className;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongji);
        ButterKnife.bind(this);
        className = getIntent().getStringExtra("className");
        classNameTv.setText(className);
        loadData(className);
    }
    private void loadData(String className){
        RequestParams params = new RequestParams(HttpUrls.TEACHER_GET_CLASS_LIST);
        params.addBodyParameter("staffId",getStaffId());
        params.addBodyParameter("className",className);
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
                    mList = GsonUtils.createGson().fromJson(result.toString(), new TypeToken<List<TongjiData>>(){}.getType());
                    setAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void setAdapter(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        TongjiAdapter adapter = new TongjiAdapter(this, mList, className);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new ItemDivider(this, R.drawable.item_divider));
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
}

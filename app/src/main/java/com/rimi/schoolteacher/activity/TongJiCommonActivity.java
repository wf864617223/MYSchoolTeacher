package com.rimi.schoolteacher.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.rimi.schoolteacher.BaseActivity;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.adapter.TongjiCommonAdapter;
import com.rimi.schoolteacher.bean.TongjiCommonData;
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
 * Created by Android on 2016/7/26.
 */
public class TongJiCommonActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private List<TongjiCommonData> mList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tongji_common);
        ButterKnife.bind(this);
        loadData();
    }
    private void loadData(){
        RequestParams params = new RequestParams(HttpUrls.GET_KAOQIN_LIST);
        params.addBodyParameter("curriculumId", getIntent().getStringExtra("curriculumId"));
        params.addBodyParameter("className",getIntent().getStringExtra("className"));
        params.addBodyParameter("time",getIntent().getStringExtra("time"));
//        params.addBodyParameter("curriculumId", "188");
//        params.addBodyParameter("className","数13.6");
//        params.addBodyParameter("time","2017-10-17");
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
                    mList = GsonUtils.createGson().fromJson(result.toString(), new TypeToken<List<TongjiCommonData>>(){}.getType());
                    setAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void setAdapter(){
        TongjiCommonAdapter adapter = new TongjiCommonAdapter(this, mList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
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
}

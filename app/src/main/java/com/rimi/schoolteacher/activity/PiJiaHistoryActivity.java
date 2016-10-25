package com.rimi.schoolteacher.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.rimi.schoolteacher.BaseActivity;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.adapter.PijiaAdapter;
import com.rimi.schoolteacher.bean.PijiaData;
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
 * Created by Android on 2016/7/7.
 */
public class PiJiaHistoryActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.history_recyclerview)
    RecyclerView mRecyclerView;

    private static final int AGREE_ABOUT = 1;
    private List<PijiaData> mList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pijia_history);
        ButterKnife.bind(this);
        loadData();
    }
    private void loadData(){
        RequestParams params = new RequestParams(HttpUrls.GET_STAFF_LIST);
        params.addBodyParameter("staffId", getStaffId());
        params.addBodyParameter("history", "1");
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
                    mList = GsonUtils.createGson().fromJson(result.toString(), new TypeToken<List<PijiaData>>(){}.getType());
                    setAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void setAdapter(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        PijiaAdapter adapter = new PijiaAdapter(this, mList, AGREE_ABOUT);
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
}

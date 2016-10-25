package com.rimi.schoolteacher.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.rimi.schoolteacher.BaseFragment;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.activity.PiJiaHistoryActivity;
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
 * Created by Android on 2016/7/6.
 * 批假
 */
public class PijiaFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.pijia_recyclerview)
    RecyclerView mRecyclerView;

    private static final int UNDEAL = 2;
    private List<PijiaData> mList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_pijia, null);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData(){
        RequestParams params = new RequestParams(HttpUrls.GET_STAFF_LIST);
        params.addBodyParameter("staffId", getStaffId());
        params.addBodyParameter("history", "0");
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
                    mList = GsonUtils.createGson().fromJson(result.toString(), new TypeToken<List<PijiaData>>(){}.getType());
                    setAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void setAdapter(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        PijiaAdapter adapter = new PijiaAdapter(getActivity(), mList, UNDEAL);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new ItemDivider(getActivity(), R.drawable.item_divider));
    }
    @OnClick({R.id.history_img})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.history_img:
                startActivity(new Intent(getActivity(), PiJiaHistoryActivity.class));
                break;
        }
    }
}

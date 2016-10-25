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
import com.rimi.schoolteacher.activity.NotifyCommonActivity;
import com.rimi.schoolteacher.adapter.NotifyAdapter;
import com.rimi.schoolteacher.bean.NotifyData;
import com.rimi.schoolteacher.https.HttpUrls;
import com.rimi.schoolteacher.https.MyCallBack;
import com.rimi.schoolteacher.https.XHttpRequest;
import com.rimi.schoolteacher.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/7/6.
 * 通知
 */
public class NotifyFragment extends BaseFragment implements NotifyAdapter.OnRecyclerItemClickListener{

    @Bind(R.id.notify_recyclerview)
    RecyclerView mRecyclerView;

    private List<NotifyData> mLsit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_notify, null);
        ButterKnife.bind(this, rootView);
        loadData();
        List<String> list = new ArrayList<String>();
        list.add("111111");
        list.add("222222");
        list.add("333333");
        list.add("444444");
        list.add("555555");

        return rootView;
    }
    private void loadData(){
        RequestParams params = new RequestParams(HttpUrls.GET_NOTIFY_DATA);
        params.addBodyParameter("userId", getStaffId());
        params.addBodyParameter("userType", "教职工");
        XHttpRequest.getInstance().httpGet(getActivity(), params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    JSONArray result = response.getJSONArray("result");
                    mLsit = GsonUtils.createGson().fromJson(result.toString(), new TypeToken<List<NotifyData>>(){}.getType());
                    setAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void setAdapter(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        NotifyAdapter adapter = new NotifyAdapter(getActivity(),mLsit);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnRecyclerItemClickListener(this);
    }
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), NotifyCommonActivity.class);
        intent.putExtra("title",mLsit.get(position).getNoticeTitle());
        intent.putExtra("content",mLsit.get(position).getNoticeContent());
        intent.putExtra("date",mLsit.get(position).getNoticeDate());
        startActivity(intent);
    }
}

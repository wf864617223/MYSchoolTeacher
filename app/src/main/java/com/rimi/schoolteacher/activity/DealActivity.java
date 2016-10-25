package com.rimi.schoolteacher.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
 import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rimi.schoolteacher.BaseActivity;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.https.HttpUrls;
import com.rimi.schoolteacher.https.MyCallBack;
import com.rimi.schoolteacher.https.XHttpRequest;

import org.xutils.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/7/11.
 * 批假
 */
public class DealActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.applicant_tv)
    TextView applicantTv;
    @Bind(R.id.leave_time_tv)
    TextView leaveTimeTv;
    @Bind(R.id.end_time_tv)
    TextView endTimeTv;
    @Bind(R.id.content_tv)
    TextView contentTv;
    @Bind(R.id.agree_btn)
    Button agreeBtn;
    @Bind(R.id.refuse_btn)
    Button refuseBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal);
        ButterKnife.bind(this);
        int code = getIntent().getIntExtra("code",0);
        if (code == 1){
            agreeBtn.setText("已处理");
            agreeBtn.setClickable(false);
            refuseBtn.setVisibility(View.GONE);
        }else if (code == 2){

        }
        applicantTv.setText(getIntent().getStringExtra("studentName"));
        leaveTimeTv.setText(getIntent().getStringExtra("startTime"));
        endTimeTv.setText(getIntent().getStringExtra("endTime"));
        contentTv.setText(getIntent().getStringExtra("content"));
    }
    @OnClick({R.id.back_img,R.id.agree_btn,R.id.refuse_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_img:
                finish();
                break;
            case R.id.agree_btn:
                deal("同意");
                break;
            case R.id.refuse_btn:
                deal("不同意");
                break;
        }
    }
    private void deal(String str){
        RequestParams params = new RequestParams(HttpUrls.DEAL_ASK_LEAVE);
        params.addBodyParameter("staffId", getStaffId());
        params.addBodyParameter("askForLeaveId", getIntent().getStringExtra("askForLeaveId"));
        params.addBodyParameter("askForLeaveStatus", str);
        params.addBodyParameter("token",getToken());
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    Toast.makeText(DealActivity.this,"请求失败", Toast.LENGTH_LONG).show();
                    return;
                }
                finish();
            }
        });
    }
}

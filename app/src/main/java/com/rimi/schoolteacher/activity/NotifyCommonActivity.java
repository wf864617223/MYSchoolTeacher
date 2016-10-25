package com.rimi.schoolteacher.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.rimi.schoolteacher.BaseActivity;
import com.rimi.schoolteacher.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/7/8.
 */
public class  NotifyCommonActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.content_tv)
    WebView webView;
    @Bind(R.id.date_tv)
    TextView dateTv;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_common);
        ButterKnife.bind(this);
        titleTv.setText(getIntent().getStringExtra("title"));
        dateTv.setText(getIntent().getStringExtra("date"));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL("http://do.rimiedu.com/wxjy/", getIntent().getStringExtra("content"), "text/html", "utf-8", "");
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

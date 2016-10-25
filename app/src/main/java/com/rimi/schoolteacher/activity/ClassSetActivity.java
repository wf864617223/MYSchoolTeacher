package com.rimi.schoolteacher.activity;

import android.os.Bundle;
import android.view.View;

import com.rimi.schoolteacher.BaseActivity;
import com.rimi.schoolteacher.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/7/12.
 */
public class ClassSetActivity extends BaseActivity implements View.OnClickListener{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classset);
        ButterKnife.bind(this);
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

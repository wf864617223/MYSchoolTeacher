package com.rimi.schoolteacher.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rimi.schoolteacher.BaseActivity;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.bean.LoginResponseData;
import com.rimi.schoolteacher.https.HttpUrls;
import com.rimi.schoolteacher.https.MyCallBack;
import com.rimi.schoolteacher.https.XHttpRequest;
import com.rimi.schoolteacher.permission.MarsQuestion;
import com.rimi.schoolteacher.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/8/8.
 */
public class GuideActivity extends BaseActivity implements OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        String username = (String) SPUtils.get(GuideActivity.this, SAVE_LOGIN_DATA, USERNAME, "");
        String password = (String) SPUtils.get(GuideActivity.this, SAVE_LOGIN_DATA, PASSWORD, "");
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isAvailable()){
            Toast.makeText(this,"当前无网络连接",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            login(username, password);
        }

    }
    private void login(final String username, final String password){
        RequestParams params = new RequestParams(HttpUrls.LOGIN);
        params.addBodyParameter("account", username);
        params.addBodyParameter("password", password);
        XHttpRequest.getInstance().httpPostDefault(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                try {
                    if (!isSuccess){
                        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        JSONObject response = new JSONObject(obj.toString());
                        if (!response.isNull("status")){
                            if (response.getInt("status") == 406){
                                Toast.makeText(GuideActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(GuideActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                    JSONObject response = new JSONObject(obj.toString());
                    Gson gson = new GsonBuilder().create();
                    LoginResponseData data = gson.fromJson(response.getJSONObject("result").toString(),new TypeToken<LoginResponseData>(){}.getType());
                    SPUtils.setObject(getApplicationContext(), SAVE_LOGIN_DATA, LOGIN_RESPONSE, data);
                    SPUtils.put(getApplicationContext(),SAVE_TOKEN,TOKEN,data.getToken());
                    SPUtils.clear(getApplicationContext(),SAVE_CLASS_NAME);
                    Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @OnClick({R.id.join_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.join_btn:
                startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                finish();
                break;
        }
    }
}

package com.rimi.schoolteacher.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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
import com.rimi.schoolteacher.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/7/5.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.username_edt)
    EditText usernameEdt;
    @Bind(R.id.password_edt)
    EditText passwordEdt;

    private String username;
    private String password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        username = (String) SPUtils.get(getApplicationContext(), SAVE_LOGIN_DATA, USERNAME, "");
        password = (String) SPUtils.get(getApplicationContext(), SAVE_LOGIN_DATA, PASSWORD, "");
        if (username.equals("") || password.equals("")){

        }else {
            usernameEdt.setText(username);
            passwordEdt.setText(password);
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
        }
    }
    @OnClick({R.id.login_btn})
    @Override
    public void onClick(View v) {
        username = usernameEdt.getText().toString().trim();
        password = passwordEdt.getText().toString().trim();
        switch (v.getId()){
            case R.id.login_btn:
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(networkInfo == null || !networkInfo.isAvailable()){
                    Toast.makeText(this,"当前无网络连接",Toast.LENGTH_SHORT).show();
                }
                else {
                    login(username, password);
                }
                break;
        }
    }
    private void login(final String username, final String password){
        RequestParams params = new RequestParams(HttpUrls.LOGIN);
        params.addBodyParameter("account", username);
        params.addBodyParameter("password", password);
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                try {

                    JSONObject response = new JSONObject(obj.toString());
                if (!isSuccess){
                    if (!response.isNull("status")){
                        if (response.getInt("status") == 406){
                            Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                    Gson gson = new GsonBuilder().create();
                    LoginResponseData data = gson.fromJson(response.getJSONObject("result").toString(),new TypeToken<LoginResponseData>(){}.getType());
                    SPUtils.put(getApplicationContext(), SAVE_LOGIN_DATA, USERNAME, username);
                    SPUtils.put(getApplicationContext(), SAVE_LOGIN_DATA, PASSWORD, password);
                    SPUtils.setObject(getApplicationContext(), SAVE_LOGIN_DATA, LOGIN_RESPONSE, data);
                    SPUtils.put(getApplicationContext(),SAVE_TOKEN,TOKEN,data.getToken());
                    SPUtils.clear(getApplicationContext(),SAVE_CLASS_NAME);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

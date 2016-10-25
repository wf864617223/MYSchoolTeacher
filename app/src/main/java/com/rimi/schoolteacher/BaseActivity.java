package com.rimi.schoolteacher;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.rimi.schoolteacher.bean.LoginResponseData;
import com.rimi.schoolteacher.utils.SPUtils;

/**
 * Created by Android on 2016/7/5.
 * 基类
 */
public class BaseActivity extends FragmentActivity {

    public static final String SAVE_TOKEN = "save_token";

    public static final String SAVE_LOGIN_DATA = "save_login_data";

    public static final String USERNAME = "username";

    public static final String PASSWORD = "password";

    public static final String LOGIN_RESPONSE = "login_response";

    public static final String SAVE_HEAD_URI = "save_head_uri";

    public static final String TOKEN = "token";

    public static final String SAVE_CLASS_NAME = "save_class_name";

    public static final String CLASS_LIST = "class_list";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo == null || !networkInfo.isAvailable()){
            Toast.makeText(this,"当前无网络连接",Toast.LENGTH_SHORT).show();
        }
        else {
        }
    }
    public LoginResponseData getLoginData(){
        LoginResponseData data =  SPUtils.getObject(this, SAVE_LOGIN_DATA, LOGIN_RESPONSE);
        return  data;
    }
    public String getStaffId(){
        return String.valueOf(getLoginData().getStaffId());
    }
    public String getToken(){
        return getLoginData().getToken();
    }
}

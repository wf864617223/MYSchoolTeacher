package com.rimi.schoolteacher;

import android.support.v4.app.Fragment;

import com.rimi.schoolteacher.bean.LoginResponseData;
import com.rimi.schoolteacher.utils.SPUtils;

/**
 * Created by Android on 2016/7/21.
 */
public class BaseFragment extends Fragment {

    public static final String SAVE_HEAD_URI = "save_head_uri";

    public static final String SAVE_TOKEN = "save_token";

    public static final String SAVE_LOGIN_DATA = "save_login_data";

    public static final String SAVE_CLASS_NAME = "save_class_name";

    public static final String SAVE_SIGN_DATA = "save_sign_data";

    public static final String CLASS_LIST = "class_list";

    public LoginResponseData getLoginData(){
        LoginResponseData data =  SPUtils.getObject(getActivity(), SAVE_LOGIN_DATA, "login_response");
        return  data;
    }
    public String getStaffId(){
        return String.valueOf(getLoginData().getStaffId());
    }
    public String getToken(){
        return getLoginData().getToken();
    }
}

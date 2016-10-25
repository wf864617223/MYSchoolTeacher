package com.rimi.schoolteacher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rimi.schoolteacher.BaseActivity;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.TeacherApplication;
import com.rimi.schoolteacher.https.HttpUrls;
import com.rimi.schoolteacher.https.MyCallBack;
import com.rimi.schoolteacher.https.XHttpRequest;
import com.rimi.schoolteacher.utils.SPUtils;

import org.xutils.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/7/6.
 */
public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.oldpassword_edt)
    EditText oldPasswordEdt;
    @Bind(R.id.newpassword_edt)
    EditText newPasswordEdt;
    @Bind(R.id.confirm_edt)
    EditText confirmEdt;
    @Bind(R.id.change_password_btn)
    Button changePsBtn;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        TeacherApplication.getInstance().addActivity(this);
        changePsBtn.setClickable(false);
        changePsBtn.setBackgroundResource(R.drawable.btn_unfocuse_backg);
        newPasswordEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5){
                    changePsBtn.setClickable(true);
                    changePsBtn.setBackgroundResource(R.drawable.btn_signout_backg);
                }else {
                    changePsBtn.setClickable(false);
                    changePsBtn.setBackgroundResource(R.drawable.btn_unfocuse_backg);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @OnClick({R.id.back_img, R.id.change_password_btn})
    @Override
    public void onClick(View v) {
        String oldPassword = oldPasswordEdt.getText().toString();
        String newPassword = newPasswordEdt.getText().toString();
        String confirm = confirmEdt.getText().toString();
        switch (v.getId()){
            case R.id.back_img:
                finish();
                break;
            case R.id.change_password_btn:
                if (TextUtils.isEmpty(oldPassword)){
                    Toast.makeText(ChangePasswordActivity.this, "旧密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newPassword)){
                    Toast.makeText(ChangePasswordActivity.this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirm)){
                    Toast.makeText(ChangePasswordActivity.this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!confirm.equals(newPassword)){
                    Toast.makeText(ChangePasswordActivity.this, "请输入相同密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (oldPassword.equals(newPassword)){
                    Toast.makeText(ChangePasswordActivity.this, "新密码不能与旧密码相同", Toast.LENGTH_SHORT).show();
                    return;
                }
                changePassword(oldPassword, newPassword);
                break;
        }
    }
    private void changePassword(String oldPs, String newPs){
        RequestParams params = new RequestParams(HttpUrls.CHANGE_PASSWORD);
        params.addBodyParameter("account", getLoginData().getStaffAccount());
        params.addBodyParameter("oldPassword",oldPs);
        params.addBodyParameter("newPassword",newPs);
        params.addBodyParameter("token",getToken());
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    Toast.makeText(ChangePasswordActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                SPUtils.clear(ChangePasswordActivity.this,SAVE_LOGIN_DATA);
                SPUtils.clear(ChangePasswordActivity.this,SAVE_TOKEN);
                SPUtils.clear(ChangePasswordActivity.this,SAVE_HEAD_URI );
                TeacherApplication.getInstance().exit();
                TeacherApplication.getInstance().clearActivity();
                startActivity(new Intent(ChangePasswordActivity.this,LoginActivity.class));
                Toast.makeText(ChangePasswordActivity.this, "修改成功,请重新登录", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

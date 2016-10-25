package com.rimi.schoolteacher.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rimi.schoolteacher.BaseFragment;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.TeacherApplication;
import com.rimi.schoolteacher.activity.AboutActivity;
import com.rimi.schoolteacher.activity.ChangePasswordActivity;
import com.rimi.schoolteacher.activity.LoginActivity;
import com.rimi.schoolteacher.activity.UserinfoActivity;
import com.rimi.schoolteacher.utils.HttpLoadImg;
import com.rimi.schoolteacher.utils.SPUtils;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/7/6.
 * 我的
 */
public class UserinfoFragment extends BaseFragment implements View.OnClickListener{

    @Bind(R.id.name_tv)
    TextView nameTv;
    @Bind(R.id.title_tv)
    TextView titleTv;
    @Bind(R.id.head_img)
    ImageView headImg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_userinfo, null);
        ButterKnife.bind(this, rootView);

        return rootView;
    }
    @OnClick({R.id.userinfo_layout,R.id.change_password_layout,R.id.about_our_layout,R.id.exit_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.userinfo_layout:
                startActivity(new Intent(getActivity(), UserinfoActivity.class));
                break;
            case R.id.change_password_layout:
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                break;
            case R.id.about_our_layout:
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;
            case R.id.exit_btn:
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("是否退出登录")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SPUtils.clear(getActivity(),SAVE_LOGIN_DATA);
                                SPUtils.clear(getActivity(),SAVE_TOKEN);
                                SPUtils.clear(getActivity(),SAVE_HEAD_URI );
                                TeacherApplication.getInstance().clearActivity();
                                TeacherApplication.getInstance().exit();
                                startActivity(new Intent(getActivity(),LoginActivity.class));
                                Toast.makeText(getActivity(),"已退出",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .create()
                        .show();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String sex = getLoginData().getStaffSex();
        if (sex.equals("女")){
            headImg.setImageResource(R.mipmap.girl);
        }
        nameTv.setText(getLoginData().getStaffName());
        titleTv.setText(getLoginData().getStaffPost());
        if (UserinfoActivity.isTrue){
            String headPath = (String) SPUtils.get(getActivity(),SAVE_HEAD_URI,"uri","");
            headImg.setImageURI(Uri.fromFile(new File(headPath)));
            return;
        }
        String url = getLoginData().getStaffPicture();
        if (!TextUtils.isEmpty(url)){
            HttpLoadImg.loadImg(getActivity(),"http://do.rimiedu.com/wxjy/" + url,headImg);
        }
    }
}

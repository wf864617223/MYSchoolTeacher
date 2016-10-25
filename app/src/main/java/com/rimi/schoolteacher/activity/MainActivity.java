package com.rimi.schoolteacher.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rimi.schoolteacher.BaseActivity;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.TeacherApplication;
import com.rimi.schoolteacher.fragment.ClassFragment;
import com.rimi.schoolteacher.fragment.KaoqinFragment;
import com.rimi.schoolteacher.fragment.NotifyFragment;
import com.rimi.schoolteacher.fragment.PijiaFragment;
import com.rimi.schoolteacher.fragment.UserinfoFragment;
import com.rimi.schoolteacher.https.HttpUrls;
import com.rimi.schoolteacher.https.MyCallBack;
import com.rimi.schoolteacher.https.XHttpRequest;
import com.rimi.schoolteacher.permission.MarsQuestion;
import com.rimi.schoolteacher.utils.SPUtils;

import org.xutils.http.RequestParams;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements View.OnClickListener{

//    @Bind(R.id.main_viewpager)
//    MyViewPager mPager;
//    @Bind(R.id.continer)
//    FrameLayout continer;

    private List<Fragment> fragmentList;
    private ClassFragment classFragment ;
    private PijiaFragment pijiaFragment ;
    private KaoqinFragment kaoqinFragment ;
    private NotifyFragment notifyFragment ;
    private UserinfoFragment userinfoFragment ;
    public static Button pijiaBtn;
    private long exitTime = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        MarsQuestion.verifyStoragePermissions(this);
        pijiaBtn = (Button) findViewById(R.id.pijia_btn);
        TeacherApplication.getInstance().addActivity(this);
        init();
        synToken();//同步推送token
    }
    private void init(){
        classFragment = new ClassFragment();
        pijiaFragment = new PijiaFragment();
        kaoqinFragment = new KaoqinFragment();
        notifyFragment = new NotifyFragment();
        userinfoFragment = new UserinfoFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.continer, classFragment);
        ft.commit();
    }
    private void synToken(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("推送同步中...");
        dialog.show();
        String pushToken = (String) SPUtils.get(getApplicationContext(),"push", "push_id", "");
        RequestParams params = new RequestParams(HttpUrls.SYNCH_PUSH_TOKEN);
        params.addBodyParameter("staffId", getStaffId());
        params.addBodyParameter("deviceName", "安卓");
        params.addBodyParameter("pushToken", pushToken);
        params.addBodyParameter("token", getToken());
        XHttpRequest.getInstance().httpPostDefault( this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                dialog.dismiss();
                if (!isSuccess){
                    Toast.makeText(MainActivity.this, "推送同步失败", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(MainActivity.this, "推送同步成功", Toast.LENGTH_LONG).show();

            }
        });
    }
    @OnClick ({R.id.class_btn,R.id.pijia_btn,R.id.kaoqin_btn,R.id.notify_btn,R.id.userinfo_btn})
    @Override
    public void onClick(View v) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.class_btn:
                ft.replace(R.id.continer, classFragment);
                break;
            case R.id.pijia_btn:
                ft.replace(R.id.continer, pijiaFragment);
                break;
            case R.id.kaoqin_btn:
                ft.replace(R.id.continer, kaoqinFragment);
                break;
            case R.id.notify_btn:
                ft.replace(R.id.continer, notifyFragment);
                break;
            case R.id.userinfo_btn:
                ft.replace(R.id.continer, userinfoFragment);
                break;
        }
        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String str = getLoginData().getStaffPost();
        if (str.equals("辅导员")){
            pijiaBtn.setVisibility(View.VISIBLE);
        }else {
            pijiaBtn.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

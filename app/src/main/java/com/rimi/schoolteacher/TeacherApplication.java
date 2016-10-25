package com.rimi.schoolteacher;

import android.app.Activity;
import android.app.Application;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2016/7/5.
 */
public class TeacherApplication extends Application {

    private List<Activity> activityList = new ArrayList<Activity>();

    private static TeacherApplication instance = null;
    public static TeacherApplication getInstance(){
        if (null == instance){
            instance = new TeacherApplication();
        }
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY,"aDZtvMX7zcXyTKuDSS0rpDsj");
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }
    public void clearActivity(){
        activityList.clear();
    }
    public void exit(){
        if (0 == activityList.size()){
            return;
        }
        for (Activity ac :activityList) {
            ac.finish();
        }
    }
}

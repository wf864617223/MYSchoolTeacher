package com.rimi.schoolteacher.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.rimi.schoolteacher.activity.DealActivity;
import com.rimi.schoolteacher.activity.NotifyCommonActivity;
import com.rimi.schoolteacher.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Android on 2016/6/21.
 */
public class MyPushReceiver extends PushMessageReceiver {

    @Override
    public void onBind(Context context, int i, String s, String s1, String s2, String s3) {
        Log.d("result_!", s2);
        SPUtils.put(context,"push", "push_id", s2);
    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override
    public void onMessage(Context context, String s, String s1) {

    }

    @Override
    public void onNotificationClicked(Context context, String title, String description, String customContentString) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            JSONObject json = changeJson(customContentString);
            if (json.has("noticeDate")) {
                intent.setClass(context, NotifyCommonActivity.class);
                intent.putExtra("title", json.getString("flag"));
                intent.putExtra("date", json.getString("noticeDate"));
                intent.putExtra("content", json.getString("noticeContent"));
            }else if (json.has("studentName")){
                intent.setClass(context, DealActivity.class);
                intent.putExtra("studentName", json.getString("studentName"));
                intent.putExtra("startTime", json.getString("askForLeaveStartTime"));
                intent.putExtra("endTime", json.getString("askForLeaveEndTime"));
                intent.putExtra("content", json.getString("askForLeaveContent"));
                intent.putExtra("askForLeaveId", json.getString("askForLeaveId"));
            }
            context.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotificationArrived(Context context, String s, String s1, String s2) {

    }
    private JSONObject changeJson(String str) throws JSONException {
        JSONObject job = new JSONObject(str);
        return job;
    }
}

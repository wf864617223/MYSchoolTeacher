package com.rimi.schoolteacher.https;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.rimi.schoolteacher.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by Android on 2016/6/6.
 * xUtils 网络请求封装
 */
public class XHttpRequest {

    private static XHttpRequest instance = null;
    public synchronized static XHttpRequest getInstance(){
        if (null == instance){
            instance = new XHttpRequest();
        }
        return instance;
    }
    //get请求
    public void httpGet(final Context mContext, RequestParams params, final MyCallBack myCallBack){
        CommonUtils.getLoading(mContext, "加载中....");
        x.http().get(params, new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                CommonUtils.dismiss();
                try {
                    JSONObject job = new JSONObject(result);
                    if (job.getInt("status") != 0){
                        if (job.getInt("status") == 401){
                            Toast.makeText(mContext, "登录信息已失效，请重新登录", Toast.LENGTH_LONG).show();
                        }
                        myCallBack.onCallBack(false, result);
                        return;
                    }
                    myCallBack.onCallBack(true, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.dismiss();
                myCallBack.onCallBack(false,ex.getMessage());
//                ex instanceof HttpException
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    //Post请求
    public void httpPost(final Context mContext, RequestParams params, final MyCallBack myCallBack){
        CommonUtils.getLoading(mContext,"加载中....");
        String str = params.getUri();
        if (str.equals(HttpUrls.LOGIN)){
            CommonUtils.setCancelable(false);
        }
        x.http().post(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                CommonUtils.dismiss();
                try {
                    Log.d("result" ,result + "-------------");
                    JSONObject job = new JSONObject(result);
                    if (job.getInt("status") != 0){
                        if (job.getInt("status") == 401){
                            Toast.makeText(mContext, "登录信息已失效，请重新登录", Toast.LENGTH_LONG).show();
                        }
                        if (job.getInt("status") == 407){
                            Toast.makeText(mContext, "当前课程已签到", Toast.LENGTH_LONG).show();
                        }
                        myCallBack.onCallBack(false, result);
                        return;
                    }
                    myCallBack.onCallBack(true, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.dismiss();
                myCallBack.onCallBack(false,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    public void httpPostDefault(final Context mContext, RequestParams params, final MyCallBack myCallBack){
        x.http().post(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                try {
                    Log.d("result" ,result + "-------------");
                    JSONObject job = new JSONObject(result);
                    if (job.getInt("status") != 0){
                        if (job.getInt("status") == 401){
                            Toast.makeText(mContext, "登录信息已失效，请重新登录", Toast.LENGTH_LONG).show();
                        }
                        myCallBack.onCallBack(false, result);
                        return;
                    }
                    myCallBack.onCallBack(true, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.dismiss();
                myCallBack.onCallBack(false,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    public void httpGetDefault(final Context mContext, RequestParams params, final MyCallBack myCallBack){
        x.http().get(params,new Callback.CommonCallback<String>(){

            @Override
            public void onSuccess(String result) {
                try {
                    Log.d("result" ,result + "-------------");
                    JSONObject job = new JSONObject(result);
                    if (job.getInt("status") != 0){
                        if (job.getInt("status") == 401){
                            Toast.makeText(mContext, "登录信息已失效，请重新登录", Toast.LENGTH_LONG).show();
                        }
                        myCallBack.onCallBack(false, result);
                        return;
                    }
                    myCallBack.onCallBack(true, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                CommonUtils.dismiss();
                myCallBack.onCallBack(false,ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


}

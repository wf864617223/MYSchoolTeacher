package com.rimi.schoolteacher.customs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.bean.LoginResponseData;
import com.rimi.schoolteacher.https.HttpUrls;
import com.rimi.schoolteacher.https.MyCallBack;
import com.rimi.schoolteacher.https.XHttpRequest;
import com.rimi.schoolteacher.utils.SPUtils;

import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/7/14.
 */
public class MapSignOutDialog extends Dialog implements View.OnClickListener, LocationSource, AMapLocationListener {

    @Bind(R.id.mapview)
    MapView mMapView;
    @Bind(R.id.signout_btn)
    Button signoutBtn;
    @Bind(R.id.signout_time_tv)
    TextView signoutTimeTv;
    @Bind(R.id.signout_code)
    TextView signoutCode;
    @Bind(R.id.signout_adds_tv)
    TextView signoutAddsTv;
    @Bind(R.id.classname_tv)
    TextView classNameTv;

    private AMap aMap;
    private AMapLocationClient mapLocationClient;
    private OnLocationChangedListener mListener;
    private String curriculumId;
    private String longitude;
    private String latitude;
    private Context context;
    private Map<String,String> map = new HashMap<String,String>();
    public MapSignOutDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_signabout,null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);

        ButterKnife.bind(this,view);
        aMap = mMapView.getMap();
        initMap();
        mMapView.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dme = context.getResources().getDisplayMetrics();
        lp.width = (int) (dme.widthPixels * 0.95);
        lp.height = (int) (dme.heightPixels * 0.95);
        dialogWindow.setAttributes(lp);
    }

    private void initMap(){
        mapLocationClient = new AMapLocationClient(context);
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        MyLocationStyle style = new MyLocationStyle();
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(),R.mipmap.map_sign));
        style.myLocationIcon(bitmap);
        style.strokeColor(Color.BLACK);
        style.strokeWidth(0);
        aMap.setMyLocationStyle(style);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setZoomGesturesEnabled(false);
        aMap.getUiSettings().setScaleControlsEnabled(false);
        aMap.getUiSettings().setScrollGesturesEnabled(false);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setNeedAddress(true);
        option.setOnceLocation(false);

        mapLocationClient.setLocationOption(option);
        mapLocationClient.setLocationListener(this);
    }
    @OnClick({R.id.signout_btn,R.id.radio_one,R.id.radio_two,R.id.radio_three})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signout_btn:
                String mnu = map.get("isSelected");
                if (TextUtils.isEmpty(mnu)){
                    Toast.makeText(context,"请选择停止签退时间",Toast.LENGTH_LONG).show();
                    return;
                }
                signout();
                break;
            case R.id.radio_one:
                map.put("isSelected","15");
                break;
            case R.id.radio_two:
                map.put("isSelected","10");
                break;
            case R.id.radio_three:
                map.put("isSelected","5");
                break;
        }
    }
    public void setClassName(String className){
        if (TextUtils.isEmpty(className)){
            classNameTv.setVisibility(View.INVISIBLE);
        }
        classNameTv.setText(className);
    }
    public void setSignoutTime(String time){
        signoutTimeTv.setText(time);
    }
    public void setSignoutCode(String code){
        signoutCode.setText(code);
    }
    public void setCurriculumId(String curriculumId) {
        this.curriculumId = curriculumId;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        mapLocationClient.startLocation();
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        String str = aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getStreetNum();
        if(mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                signoutAddsTv.setText(" " + str);
                longitude = String.valueOf(aMapLocation.getLongitude());
                latitude = String.valueOf(aMapLocation.getLatitude());
                LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f);
                aMap.moveCamera(cameraUpdate);
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                signoutAddsTv.setText(errText);
            }
        }
        mapLocationClient.stopLocation();
    }
    private void signout(){
        if (longitude == null || latitude == null){
            Toast.makeText(context,"未定位成功暂不能签到",Toast.LENGTH_LONG).show();
            return;
        }
        LoginResponseData data =  SPUtils.getObject(context, "save_login_data", "login_response");
        RequestParams params = new RequestParams(HttpUrls.SAVE_TEACHER_SIGN_DATA);
        params.addBodyParameter("staffId",String.valueOf(data.getStaffId()));
        params.addBodyParameter("curriculumId",curriculumId);
        params.addBodyParameter("rollcallTime",signoutTimeTv.getText().toString());
        params.addBodyParameter("rollcallBeLate",map.get("isSelected"));
        params.addBodyParameter("rollcallSignCode",signoutCode.getText().toString());
        params.addBodyParameter("rollcallLongitude", longitude);
        params.addBodyParameter("rollcallLatitude", latitude);
        params.addBodyParameter("rollcallType", "签退");
        params.addBodyParameter("token",data.getToken());
        XHttpRequest.getInstance().httpPost(context,params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    Toast.makeText(context,"签退失败",Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(context,"签退成功",Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
    }
}

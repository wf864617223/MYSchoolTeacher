package com.rimi.schoolteacher.fragment;

import android.annotation.TargetApi;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
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
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.rimi.schoolteacher.BaseFragment;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.https.HttpUrls;
import com.rimi.schoolteacher.https.MyCallBack;
import com.rimi.schoolteacher.https.XHttpRequest;
import com.rimi.schoolteacher.utils.CommonUtils;
import com.rimi.schoolteacher.utils.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/7/6.
 * 考勤
 */
public class KaoqinFragment extends BaseFragment implements LocationSource, AMapLocationListener, View.OnClickListener {

    @Bind(R.id.mapview)
    MapView mapView;
    @Bind(R.id.location_adds_tv)
    TextView loactionAddsTv;
    @Bind(R.id.scrollView)
    ScrollView mScrollView;
    @Bind(R.id.sign_time_tv)
    TextView signTimeTv;
    @Bind(R.id.work_adds_tv)
    TextView workAddsTv;
    @Bind(R.id.sign_btn)
    Button signBtn;
    @Bind(R.id.inside_kaoqin_btn)
    Button insideBtn;
    @Bind(R.id.outside_kaoqin_btn)
    Button outsideBtn;
    @Bind(R.id.tishi_tv)
    TextView tishiTv;
    @Bind(R.id.sign)
    TextView signTv;
    @Bind(R.id.adds)
    TextView addsTv;

    private AMapLocationClient mapLocationClient ;
    private OnLocationChangedListener mListener;
    private AMap aMap;
    private String longitude;//经度
    private String latitude;//纬度
    private boolean isInside = true;//室内办公OR出差
    private boolean isSign = true;//签到OR签退
    private boolean inSideIsSign = true;
    private boolean outSideIsSign = true;
    private String str = null;//定位地点
    private double houseLongitude;
    private double houseLatitude;
    private String rollcallStaffId;

    private String hoserName ;
    private static final double ERROR_CODE = 0.0002;
    private double errorDistance;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_kaoqin, null);
        ButterKnife.bind(this, rootView);
        mapView.onCreate(savedInstanceState);
        getErrorDistance();
        Log.d("result", isSign + "________>");
//        isSign = (boolean) SPUtils.get(getActivity(),SAVE_SIGN_DATA,"isSign",true);
//        inSideIsSign = (boolean) SPUtils.get(getActivity(),SAVE_SIGN_DATA, "inSideIsSign", true);
//        outSideIsSign = (boolean) SPUtils.get(getActivity(),SAVE_SIGN_DATA, "outSideIsSign", true);
//        if (isSign){
//            signBtn.setText(getResources().getString(R.string.sign));
//        }else {
//            signBtn.setText(getResources().getString(R.string.signout));
//        }
        getData();
        return rootView;
    }

    private void getData(){//获取校内考勤信息
        RequestParams params = new RequestParams(HttpUrls.GET_KAOQIN_DATA);
        params.addBodyParameter("staffId",getStaffId());
        params.addBodyParameter("token",getToken());
        XHttpRequest.getInstance().httpGet(getActivity(), params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    JSONObject result = response.getJSONObject("result");
                    JSONObject house = result.getJSONObject("house");
                    houseLongitude = house.getDouble("houseLongitude");
                    houseLatitude = house.getDouble("houseLatitude");
                    signTimeTv.setText(CommonUtils.getNowDateCommon());
                    hoserName = house.getString("houseName");
                    workAddsTv.setText(house.getString("houseName"));
                    boolean ifSignIn = result.getBoolean("ifSignIn");
                    boolean ifSignOff = result.getBoolean("ifSignOff");
                    judgeState(ifSignIn,ifSignOff);
                    if (ifSignIn & ifSignOff){
                        signBtn.setText("已签退");
                        signBtn.setClickable(false);
                        signBtn.setBackground(getResources().getDrawable(R.drawable.btn_unfocuse_backg));
                    }else if (!ifSignIn & !ifSignOff){
                        signBtn.setClickable(true);
                        signBtn.setText("签到");
                        signBtn.setBackground(getResources().getDrawable(R.drawable.btn_sign_backg));
                    }else if (ifSignIn & !ifSignOff){
                        signBtn.setClickable(true);
                        signBtn.setText("签退");
                        signBtn.setBackground(getResources().getDrawable(R.drawable.btn_sign_backg));
                    }
                    initMap();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getErrorDistance(){
        RequestParams params = new RequestParams(HttpUrls.GET_ERROR_DISTANCE);
        XHttpRequest.getInstance().httpGetDefault(getActivity(), params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    errorDistance = response.getDouble("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getOutsideData(){
        RequestParams params = new RequestParams(HttpUrls.GET_OUTSIDE_KAOQIN_DATA);
        params.addBodyParameter("staffId", getStaffId());
        params.addBodyParameter("token",getToken());
        XHttpRequest.getInstance().httpGet(getActivity(), params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    return;
                }
                try {
                    JSONObject response = new JSONObject(obj.toString());
                    signTimeTv.setText(CommonUtils.getNowDateCommon());
                    if (response.isNull("result")){
                        mapLocationClient.stopLocation();
                        workAddsTv.setText(null);
                        loactionAddsTv.setText(null);
                        mapView.setVisibility(View.GONE);
                    }else {
                        JSONObject result = response.getJSONObject("result");
                        JSONObject rollcallStaff = result.getJSONObject("rollcallStaff");
                        rollcallStaffId = rollcallStaff.getString("rollcallStaffId");
                        houseLongitude = rollcallStaff.getDouble("rollcallStaffLongitude");
                        houseLatitude = rollcallStaff.getDouble("rollcallStaffLatitude");
                        workAddsTv.setText(rollcallStaff.getString("rollcallStaffAddress"));
                        boolean ifSignIn = result.getBoolean("ifSignIn");
                        boolean ifSignOff = result.getBoolean("ifSignOff");
                        judgeState(ifSignIn,ifSignOff);
                        if (ifSignIn & ifSignOff){
                            signBtn.setText("已签退");
                            signBtn.setClickable(false);
                            signBtn.setBackground(getResources().getDrawable(R.drawable.btn_unfocuse_backg));
                        }else if (!ifSignIn & !ifSignOff){
                            signBtn.setText("签到");
                            signBtn.setClickable(true);
                            signBtn.setBackground(getResources().getDrawable(R.drawable.btn_sign_backg));
                        }else if (ifSignIn & !ifSignOff){
                            signBtn.setClickable(true);
                            signBtn.setText("签退");
                            signBtn.setBackground(getResources().getDrawable(R.drawable.btn_sign_backg));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void initMap(){//初始化地图
        mapLocationClient = new AMapLocationClient(getActivity());
        aMap = mapView.getMap();
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
        MyLocationStyle style = new MyLocationStyle();
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.map_sign));
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
        MarkerOptions options = new MarkerOptions();
        LatLng latLng2 = new LatLng(houseLatitude, houseLongitude);
        options.title("工作地点");
        BitmapDescriptor bitmap1 = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.map_work));
        options.icon(bitmap1);
        options.visible(true);
        Marker marker = aMap.addMarker(options);
        marker.setPosition(latLng2);
        mapLocationClient.setLocationOption(option);
        mapLocationClient.setLocationListener(this);
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
        str = aMapLocation.getCity() + aMapLocation.getDistrict() + aMapLocation.getStreet() + aMapLocation.getStreetNum();
        if(mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                longitude = String.valueOf(aMapLocation.getLongitude());
                latitude = String.valueOf(aMapLocation.getLatitude());
                double distance = CommonUtils.getDistance(houseLongitude, houseLatitude, aMapLocation.getLongitude(), aMapLocation.getLatitude());
                if (distance < (errorDistance * 5) ){
                    loactionAddsTv.setText(str);
                    LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f);
                    aMap.moveCamera(cameraUpdate);
                    mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                }else {
//                    LatLng latLng = new LatLng(houseLongitude - ERROR_CODE, houseLongitude);
//                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f);
//                    aMap.moveCamera(cameraUpdate);
                    loactionAddsTv.setText(hoserName);
                    MarkerOptions options = new MarkerOptions();
                    LatLng latLng2 = new LatLng(houseLatitude - ERROR_CODE, houseLongitude);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng2, 17f);
                    aMap.moveCamera(cameraUpdate);
                    BitmapDescriptor bitmap1 = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.map_sign));
                    options.icon(bitmap1);
                    options.visible(true);
                    Marker marker = aMap.addMarker(options);
                    marker.setPosition(latLng2);
                }
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                loactionAddsTv.setText(errText);
            }
        }
        mapLocationClient.stopLocation();
    }

//    @Override
//    public void onTouch(MotionEvent motionEvent) {
//        int action = motionEvent.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_UP:
//                mScrollView.requestDisallowInterceptTouchEvent(false);
//                break;
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_MOVE:
//                mScrollView.requestDisallowInterceptTouchEvent(true);
//                break;
//        }
//    }
    @OnClick({R.id.inside_kaoqin, R.id.outside_kaoqin,R.id.sign_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.inside_kaoqin:
                getData();
                mapView.setVisibility(View.VISIBLE);
                insideBtn.setTextColor(getResources().getColor(R.color.title_bg));
                outsideBtn.setTextColor(getResources().getColor(R.color.blacks));
                isInside = true;
//                if (inSideIsSign){
//                    signBtn.setText(getResources().getString(R.string.sign));
//                }else {
//                    signBtn.setText(getResources().getString(R.string.signout));
//                }
                break;
            case R.id.outside_kaoqin:
                getOutsideData();
                insideBtn.setTextColor(getResources().getColor(R.color.blacks));
                outsideBtn.setTextColor(getResources().getColor(R.color.title_bg));
                isInside = false;
//                if (outSideIsSign){
//                    signBtn.setText(getResources().getString(R.string.sign));
//                }else {
//                    signBtn.setText(getResources().getString(R.string.signout));
//                }
                break;
            case R.id.sign_btn:
                if (TextUtils.isEmpty(str)){
                    Toast.makeText(getActivity(),"未定位成功请稍后再试",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isInside){
                    signAbout(signBtn.getText().toString().trim(),"办公");
                }else {
                    signAbout(signBtn.getText().toString().trim(),"外出");
                }
                break;
        }
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void signAbout(final String signORsignout, final String inOrOutSide){
        signBtn.setText("处理中");
        signBtn.setClickable(false);
        signBtn.setBackground(getResources().getDrawable(R.drawable.btn_unfocuse_backg));
        RequestParams params = new RequestParams(HttpUrls.SIGN_ABOUT);
        params.addBodyParameter("staffId", getStaffId());
        params.addBodyParameter("rollcallStaffAddress", str);
        params.addBodyParameter("rollcallStaffTime", CommonUtils.getNowDateCommon());
        params.addBodyParameter("rollcallStaffLatitude", latitude);
        params.addBodyParameter("rollcallStaffLongitude", longitude);
        params.addBodyParameter("rollcallType", signORsignout);
        params.addBodyParameter("rollcallStaffType", inOrOutSide);
        params.addBodyParameter("rollcallStaffId",null == rollcallStaffId ? "0" : rollcallStaffId);
        params.addBodyParameter("token",getToken());
        XHttpRequest.getInstance().httpPost(getActivity(), params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                signBtn.setClickable(true);
                signBtn.setBackground(getResources().getDrawable(R.drawable.btn_sign_backg));
                if (inOrOutSide.equals("办公")){
                    getData();
                }else {
                    getOutsideData();
                }
                if (!isSuccess){
                    try {
                        JSONObject response = new JSONObject(obj.toString());
                        if (!response.isNull("status")){
                            if (response.getInt("status") == 408 || response.getInt("status") == 412){
                                Toast.makeText(getActivity(), response.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), "签到失败",Toast.LENGTH_LONG).show();
//                    if (inOrOutSide.equals("办公") & inSideIsSign){
//                        signBtn.setText("签到");
//                    }else if (inOrOutSide.equals("办公") & !inSideIsSign){
//                        signBtn.setText("签退");
//                    }
//                    if (inOrOutSide.equals("外出") & outSideIsSign){
//                        signBtn.setText("签到");
//                    }else if (inOrOutSide.equals("外出") & outSideIsSign){
//                        signBtn.setText("签退");
//                    }
                    return;
                }
                if (inOrOutSide.equals("办公") & inSideIsSign){
                    inSideIsSign = false;
                    SPUtils.put(getActivity(),SAVE_SIGN_DATA, "inSideIsSign", false);
                }else if (inOrOutSide.equals("办公") & !inSideIsSign){
                    inSideIsSign = true;
                    SPUtils.put(getActivity(),SAVE_SIGN_DATA, "inSideIsSign", true);
                }
                if (inOrOutSide.equals("外出") & outSideIsSign){
                    outSideIsSign = false;
                    SPUtils.put(getActivity(),SAVE_SIGN_DATA, "outSideIsSign", false);
                }else if (inOrOutSide.equals("外出") & outSideIsSign){
                    outSideIsSign = true;
                    SPUtils.put(getActivity(),SAVE_SIGN_DATA, "outSideIsSign", true);
                }
//                judgeState(isSign);
                if (isSign){
                    SPUtils.put(getActivity(),SAVE_SIGN_DATA, "isSign", false);
                    isSign = false;
                }else {
                    SPUtils.put(getActivity(),SAVE_SIGN_DATA, "isSign", true);
                    isSign = true;
                }
            }
        });
    }
    private void judgeState(boolean isSignIn, boolean isSignOff){
        if (isSignIn & !isSignOff){
            signTv.setText(getResources().getString(R.string.signout_time));
            addsTv.setText(getResources().getString(R.string.signout_adds));
            signBtn.setText(getResources().getString(R.string.signout));
        }else {
            signTv.setText(getResources().getString(R.string.sign_time));
            addsTv.setText(getResources().getString(R.string.sign_adds));
            signBtn.setText(getResources().getString(R.string.sign));
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.setVisibility(View.VISIBLE);
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.setVisibility(View.INVISIBLE);
        mapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
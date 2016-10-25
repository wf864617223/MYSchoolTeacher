package com.rimi.schoolteacher.fragment;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.rimi.schoolteacher.BaseFragment;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.https.HttpUrls;
import com.rimi.schoolteacher.https.MyCallBack;
import com.rimi.schoolteacher.https.XHttpRequest;
import com.rimi.schoolteacher.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/8/9.
 */
public class InsideKaoqinFragment extends BaseFragment implements LocationSource, AMapLocationListener, View.OnClickListener {

    @Bind(R.id.mapview)
    MapView mapView;
    @Bind(R.id.location_adds_tv)
    TextView loactionAddsTv;
    @Bind(R.id.sign_time_tv)
    TextView signTimeTv;
    @Bind(R.id.work_adds_tv)
    TextView workAddsTv;
    @Bind(R.id.sign_btn)
    Button signBtn;
    @Bind(R.id.sign)
    TextView signTv;
    @Bind(R.id.adds)
    TextView addsTv;

    private AMap aMap;
    private String longitude;//经度
    private String latitude;//纬度
    private AMapLocationClient mapLocationClient;
    private OnLocationChangedListener mListener;
    private double houseLatitude; //纬度
    private double houseLongitude; //经度
    private String str;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_inside, null);
        ButterKnife.bind(this,rootView);
        mapView.onCreate(savedInstanceState);
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
                    houseLongitude = result.getDouble("houseLongitude");
                    houseLatitude = result.getDouble("houseLatitude");
                    signTimeTv.setText(CommonUtils.getNowDateCommon());
                    workAddsTv.setText(result.getString("houseName"));
                    initMap();
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
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
                loactionAddsTv.setText(str);
                longitude = String.valueOf(aMapLocation.getLongitude());
                latitude = String.valueOf(aMapLocation.getLatitude());
                LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f);
                aMap.moveCamera(cameraUpdate);
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                loactionAddsTv.setText(errText);
            }
        }
        mapLocationClient.stopLocation();
    }
    @OnClick({R.id.sign_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_btn:
                signAbout("","");
                break;
        }
    }
    private void signAbout(final String signORsignout, final String inOrOutSide){
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
//        params.addBodyParameter("rollcallStaffId",null == rollcallStaffId ? "0" : rollcallStaffId);
        params.addBodyParameter("token",getToken());
        XHttpRequest.getInstance().httpPost(getActivity(), params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                signBtn.setClickable(true);
                signBtn.setBackground(getResources().getDrawable(R.drawable.btn_sign_backg));
                if (!isSuccess){
                    Toast.makeText(getActivity(), "签到失败",Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getActivity(), "签到成功",Toast.LENGTH_LONG).show();
            }
        });
    }
}

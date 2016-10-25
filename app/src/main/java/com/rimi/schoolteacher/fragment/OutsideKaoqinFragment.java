package com.rimi.schoolteacher.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.MapView;
import com.rimi.schoolteacher.BaseFragment;
import com.rimi.schoolteacher.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Android on 2016/8/9.
 */
public class OutsideKaoqinFragment extends BaseFragment {

    @Bind(R.id.mapview)
    MapView mapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.frag_outside, null);
        ButterKnife.bind(this, rootView);
        mapView.onCreate(savedInstanceState);
        return rootView;
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
}

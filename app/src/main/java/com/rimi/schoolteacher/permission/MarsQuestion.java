package com.rimi.schoolteacher.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Android on 2016/9/20.
 */
public class MarsQuestion {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    public static void verifyStoragePermissions(Activity activity) {
        // 检查是否有读写权限
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int locationpermission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (locationpermission != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，则提示用户
            ActivityCompat.requestPermissions(activity, PERMISSIONS_LOCATION, REQUEST_EXTERNAL_STORAGE);
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，则提示用户
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }

    }
}

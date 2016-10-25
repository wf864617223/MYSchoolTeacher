package com.rimi.schoolteacher.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.rimi.schoolteacher.BaseActivity;
import com.rimi.schoolteacher.R;
import com.rimi.schoolteacher.TeacherApplication;
import com.rimi.schoolteacher.bean.ClassData;
import com.rimi.schoolteacher.https.HttpUrls;
import com.rimi.schoolteacher.https.MyCallBack;
import com.rimi.schoolteacher.https.XHttpRequest;
import com.rimi.schoolteacher.permission.MarsQuestion;
import com.rimi.schoolteacher.utils.HttpLoadImg;
import com.rimi.schoolteacher.utils.SPUtils;

import org.xutils.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Android on 2016/7/6.
 */
public class UserinfoActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.head_img)
    ImageView headImg;
    @Bind(R.id.name_tv)
    TextView nameTv;
    @Bind(R.id.phone_tv)
    TextView phoneTv;
    @Bind(R.id.worker_tv)
    TextView workerTv;
    @Bind(R.id.classname_tv)
    TextView classNameTv;
    @Bind(R.id.visible_layout)
    ViewGroup visibleLayout;
    @Bind(R.id.bottom_layout)
    ViewGroup bottomLayout;

    private static final int CAMERA_REQUEST = 1;
    public static boolean isTrue = false;
    private PopupWindow mPop;
    private View popupView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        MarsQuestion.verifyStoragePermissions(this);
        TeacherApplication.getInstance().addActivity(this);
        nameTv.setText(getLoginData().getStaffName());
        phoneTv.setText(getLoginData().getStaffTelphone());
        workerTv.setText(getLoginData().getStaffPost());
        String sex = getLoginData().getStaffSex();
        if (sex.equals("女")){
            headImg.setImageResource(R.mipmap.girl);
        }
        List<ClassData> mList = SPUtils.getObject(this,SAVE_CLASS_NAME,CLASS_LIST);
        if (mList == null || mList.size() == 0){
            visibleLayout.setVisibility(View.GONE);
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mList.size(); i++) {
            if (i == (mList.size() - 1)){
                sb.append(mList.get(i).getClassName());
            }else {
                sb.append(mList.get(i).getClassName() + " 、");
            }
        }
        classNameTv.setText(sb.toString());

    }
    @OnClick({R.id.back_img,R.id.change_header_layout,R.id.photo_btn,R.id.picture_btn,R.id.null_btn})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_img:
                finish();
                break;
            case R.id.change_header_layout:
                bottomLayout.setVisibility(View.GONE);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra("return-data", true);
                startActivityForResult(intent, CAMERA_REQUEST);
//                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
//                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//                startActivityForResult(intent1, CAMERA_REQUEST);
                break;
            case R.id.photo_btn:
//                Intent intent1 = new Intent();
//                intent1.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent1.addCategory(Intent.CATEGORY_DEFAULT);
//                startActivityForResult(intent1, CAMERA_REQUEST);
                break;
            case R.id.picture_btn:
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("image/*");
//                intent.putExtra("return-data", true);
//                startActivityForResult(intent, CAMERA_REQUEST);
                break;
            case R.id.null_btn:
                bottomLayout.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == CAMERA_REQUEST){
//                File cropFile = new File(Environment.getExternalStorageDirectory() + "/Angel" + System.currentTimeMillis() + ".jpg");
//                File temp = new File(Environment.getExternalStorageDirectory() + "/Angel");
//                if (!temp.exists()) {
//                    temp.mkdir();
//                }
                startZoomPic(data.getData());
            }
            if (requestCode == 3){
                if (null != data){
                    String fileName = Environment.getExternalStorageDirectory() + "/Angel/" + System.currentTimeMillis() + ".jpg";//图片名字
                    Bitmap bm = data.getExtras().getParcelable("data");
                    headImg.setImageBitmap(bm);
                    cachePic(bm,fileName );
                    SPUtils.put(this,SAVE_HEAD_URI,"uri",fileName);
                    upLoadHead(fileName);
                }

//                Uri uri = data.getData();
//                String[] proj = { MediaStore.Images.Media.DATA };
//                Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);
//                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                actualimagecursor.moveToFirst();
//                String img_path = actualimagecursor.getString(actual_image_column_index);
//                File file = new File(img_path);
//                headImg.setImageURI(uri);
//                SPUtils.put(this,SAVE_HEAD_URI,"uri",img_path);
//                RequestParams params = new RequestParams(HttpUrls.UPLOAD_HEAD);
//                params.addBodyParameter("staffId",getStaffId());
//                params.addBodyParameter("file",file);
//                params.addBodyParameter("token",getToken());
//                params.setMultipart(true);
//                XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
//                    @Override
//                    public void onCallBack(boolean isSuccess, Object obj) {
//                        if (!isSuccess){
//                            Toast.makeText(UserinfoActivity.this, "设置失败", Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                        isTrue = true;
//                        Toast.makeText(UserinfoActivity.this, "设置成功", Toast.LENGTH_LONG).show();
//                        String headUri = (String) SPUtils.get(getApplicationContext(),SAVE_HEAD_URI,"uri","");
//                        headImg.setImageURI(Uri.parse(headUri));
//                    }
//                });
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onResume() {
        super.onResume();
        bottomLayout.setVisibility(View.GONE);
        if (isTrue){
            String headPath = (String) SPUtils.get(getApplicationContext(),SAVE_HEAD_URI,"uri","");
            headImg.setImageURI(Uri.fromFile(new File(headPath)));
            return;
        }
        String url = getLoginData().getStaffPicture();
        if (!TextUtils.isEmpty(url)){
            HttpLoadImg.loadImg(this,"http://do.rimiedu.com/wxjy/" + url,headImg);
        }
    }
    private void startZoomPic(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//可裁剪
//                intent.putExtra("output", Uri.fromFile(cropFile));
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);//若为false则表示不返回数据
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 3);
    }
    private void upLoadHead(String path){
        RequestParams params = new RequestParams(HttpUrls.UPLOAD_HEAD);
        params.addBodyParameter("staffId",getStaffId());
        params.addBodyParameter("file",new File(path));
        params.addBodyParameter("token",getToken());
        params.setMultipart(true);
        XHttpRequest.getInstance().httpPost(this, params, new MyCallBack() {
            @Override
            public void onCallBack(boolean isSuccess, Object obj) {
                if (!isSuccess){
                    Toast.makeText(UserinfoActivity.this, "设置失败", Toast.LENGTH_LONG).show();
                    return;
                }
                isTrue = true;
                Toast.makeText(UserinfoActivity.this, "设置成功", Toast.LENGTH_LONG).show();
                String headPath = (String) SPUtils.get(getApplicationContext(),SAVE_HEAD_URI,"uri","");
                headImg.setImageURI(Uri.fromFile(new File(headPath)));
            }
        });
    }
    private void cachePic(Bitmap bm, String fileName){
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            System.out.println("SD卡不可用");
            return;
        }
        FileOutputStream b = null;
        File temp = new File(Environment.getExternalStorageDirectory() + "/Angel/");
            if (!temp.exists()) {
                temp.mkdir();
             }
//        String fileName = Environment.getExternalStorageDirectory() + "/Angel/" + System.currentTimeMillis() + ".jpg";//图片名字
        try {
            b = new FileOutputStream(fileName);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

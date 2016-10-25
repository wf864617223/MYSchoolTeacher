package com.rimi.schoolteacher.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * TODO : 图片加载器(封装图片加载,在以后需要的时候可以随时换)
 * Created by hb on 2016-05-14.
 */
public class HttpLoadImg {
    /**
     * TODO: 加载图片,
     *
     * @param activity  传acitivity是为了在 onstop方法的时候停止加载, 在onresume方法中继续加载
     * @param url
     * @param imageView
     */
    public static void loadImg(Activity activity, String url, ImageView imageView) {
        Glide.with(activity).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * TODO: 加载图片,
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImg(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    /**
     * 加载本地图片资源ID
     */
    public static void loadImg(Context context, Integer resId, ImageView imageView) {
        Glide.with(context).load(resId).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }
//    public static  void downLoadImg(final Context mContext, final String testUrl, final SubsamplingScaleImageView imageView){
//        Glide.with(mContext)
//                .load(testUrl)
//                .asBitmap()
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        File folder = new File(Environment.getExternalStorageDirectory(), "Angel");
//                        if (!folder.exists()){
//                            folder.mkdir();
//                        }
//                        File file = new File(Environment.getExternalStorageDirectory(), "Angel/" +testUrl.replace("/","_"));
//                        if (!file.exists()) {
//                            try {
//                                file.createNewFile();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        FileOutputStream fout = null;
//                        try {
//                            //保存图片
//                            fout = new FileOutputStream(file);
//                            resource.compress(Bitmap.CompressFormat.JPEG, 100, fout);
//                            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
//                            // 将保存的地址给SubsamplingScaleImageView,这里注意设置ImageViewState
//                            imageView.setImage(ImageSource.bitmap(bm), new ImageViewState(0.5F, new PointF(0, 0), 0));
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        } finally {
//                            try {
//                                if (fout != null) fout.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                });
//    }
    /**
     * 下载图片转圆形
     */
//    public static void loadCircleImg(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).transform(new GlideCircleTransform(context)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
//    }

    /**
     * 下载图片转圆角
     */
//    public static void loadRoundImg(Context context, String url, ImageView imageView) {
////        Glide.with(context).load(url).transform(new GlideRoundTransform(context)).into(imageView);
//        Glide.with(context).load(url).transform(new GlideRoundTransform(context, 10)).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
//
//    }
}

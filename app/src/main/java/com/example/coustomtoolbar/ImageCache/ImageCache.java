package com.example.coustomtoolbar.ImageCache;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;


/**
 * Created by SEELE on 2017/7/25.
 */

public class ImageCache {
    private static final String TAG = "ImageCache";
    private static ImageCache mImageCache;
    private  LruCache<String,Bitmap> mLruCache;
    private com.jakewharton.disklrucache.DiskLruCache diskLruCache = null;
    private int MaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private int cacheSize = MaxMemory / 8;
    private ImageDiskLruCache imageDiskLru;
    private int maxWidth;
    private int width;
    private int height;
    private ImageDispatcher mImageDispatcher;
    private Context context;
    private ImageCache(Context context) {
        this.context = context;
        getScreenWidth(context);
        imageDiskLru = ImageDiskLruCache.getImageDiskLruInstance(context);
        mImageDispatcher = new ImageDispatcher();

    }
    public static ImageCache getInstance(Context context){
        if (mImageCache == null){
            mImageCache = new ImageCache(context);
        }
        return mImageCache;
    }

    public void   getScreenWidth(Context context){
        DisplayMetrics displayMetrics  = context.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels / 3;
        height = displayMetrics.heightPixels / 3;
    }
    public void setMaxWidth(int maxWidth){
        this.maxWidth = maxWidth;
    }

    public void showImage(final ImageView imageView, String url) throws ExecutionException, InterruptedException {

        if (imageDiskLru.getBitmapFromCache(url) != null){
            Log.e(TAG, "showImage: " + "fro cache" );
            if (imageView.getTag() == url){
                imageView.setImageBitmap(imageDiskLru.getBitmapFromCache(url));
            }
        }
        else {
            Glide.with(context)
                    .load(url)
                    .into(imageView);

            //new BitmapTask(url,imageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,url);
            //getBitmapFromNetWork(url,imageView);
            //executor1.execute(url);
            /*
            mImageDispatcher.enqueue(new ImageAsyncCall(imageDiskLru, url, new ImageCallback() {
                @Override
                public void OnSuccess(Bitmap bitmap, String url) {
                    if (imageView.getTag() == url){
                        //imageDiskLru.addBitmapToMemoryCache(url,bitmap);
                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void OnFailure(String url) {

                }

                @Override
                public void OnLoading(String url) {

                }
            }));
            */
        }
    }

    private  Bitmap decodeSampleBitmapFromResource(FileDescriptor in, int reqWidth,int height){
        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeStream(in,null,options);
        BitmapFactory.decodeFileDescriptor(in,null,options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth,height);

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(in,null,options);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(in,null,options);
    }
    private  int calculateInSampleSize(BitmapFactory.Options options,
                                       int reqWidth,int reqHeight){

        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        Log.e(TAG, "calculateInSampleSize: " );
        if ( width > reqWidth){
            final int widthRatio = Math.round((float)width / (float)reqWidth);
            inSampleSize = widthRatio;
            return inSampleSize;
        }
        if (height > reqHeight){
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            inSampleSize = heightRatio;
            return inSampleSize;
        }
        return inSampleSize;
    }
}

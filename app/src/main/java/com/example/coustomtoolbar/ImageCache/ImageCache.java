package com.example.coustomtoolbar.ImageCache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.coustomtoolbar.Adapter.MyAdapter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.internal.cache.DiskLruCache;

/**
 * Created by SEELE on 2017/7/25.
 */

public class ImageCache {
    private static final String TAG = "ImageCache";
    public static ImageCache mImageCache;
    private  LruCache<String,Bitmap> mLruCache;
    private DiskLruCache diskLruCache = null;
    private int MaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private int cacheSize = MaxMemory / 8;
    private int maxWidth;
    private ImageCache(Context context) {
        mLruCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
        File cacheDir = getDiskLruCacheDir(context,"bitmap");
        if (cacheDir.exists()){
            cacheDir.mkdirs();
        }
    }
    public static ImageCache getInstance(Context context){
        if (mImageCache == null){
            mImageCache = new ImageCache(context);
        }
        return mImageCache;
    }

    public  void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if (getBitmapFromCache(key) == null){
            mLruCache.put(key,bitmap);
        }
    }
    public  Bitmap getBitmapFromCache(String key){

        return mLruCache.get(key);
    }

    public void setMaxWidth(int maxWidth){
        this.maxWidth = maxWidth;
    }


    public void showImage(ImageView imageView, String url) throws ExecutionException, InterruptedException {

        if (getBitmapFromCache(url) != null){
            Log.e(TAG, "addBitmapToMemoryCache: "+ "form cache" );
                if (imageView.getTag() == url){
                    imageView.setImageBitmap(getBitmapFromCache(url));
                }
        }
        else {
            Log.e(TAG, "showImage: " + "form network" );
            new BitmapTask(url,imageView).execute(url);
        }

    }

    public File getDiskLruCacheDir(Context context,String uniqueName){
        String cahceDir;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()){
            cahceDir = context.getExternalCacheDir().getPath();
        }else {
            cahceDir = context.getCacheDir().getPath();
        }
        return new File(cahceDir + File.separator + uniqueName);
    }

    public int getAppVersion(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public  class BitmapTask extends AsyncTask<String, Void, Bitmap> {
        private int reqWidth;
        private String url;
        private ImageView imageView;

        public BitmapTask(String url,ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                bitmap = getBitMapFromNetWork(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageView.getTag() == url){

                Log.e(TAG, "getBitMapFromNetWork: " + url );
                addBitmapToMemoryCache(url,bitmap);
                imageView.setImageBitmap(bitmap);
            }

        }
        public Bitmap getBitMapFromNetWork(String url) throws IOException{
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) imageUrl.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setDoInput(true);
            con.connect();
            InputStream in = con.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
            return bitmap;
        }
    }

    public static Bitmap decodeSampleBitmapFromResource(InputStream in,int reqWidth ){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeStream(in);
        BitmapFactory.decodeStream(in,null,options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(in,null,options);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth){
        final int width = options.outWidth;
        int inSampleSize = 1;
        if ( width > reqWidth){

            final int widthRatio = Math.round((float)width / (float)reqWidth);
            inSampleSize = widthRatio;
        }
        return inSampleSize;
    }


}

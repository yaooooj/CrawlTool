package com.example.coustomtoolbar.ImageCache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by SEELE on 2017/7/25.
 */

public class ImageCache {
    private static final String TAG = "ImageCache";
    public static ImageCache mImageCache;
    private LruCache<String,Bitmap> mLruCache;
    private int MaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private int cacheSize = MaxMemory / 8;
    private int maxWidth;
    private ImageCache() {

        mLruCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }
    public static ImageCache Instance(){
        if (mImageCache == null){
            mImageCache = new ImageCache();
        }
        return mImageCache;
    }

    public void addBitmapToMemoryCache(String key,Bitmap bitmap){
        if (getBitmapFromCache(key) != null){
            mLruCache.put(key,bitmap);
        }
    }
    public Bitmap getBitmapFromCache(String key){
        return mLruCache.get(key);
    }

    public void setMaxWidth(int maxWidth){
        this.maxWidth = maxWidth;
    }

    public Bitmap loadBitmap(String url) throws ExecutionException, InterruptedException {
        Bitmap bitmap = null;
        if (getBitmapFromCache(url) == null){
            //BitmapTask task = new BitmapTask(url,maxWidth);
            //task.execute();
            bitmap = new BitmapTask(url,maxWidth).execute().get();
        }else {
            bitmap = getBitmapFromCache(url);
        }
        return bitmap;
    }

    private class BitmapTask extends AsyncTask<String, Void, Bitmap> {
        private String url;
        private int reqWidth;
        private BitmapTask(String url,int reqWidth) {
            this.url = url;
            this.reqWidth = reqWidth;
        }


        @Override
        protected Bitmap doInBackground(String... strings) {
           // Bitmap bitmap = getBitMapFromNetWork(url);

            return getBitMapFromNetWork(url);
        }

        public Bitmap getBitMapFromNetWork(String url){
            Bitmap bitmap = null;
            try {
                URL imageUrl = new URL(url);
                HttpURLConnection con = (HttpURLConnection) imageUrl.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setDoInput(true);
                con.connect();
                InputStream in = con.getInputStream();
                //bitmap = BitmapFactory.decodeStream(in);
                bitmap = decodeSampleBitmapFromResource(in,reqWidth);
                addBitmapToMemoryCache(url,bitmap);
                Log.e(TAG, "run: "+"get current thread id " + Thread.currentThread().getName() );
                in.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

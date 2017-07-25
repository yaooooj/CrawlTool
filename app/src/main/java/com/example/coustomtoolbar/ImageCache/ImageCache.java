package com.example.coustomtoolbar.ImageCache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.util.LruCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by SEELE on 2017/7/25.
 */

public class ImageCache {
    private static final String TAG = "ImageCache";
    public static ImageCache mImageCache;
    private LruCache<String,Bitmap> mLruCache;
    private int MaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private int cacheSize = MaxMemory / 8;
    private ImageCache() {

        mImageCache = new ImageCache();
        mLruCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static ImageCache Instance(){

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

    public Bitmap loadBitmap(String url){
        Bitmap bitmap = null;
        if (getBitmapFromCache(url) == null){
            BitmapTask task = new BitmapTask(url,50);
            task.execute();

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
            Bitmap bitmap = getBitMapFromNetWork(url);

            return null;
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
        BitmapFactory.decodeStream(in);
        options.inSampleSize = calculateInSampleSize(options,reqWidth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(in);

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

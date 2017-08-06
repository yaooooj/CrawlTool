package com.example.coustomtoolbar.ImageCache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.coustomtoolbar.Adapter.MyAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by SEELE on 2017/7/25.
 */

public class ImageCache {
    private static final String TAG = "ImageCache";
    public static ImageCache mImageCache;
    private  LruCache<String,Bitmap> mLruCache;
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
    public static ImageCache getInstance(){
        if (mImageCache == null){
            mImageCache = new ImageCache();
        }
        return mImageCache;
    }

    public  void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if (getBitmapFromCache(key) == null){
            mLruCache.put(key,bitmap);
        }
    }
    public  Bitmap getBitmapFromCache(String key){
        Log.e(TAG, "addBitmapToMemoryCache: "+ "form cache" );
        return mLruCache.get(key);
    }

    public void setMaxWidth(int maxWidth){
        this.maxWidth = maxWidth;
    }

    public Bitmap loadBitmap(String url) throws ExecutionException, InterruptedException {
        //List<Bitmap> bitmap = new ArrayList<>();
        Bitmap bitmap = null;
        if (getBitmapFromCache(url) == null){
            new BitmapTask().execute(url);
        }else {
            bitmap = getBitmapFromCache(url);
        }
        return bitmap;
    }

    public Bitmap showImage( String url) throws ExecutionException, InterruptedException {

        /*
        if (getBitmapFromCache(url) != null){
                imageView.setImageBitmap(getBitmapFromCache(url));
        }
        else {
            new BitmapTask(imageView).execute(url);
        }

           */
        return new BitmapTask().execute(url).get();
    }

    public void showImageByHolder(){

    }

    public  class BitmapTask extends AsyncTask<String, Void, Bitmap> {
        private int reqWidth;
        private ImageView mImageView;

        public BitmapTask() {
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            Bitmap bitmap = null;
            if (ImageCache.getInstance().getBitmapFromCache(strings[0])!= null){
                bitmap = ImageCache.getInstance().getBitmapFromCache(strings[0]);
                return bitmap;
            }
            try {
                bitmap = getBitMapFromNetWork(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap != null){
                addBitmapToMemoryCache(strings[0],bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.e(TAG, "onPostExecute: " +"form network" );

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
            //bitmap = decodeSampleBitmapFromResource(in,reqWidth);
            Log.e(TAG, "run: "+"get current thread id " + Thread.currentThread().getName() );
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

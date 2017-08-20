package com.example.coustomtoolbar.ImageCache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by SEELE on 2017/8/17.
 */

public class ImageAsyncCall implements Runnable {
    private static final String TAG = "ImageAsyncCall";
    private static ImageCallback mImageCallback;
    private String url;
    private ImageDiskLruCache cache;
    private ImageView mImageView;
    private ImageDispatcher dispatcher = new ImageDispatcher();
    private ImageIntentHandler sHandler;
    public ImageAsyncCall(ImageDiskLruCache cache, String url, ImageCallback imageCallback ){
        mImageCallback = imageCallback;
        this.url = url;
        this.cache = cache;

    }

    public String getUrl(){
        return url;
    }

    @Override
    public void run() {
        HttpURLConnection con = null;
        BufferedInputStream in = null;
        try {
            if (cache.getBitmapFromDiskLruCache(url) != null){
                Bitmap bitmap1 = cache.getBitmapFromDiskLruCache(url);
                if (bitmap1 != null){
                    Log.e(TAG, "run: " + "from disk memory" );
                    Message message = getHandler().obtainMessage(
                            1,new ImageAscynTaskResult<Bitmap>(this,bitmap1));
                    message.sendToTarget();
                }
            }else {
                URL imageUrl = new URL(url);
                con = (HttpURLConnection) imageUrl.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setDoInput(true);
                con.connect();
                in = new BufferedInputStream(con.getInputStream());
                //bitmap = BitmapFactory.decodeStream(in);
                cache.addBitmapToDiskLurCache(in,url);

                Bitmap bitmap2 = cache.getBitmapFromDiskLruCache(url);
                if (bitmap2 != null){
                    //cache.addBitmapToMemoryCache(url,bitmap2);
                    Log.e(TAG, "run: " + "from network" );
                    Message message = getHandler().obtainMessage(
                            1,new ImageAscynTaskResult<Bitmap>(this,bitmap2));
                    message.sendToTarget();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            dispatcher.finish(this);
        }
    }

    private Handler getHandler(){
        synchronized (ImageAsyncCall.class){
            if (sHandler == null){
                sHandler =  new ImageIntentHandler(url);
            }
        }
        return sHandler;
    }
    private static class ImageIntentHandler extends Handler{
        private String url;
        public ImageIntentHandler(String url) {
            super(Looper.getMainLooper());
            this.url = url;
        }

        @Override
        public void handleMessage(Message msg) {
            ImageAscynTaskResult<?> result = (ImageAscynTaskResult<?>) msg.obj;
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    if (msg.obj != null){
                        mImageCallback.OnSuccess((Bitmap) result.data[0],url);
                    }else {
                        mImageCallback.OnFailure(url);
                    }
                    break;
                default:
                    break;
            }

        }
    }
    private  class ImageAscynTaskResult<T>{
        private ImageAsyncCall imageAscynCall;
        private T[] data;

        public ImageAscynTaskResult(ImageAsyncCall imageAscynCall, T... data) {
            this.imageAscynCall = imageAscynCall;
            this.data = data;
        }
    }
}

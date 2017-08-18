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
    private ImageCallback mImageCallback;
    private String url;
    private ImageDiskLruCache cache;
    private ImageDispatcher dispatcher = new ImageDispatcher();
    private Handler handlerBitmap = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null){
                mImageCallback.OnSuccess((Bitmap) msg.obj,url);
            }else {
               mImageCallback.OnFailure(url);
            }
        }
    };
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
        Bitmap bitmap = null;
        HttpURLConnection con = null;
        try {
            if (cache.getBitmapFromDiskLruCache(url) != null){
                bitmap = cache.getBitmapFromDiskLruCache(url);
                if (bitmap != null){
                    cache.addBitmapToMemoryCache(url,bitmap);
                    Message message = handlerBitmap.obtainMessage();
                    message.obj = bitmap;
                    message.sendToTarget();
                }
            }else {
                URL imageUrl = new URL(url);
                con = (HttpURLConnection) imageUrl.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setDoInput(true);
                con.connect();
                BufferedInputStream in  = new BufferedInputStream(con.getInputStream());
                //bitmap = BitmapFactory.decodeStream(in);
                cache.addBitmapToDiskLurCache(in,url);
                bitmap = cache.getBitmapFromDiskLruCache(url);
                if (bitmap != null){
                    cache.addBitmapToMemoryCache(url,bitmap);
                    Message message = handlerBitmap.obtainMessage();
                    message.obj = bitmap;
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
            Log.e(TAG, "run: " +"finish finish");
            dispatcher.finish(this);
        }
    }
}

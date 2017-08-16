package com.example.coustomtoolbar.ImageCache;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by yaojian on 2017/8/15.
 */

public class DownloadBitmapExecutor {
    private static final String TAG = "DownloadBitmapExecutor";
    private static final int coreCount = Runtime.getRuntime().availableProcessors();
    private static final int corePoolSize = coreCount;
    private static final int maxPoolSize = coreCount * 2 + 1;
    private static final long keepAliveTime = 10;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private static final BlockingQueue<Runnable> sWorkQueues = new LinkedBlockingQueue<>(10);
    private static DownloadBitmapExecutor sDownloadBitmapExecutor = null;
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            corePoolSize,maxPoolSize,keepAliveTime,TIME_UNIT,sWorkQueues);

    private LoadBitmap laodBitmap = null;
    private String url = null;
    private FutureTask<Bitmap> future = null;

    private DownloadBitmapExecutor() {
        Log.e(TAG, "DownloadBitmapExecutor: " );
        laodBitmap = new LoadBitmap(url);
        future = new FutureTask<Bitmap>(laodBitmap){
            @Override
            protected void done() {
                super.done();

            }
        };
    }

    public static DownloadBitmapExecutor getInstanceExecutor(){
        if (sDownloadBitmapExecutor == null){
            synchronized (DownloadBitmapExecutor.class){
                if (sDownloadBitmapExecutor == null){
                    sDownloadBitmapExecutor = new DownloadBitmapExecutor();
                }
            }
        }
        return sDownloadBitmapExecutor;
    }


    public void execute(String url){
        laodBitmap = new LoadBitmap(url);


    }
    public void remove(Runnable task){
        if (task != null){
            executor.remove(task);
        }
    }
    private class LoadBitmap implements Callable<Bitmap> {
        String url;

        LoadBitmap(String url) {
            this.url = url;
        }
        @Override
        public Bitmap call() throws Exception {
            Bitmap bitmap = null;
            HttpURLConnection con = null;
            //URL imageUrl = null;
            try {
                URL imageUrl = new URL(url);
                con = (HttpURLConnection) imageUrl.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setDoInput(true);
                con.connect();
                InputStream in = con.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }
}

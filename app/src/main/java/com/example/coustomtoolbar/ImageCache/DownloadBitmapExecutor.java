package com.example.coustomtoolbar.ImageCache;


import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    private static ThreadPoolExecutor executor = null;

    private DownloadBitmapExecutor() {
        Log.e(TAG, "DownloadBitmapExecutor: " );
        executor = new ThreadPoolExecutor(
                corePoolSize,maxPoolSize,keepAliveTime,TIME_UNIT,sWorkQueues);
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
    public void submitTask (Runnable task) throws ExecutionException, InterruptedException {
        if (task != null){
           executor.submit(task);
        }

    }

    public <V> V submitCallable(Callable<V> task) throws ExecutionException, InterruptedException {
       // V v = null;
        Future<V> v =null;
        if (task == null){
            Log.e(TAG, "submitCallable: " + "999999999999999" );
        }else {
            v =  executor.submit(task);
        }

        return v.get();
    }
    public void remove(Runnable task){
        if (task != null){
            executor.remove(task);
        }
    }
}

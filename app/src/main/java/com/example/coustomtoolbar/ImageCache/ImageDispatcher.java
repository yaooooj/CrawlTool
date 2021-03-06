package com.example.coustomtoolbar.ImageCache;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.internal.Util;

/**
 * Created by SEELE on 2017/8/18.
 */

public class ImageDispatcher {

    private static final String TAG = "ImageDispatcher";
    private static final int coreCount = Runtime.getRuntime().availableProcessors();
    private static final int maxRequests = 64;
    private int maxRequestsPerHost = 5;
    private final Deque<ImageAsyncCall> runningAsyncCalls = new ArrayDeque<>();
    private final Deque<ImageAsyncCall> readyAsyncCalls = new ArrayDeque<>();

    private ExecutorService executorService;

    public ImageDispatcher() {
    }

    private synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(coreCount, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), Util.threadFactory("Image Load Dispatcher", false));
        }
        return executorService;
    }

    synchronized void enqueue(ImageAsyncCall call){
        if (runningAsyncCalls.size() < maxRequests && runningCallForHost(call) < maxRequestsPerHost){
            runningAsyncCalls.add(call);
            executorService().execute(call);
        }else {
            readyAsyncCalls.add(call);
        }
    }

    private int runningCallForHost(ImageAsyncCall call){
        int count = 0;
        for (ImageAsyncCall c: runningAsyncCalls){
            if (c.getUrl().equals(call.getUrl())){
                count++;
            }
        }
        return count;
    }

    public void finish(ImageAsyncCall call){
        finished(runningAsyncCalls,call,true);
    }

    private  <T> void finished(Deque<T> deque,T call,Boolean promoteCalls){
        synchronized (this){
            //Log.e(TAG, "finished: " + "finished finished finished " );
            if (!deque.remove(call)) return;
            if (promoteCalls){
                PromoteCalls();
            }

        }
    }

    private void PromoteCalls(){
        if (runningAsyncCalls.size() > maxRequests) return;
        if (readyAsyncCalls.isEmpty()) return;

        for (Iterator<ImageAsyncCall> i = readyAsyncCalls.iterator();i.hasNext();){
            ImageAsyncCall call = i.next();
            if (runningAsyncCalls.size() < maxRequests ){
                i.remove();
                runningAsyncCalls.add(call);
                executorService.execute(call);
            }
            if (runningAsyncCalls.size() >= maxRequests) return;
        }
    }
}

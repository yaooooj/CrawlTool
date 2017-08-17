package com.example.coustomtoolbar.ImageCache;

import android.os.AsyncTask;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import okhttp3.internal.Util;

/**
 * Created by SEELE on 2017/8/18.
 */

public class ImageDispatcher {


    private static final int coreCount = Runtime.getRuntime().availableProcessors();
    private static final int maxRequests = 64;
    private int maxRequestsPerHost = 5;
    private final Deque<ImageAsycCall> runningAsyncCalls = new ArrayDeque<>();
    private final Deque<ImageAsycCall> readyAsyncCalls = new ArrayDeque<>();

    private ExecutorService executorService;

    public ImageDispatcher() {
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp Dispatcher", false));
        }
        return executorService;
    }

    public synchronized void enqueue(ImageAsycCall call){
        if (runningAsyncCalls.size() < maxRequests && runningCallForHost(call) < maxRequestsPerHost){
            runningAsyncCalls.add(call);
            executorService().execute(call);
        }else {
            readyAsyncCalls.add(call);
        }
    }

    public int runningCallForHost(ImageAsycCall call){
        int count = 0;
        for (ImageAsycCall c: runningAsyncCalls){
            if (c.getUrl().equals(call.getUrl())){
                count++;
            }
        }
        return count;
    }
}

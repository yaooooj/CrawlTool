package com.example.coustomtoolbar.NetUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by yaojian on 2017/7/22.
 */

public class OkHttp3Util {
    private static final String TAG = "OkHttp3Util";
    public static final String APIKEY = "42731";
    public static final String APISECRET = "96039fbf84ee42afaad5d66f14159c31";
    public static final String URL = "http://route.showapi.com/852-1?showapi_appid=" + APIKEY + "&showapi_sign=" + APISECRET;
    private OkHttpClient client;
    private GsonUtil gson;
    private String response;
    private Context mContext;
    private Interceptor mInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response1 = chain.proceed(request);
            String cacheControl = request.cacheControl().toString();
            if (TextUtils.isEmpty(cacheControl)){
                cacheControl = "public,max-age=60";
            }

            return response1.newBuilder()
                    .header("Cache-Control",cacheControl)
                    .removeHeader("Pragma")
                    .build();
        }
    };
    public OkHttp3Util(Context context) {
        mContext = context;

        gson = new GsonUtil();
    }

    public void executeGet(String url, final Handler handler, final Class<?> claszz, final int flag) {


        File httpCacheDirectory  = new File(mContext.getCacheDir(),"response");
        Cache cache = new Cache(httpCacheDirectory,10 * 1024 * 1024);

        //CacheControl cacheControl1 = new CacheControl().

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .cache(cache)
                .build();

        Request request1 = new Request.Builder()
                .url(url)
                .cacheControl(CacheControl.FORCE_CACHE)

                .build();

        Request request2 = new Request.Builder()
                .url(url)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();

        try {
            Response forceCacheResponse = okHttpClient.newCall(request1).execute();
            if (forceCacheResponse.code() != 504){
                Message message = handler.obtainMessage();
                Object object;
                Log.e(TAG, "From ForceCacheControl: ");
                try {
                    object = GsonUtil.phraseJsonWithGson(forceCacheResponse.body().string(),claszz);
                    message.obj = object;
                }catch (Exception e){
                    e.printStackTrace();
                }
                message.what = flag;
                handler.sendMessage(message);

            }else {
                okHttpClient.newCall(request2).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, IOException e) {
                        Log.e(TAG, "onFailure: " + "failure execute  request");
                    }
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if (!response.isSuccessful()) throw new IOException("Unexpected code" + response);

                        Message message = handler.obtainMessage();
                        Object object;

                        if (response.code() == 200){
                            Log.e(TAG, "From NetWorkCacheControl: ");
                            try {
                                object = GsonUtil.phraseJsonWithGson(response.body().string(),claszz);
                                message.obj = object;
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        message.what = flag;
                        handler.sendMessage(message);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void executePost(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + "failure execute  request");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) throw new IOException("Unexpected code" + response);
                
            }
        });
    }
}



































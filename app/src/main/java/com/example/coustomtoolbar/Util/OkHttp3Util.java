package com.example.coustomtoolbar.Util;

import android.graphics.Picture;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.coustomtoolbar.Bean.PictureBean;
import com.example.coustomtoolbar.Bean.PictureBody;
import com.example.coustomtoolbar.Bean.PictureContentList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
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

    public OkHttp3Util() {
        client = new OkHttpClient();
        gson = new GsonUtil();
    }

    public void executeGet(String url, final Handler handler, final Class<?> claszz) {
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
                Message message = handler.obtainMessage();
                Object object;
               // PictureBean picture = new PictureBean();
                if (response.code() == 200){
                   Log.e(TAG, "onResponse: ");
                    try {
                        object = GsonUtil.phraseJsonWithGson(response.body().string(),claszz);
                        message.obj = object;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                handler.sendMessage(message);
            }
        });

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



































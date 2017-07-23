package com.example.coustomtoolbar.Util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by yaojian on 2017/7/21.
 */

public class GsonUtil<T> {

    public static <T>  T   phraseJsonWithGson(String jsonData,Class<T> type ){

        Gson gson = new Gson();
        T result = gson.fromJson(jsonData,type);
        return result;
    }

    public static <T> Class<T> phraseJsonWithGsonArray(String jsonData,Class<T> type){

        Gson gson = new Gson();

        Class<T> result = gson.fromJson(jsonData,new TypeToken<List<T>>(){}.getType());

        return result;
    }

}

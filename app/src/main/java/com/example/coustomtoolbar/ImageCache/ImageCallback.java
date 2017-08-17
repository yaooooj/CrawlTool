package com.example.coustomtoolbar.ImageCache;

import android.graphics.Bitmap;

/**
 * Created by SEELE on 2017/8/17.
 */

public interface ImageCallback {

    void OnSuccess(Bitmap bitmap,String url);

    void OnFailure(String url);

    void OnLoading(Bitmap bitmap,String url);
}

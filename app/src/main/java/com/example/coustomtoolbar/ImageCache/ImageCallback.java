package com.example.coustomtoolbar.ImageCache;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by SEELE on 2017/8/17.
 */

public interface ImageCallback {

    void OnSuccess(Bitmap bitmap, String url);

    void OnFailure(String url);

    void OnLoading(String url);
}

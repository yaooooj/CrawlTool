package com.example.coustomtoolbar.ImageCache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by SEELE on 2017/8/17.
 */

public class ImageAsycCall implements Runnable {
    private ImageCallback mImageCallback;
    private String url;
    public ImageAsycCall(ImageCallback imageCallback,String url) {
        mImageCallback = imageCallback;
        this.url = url;
    }

    public String getUrl(){
        return url;
    }
    @Override
    public void run() {
        Bitmap bitmap = null;
        HttpURLConnection con = null;
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
        if (bitmap != null){
            mImageCallback.OnSuccess(bitmap,url);
        }else {
            mImageCallback.OnFailure(url);
        }
    }
}

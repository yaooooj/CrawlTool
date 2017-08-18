package com.example.coustomtoolbar.ImageCache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yaojian on 2017/8/18.
 */

public  class BitmapTask extends AsyncTask<String, Void, Bitmap> {
    private int reqWidth;
    private String url;
    private ImageView imageView;
    private ImageDiskLruCache imageDiskLruCache;

    public BitmapTask(String url,ImageView imageView,ImageDiskLruCache imageDiskLruCache) {
        this.url = url;
        this.imageView = imageView;
        this.imageDiskLruCache = imageDiskLruCache;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap = null;
        if (imageDiskLruCache.getBitmapFromDiskLruCache(url) != null){
            bitmap = imageDiskLruCache.getBitmapFromDiskLruCache(url);
        }else {
            try {
                bitmap = getBitMapFromNetWork(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageView.getTag() == url){
            if (bitmap != null){
                imageDiskLruCache.addBitmapToMemoryCache(url,bitmap);
                imageView.setImageBitmap(bitmap);
                imageView.getMaxWidth();

            }
        }
    }
    private Bitmap getBitMapFromNetWork(String url) throws IOException{
        Bitmap bitmap = null;
        HttpURLConnection con = null;
        FileInputStream fileInputStream;
        FileDescriptor descriptor = null;
        BufferedInputStream inputStream = null;
        String key = imageDiskLruCache.hashKeyForDisk(url);
        try {
            URL imageUrl = new URL(url);
            con = (HttpURLConnection) imageUrl.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setDoInput(true);
            con.connect();
            InputStream in = con.getInputStream();
            inputStream = new BufferedInputStream(in);

            imageDiskLruCache.addBitmapToDiskLurCache(inputStream,url);

            bitmap = imageDiskLruCache.getBitmapFromDiskLruCache(url);
            if (bitmap != null){
                return bitmap;
            }
        }finally {
            if (con != null) {
                con.disconnect();
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}



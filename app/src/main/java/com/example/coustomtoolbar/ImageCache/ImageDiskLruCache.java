package com.example.coustomtoolbar.ImageCache;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yaojian on 2017/8/18.
 */

public class ImageDiskLruCache {

    private static final String TAG = "ImageDiskLruCache";

    private DiskLruCache diskLruCache;
    private Context context;
    private int MaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private int cacheSize = MaxMemory / 6;
    private  LruCache<String,Bitmap> mLruCache;
    private  static ImageDiskLruCache imageDiskLruCache;

     private ImageDiskLruCache(Context context) {

         mLruCache = new LruCache<String, Bitmap>(cacheSize){
             @Override
             protected int sizeOf(String key, Bitmap bitmap) {
                 return bitmap.getByteCount() / 1024;
             }
         };

        try {
            diskLruCache = DiskLruCache.open(
                    getDiskLruCacheDir(context,"Bitmap1"),getAppVersion(context),1,100 * 1024 * 1024 );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static ImageDiskLruCache getImageDiskLruInstance(Context context){
        if (imageDiskLruCache == null){
            synchronized (ImageDiskLruCache.class){
                if (imageDiskLruCache == null){
                    imageDiskLruCache = new ImageDiskLruCache(context);
                }
            }
        }
        return imageDiskLruCache;
    }
    /*
    public static ImageDiskLruCache Create(
            File directory,int appVersion, int valueCount, long maxSize){
        if (maxSize < 0){
            throw new IllegalArgumentException("maxSize < 0" );
        }
        if (valueCount < 0){
            throw new IllegalArgumentException("valueCount < 0");
        }

        if (imageDiskLruCache == null){
            synchronized (ImageDiskLruCache.class){
                if (imageDiskLruCache == null){
                    new ImageDiskLruCache(directory,appVersion,valueCount,maxSize);
                }
            }
        }
        return imageDiskLruCache;
    }
    */


    public String hashKeyForDisk(String key){
        String cacheKey = null;

        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(key.getBytes());
            cacheKey = bytesToHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }
    synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if (getBitmapFromCache(key) == null){
            //Log.e(TAG, "addBitmapToMemoryCache: " + "add to memory cache" );
            mLruCache.put(key,bitmap);
        }
    }
    public Bitmap getBitmapFromCache(String key){

        return mLruCache.get(key);
    }
    synchronized void addBitmapToDiskLurCache(BufferedInputStream in, String url) throws IOException {

        OutputStream outputStream= null;
        //BufferedOutputStream bufferedOutputStream;
        try {
            String key = hashKeyForDisk(url);
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            if (editor != null){
                outputStream = editor.newOutputStream(0);
                if (cacheToDisk(in,outputStream)){
                    editor.commit();
                } else {
                    editor.abort();
                }
            }
            diskLruCache.flush();
        } catch (IOException e) {
            if (outputStream != null){
                outputStream.close();
            }
            e.printStackTrace();
        }
    }
    synchronized Bitmap getBitmapFromDiskLruCache(String url){
        String key = hashKeyForDisk(url);

        DiskLruCache.Snapshot snapshot = null;
        FileInputStream fileInput = null;
        BufferedInputStream in = null;
        FileDescriptor descriptor = null;
        Bitmap bitmap = null;
        try {
            snapshot = diskLruCache.get(key);
            if (snapshot != null){
                fileInput = (FileInputStream) snapshot.getInputStream(0);
                descriptor = fileInput.getFD();
                //in = new BufferedInputStream(snapshot.getInputStream(0),8 * 1024);
                //bitmap = BitmapFactory.decodeStream(in);
            }
            if (descriptor != null){
                //bitmap = decodeSampleBitmapFromResource(descriptor,width,height);
                bitmap = BitmapFactory.decodeFileDescriptor(descriptor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileInput != null){
                try {
                    fileInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (snapshot != null){
                snapshot.close();
            }

        }
        return bitmap;
    }

    private boolean cacheToDisk(BufferedInputStream in,OutputStream outputStream){
        BufferedOutputStream bufferedOutputStream = null;
        int b;
        try {
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            while ((b = in.read()) != -1){
                bufferedOutputStream.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bufferedOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    private File getDiskLruCacheDir(Context context, String uniqueName){
        String cacheDir;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()){
            cacheDir = context.getExternalCacheDir().getPath();
        }else {
            cacheDir = context.getCacheDir().getPath();
        }
        return new File(cacheDir + File.separator + uniqueName);
    }

    private int getAppVersion(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}

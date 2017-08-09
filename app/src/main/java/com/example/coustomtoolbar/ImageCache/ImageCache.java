package com.example.coustomtoolbar.ImageCache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import okhttp3.internal.cache.DiskLruCache;

/**
 * Created by SEELE on 2017/7/25.
 */

public class ImageCache {
    private static final String TAG = "ImageCache";
    public static ImageCache mImageCache;
    private  LruCache<String,Bitmap> mLruCache;
    private com.jakewharton.disklrucache.DiskLruCache diskLruCache = null;
    private int MaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private int cacheSize = MaxMemory / 8;

    private int maxWidth;

    private ImageCache(Context context) {
        mLruCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
        File cacheDir = getDiskLruCacheDir(context,"bitmap");
        if (cacheDir.exists()){
            cacheDir.mkdirs();
        }
        try {
            diskLruCache = com.jakewharton.disklrucache.DiskLruCache
                    .open(cacheDir,getAppVersion(context),1,10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static ImageCache getInstance(Context context){
        if (mImageCache == null){
            mImageCache = new ImageCache(context);
        }
        return mImageCache;
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if (getBitmapFromCache(key) == null){
            mLruCache.put(key,bitmap);
        }
    }
    private Bitmap getBitmapFromCache(String key){

        return mLruCache.get(key);
    }

    public void setMaxWidth(int maxWidth){
        this.maxWidth = maxWidth;
    }


    public void showImage(ImageView imageView, String url) throws ExecutionException, InterruptedException {

        if (getBitmapFromCache(url) != null){
            Log.e(TAG, "addBitmapToMemoryCache: "+ "form cache" );
                if (imageView.getTag() == url){
                    imageView.setImageBitmap(getBitmapFromCache(url));
                }
        }
        else {
            Log.e(TAG, "showImage: " + "form network" );
            new BitmapTask(url,imageView).execute(url);
        }

    }

    private File getDiskLruCacheDir(Context context,String uniqueName){
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

    private void addBitmapToDiskLurCache(InputStream in,String url) throws IOException {
        String key = hashKeyForDisk(url);
        OutputStream outputStream = null;
        try {
            com.jakewharton.disklrucache.DiskLruCache.Editor editor = diskLruCache.edit(key);
            if (editor != null){
                outputStream = editor.newOutputStream(0);
                int b;
                Log.e(TAG, "addBitmapToDiskLurCache: " + "11111111111111111111" );
                while ((b = in.read()) != -1){
                    outputStream.write(b);
                }
            }
        } catch (IOException e) {
            if (outputStream != null){
                outputStream.close();
            }
            e.printStackTrace();
        }
    }
    private  class BitmapTask extends AsyncTask<String, Void, Bitmap> {
        private int reqWidth;
        private String url;
        private ImageView imageView;

        public BitmapTask(String url,ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                bitmap = getBitMapFromNetWork(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageView.getTag() == url){

                Log.e(TAG, "getBitMapFromNetWork: " + url );
                addBitmapToMemoryCache(url,bitmap);
                imageView.setImageBitmap(bitmap);
            }

        }
        private Bitmap getBitMapFromNetWork(String url) throws IOException{
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
                addBitmapToDiskLurCache(in,url);
                in.close();
            }finally {
                if (con != null){
                    con.disconnect();
                }
            }

            return bitmap;
        }
    }

    private  static Bitmap decodeSampleBitmapFromResource(InputStream in,int reqWidth ){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeStream(in);
        BitmapFactory.decodeStream(in,null,options);
        options.inSampleSize = calculateInSampleSize(options,reqWidth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(in,null,options);
    }
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth){
        final int width = options.outWidth;
        int inSampleSize = 1;
        if ( width > reqWidth){

            final int widthRatio = Math.round((float)width / (float)reqWidth);
            inSampleSize = widthRatio;
        }
        return inSampleSize;
    }


}

package com.example.coustomtoolbar.ImageCache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.example.coustomtoolbar.Fragment.Fragment2;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

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
    private BitmapFactory.Options options1 = new BitmapFactory.Options();
    private int maxWidth;
    private int width;
    private int height;
    private Context mContext;
    private static DownloadBitmapExecutor executor1;

    private ImageCache(Context context) {
        mContext = context;
        getScreenWidth(context);
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
                    .open(cacheDir,getAppVersion(context),1,100 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (executor1 == null){
            Log.e(TAG, "ImageCache: " +  "getInstanceExecutor");
            executor1 = DownloadBitmapExecutor.getInstanceExecutor();
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
        Log.e(TAG, "getBitmapFromCache: " + "form memory cache " );
        return mLruCache.get(key);
    }
    public int  getScreenWidth(Context context){
        DisplayMetrics displayMetrics  = context.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = 4 * width;
        Log.e(TAG, "getScreenWidth: "+ width );
        return width;
    }
    public void setMaxWidth(int maxWidth){
        this.maxWidth = maxWidth;
    }


    public void showImage(ImageView imageView, String url) throws ExecutionException, InterruptedException {
        if (getBitmapFromCache(url) != null){
            if (imageView.getTag() == url){
                imageView.setImageBitmap(getBitmapFromCache(url));
            }
        }
        else {
            //
            getBitmapFromNetWork(url,imageView);
        }
    }

    private void getBitmapFromNetWork(final String url, final ImageView imageView){
        /*Bitmap bitmap = null;
        //bitmap = executor.submit(new DownBitmap1(url,imageView));
        try {

            bitmap = executor1.submitCallable(new DownBitmap1(url,imageView));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (bitmap != null){
            Log.e(TAG, "getBitmapFromNetWork: ");
        }*/
        //executor.submitTask(new DownBitmap1(url,imageView));
        Bitmap bitmap = null;
        try {
            bitmap = executor1.submitCallable(new DownBitmap1(url,imageView));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final Bitmap finalBitmap = bitmap;
        new Thread(new Runnable() {

            @Override
            public void run() {

                if (finalBitmap != null && imageView.getTag() == url){
                    imageView.setImageBitmap(finalBitmap);
                }
            }
        }).start();

    }

    private Bitmap getBitmapFromDiskLruCache(String url){
        DiskLruCache.Snapshot snapshot = null;
        FileInputStream fileInput;
        BufferedInputStream in = null;
        FileDescriptor descriptor = null;
        Bitmap bitmap = null;
        String key = hashKeyForDisk(url);
        try {
            snapshot = diskLruCache.get(key);
            if (snapshot != null){
                fileInput = (FileInputStream) snapshot.getInputStream(0);
                descriptor = fileInput.getFD();
                //in = new BufferedInputStream(snapshot.getInputStream(0),8 * 1024);
                //bitmap = BitmapFactory.decodeStream(in);
            }

            if (descriptor != null){
                Log.e(TAG, "getBitmapFromDiskLruCache: "+ "Load from disk " );
                bitmap = decodeSampleBitmapFromResource(descriptor,width,height);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
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

    private void addBitmapToDiskLurCache(BufferedInputStream in,String url) throws IOException {

        OutputStream outputStream= null;
        //BufferedOutputStream bufferedOutputStream;
        try {
            String key = hashKeyForDisk(url);
            com.jakewharton.disklrucache.DiskLruCache.Editor editor = diskLruCache.edit(key);
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

            if (getBitmapFromDiskLruCache(url) != null){

                bitmap = getBitmapFromDiskLruCache(url);

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

                    Log.e(TAG, "getBitMapFromNetWork: " + url );
                    addBitmapToMemoryCache(url,bitmap);
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
            String key = hashKeyForDisk(url);
            try {
                URL imageUrl = new URL(url);
                con = (HttpURLConnection) imageUrl.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setDoInput(true);
                con.connect();
                InputStream in = con.getInputStream();
                inputStream = new BufferedInputStream(in);


                addBitmapToDiskLurCache(inputStream,url);

                DiskLruCache.Snapshot snapshot = diskLruCache.get(key);

                if (snapshot != null){
                    fileInputStream = (FileInputStream) snapshot.getInputStream(0);
                    //InputStream inBitmap = snapshot.getInputStream(0);
                    descriptor = fileInputStream.getFD();
                }

                if (descriptor != null){
                    //bitmap = BitmapFactory.decodeFileDescriptor(descriptor);
                    return BitmapFactory.decodeFileDescriptor(descriptor,null,options1);
                    //return bitmap;
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

     private  Bitmap decodeSampleBitmapFromResource(FileDescriptor in, int reqWidth,int height){
        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        //BitmapFactory.decodeStream(in,null,options);
        BitmapFactory.decodeFileDescriptor(in,null,options);

        options.inSampleSize = calculateInSampleSize(options,reqWidth,height);

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFileDescriptor(in,null,options);
    }
    private  int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth,int reqHeight){

        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;
        Log.e(TAG, "calculateInSampleSize: " );
        if ( width > reqWidth){

            final int widthRatio = Math.round((float)width / (float)reqWidth);
            inSampleSize = widthRatio;
        }
        if (height > reqHeight){
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            inSampleSize = heightRatio;
        }
        return inSampleSize;
    }


    private class DownBitmap1 implements Callable<Bitmap> {
        String url;
        ImageView mImageView;
        public DownBitmap1(String url,ImageView imageView) {
            this.url = url;
            mImageView = imageView;
        }
        @Override
        public Bitmap call() throws Exception {
            Bitmap bitmap = null;
            HttpURLConnection con = null;
            //URL imageUrl = null;
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
            return bitmap;
        }
    }
}

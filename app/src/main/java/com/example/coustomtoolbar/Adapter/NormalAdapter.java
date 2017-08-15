package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coustomtoolbar.ImageCache.ImageCache;
import com.example.coustomtoolbar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by yaojian on 2017/8/1.
 */

public class NormalAdapter extends BaseAdapter<String,BaseViewHolder>{
    private static final String TAG = "NormalAdapter";
    private int width;
    private int height;
    private ImageCache mImageCache;
    private ShowImage showImage;
    private boolean firstLoadImage = true;

    public NormalAdapter(Context context, int layoutResId, List<String> data, RecyclerView recyclerView) {
        super(context, layoutResId, data, recyclerView);
        getScreenWidth(context);
        mImageCache = ImageCache.getInstance(context);
    }


    public int  getScreenWidth(Context context){
        DisplayMetrics displayMetrics  = context.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        Log.e(TAG, "getScreenWidth: "+ width );
        return width;
    }

    @Override
    public void bindingItemView(final Context context, final BaseViewHolder holder, String s) {
        if (holder.getItemViewType() == ViewType.TYPE_EMPTY){
            holder.getView(R.id.image_empty).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"hhhhhhhhhhhh",Toast.LENGTH_SHORT).show();
                }
            });
        }else {

            ImageView imageView = holder.getView(R.id.fragment2_image);


            holder.setImageView(imageView,R.mipmap.ic_favorite_black_24dp);
            imageView.setTag(s);

            if (isFirstLoadImage()){
                Log.e(TAG, "bindingItemView: " + "FirstLoadImage" );
                try {
                    mImageCache.showImage(imageView,s);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }else {
                Log.e(TAG, "bindingItemView: " + "scroll" );
                showImage.setShowImage(imageView,s);
            }

        }
    }

    public boolean isFirstLoadImage() {
        return firstLoadImage;
    }

    public void setFirstLoadImage(boolean firstLoadImage) {
        this.firstLoadImage = firstLoadImage;
    }

    public void setShowImage(ShowImage showImage){
        this.showImage = showImage;
    }
    public interface ShowImage{
        void setShowImage(ImageView image,String url);
    }
}

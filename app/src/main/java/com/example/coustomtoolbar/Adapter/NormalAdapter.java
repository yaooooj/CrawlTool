package com.example.coustomtoolbar.Adapter;

import android.app.Activity;
import android.content.Context;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coustomtoolbar.ImageCache.GlideApp;
import com.example.coustomtoolbar.ImageCache.ImageCache;
import com.example.coustomtoolbar.R;

import java.util.List;


/**
 * Created by yaojian on 2017/8/1.
 */

public class NormalAdapter extends BaseAdapter<String,BaseViewHolder>{
    private static final String TAG = "NormalAdapter";
    private int width;
    private int height;
    private Fragment fragment;
    private Context mContext;
    private boolean firstLoadImage = true;
    private RecyclerView recyclerView = null;

    public NormalAdapter(Context context, int layoutResId, List<String> data, RecyclerView recyclerView) {
        super(context, layoutResId, data, recyclerView);
        this.mContext = context;
        this.recyclerView = recyclerView;
        getScreenWidth1(context);
    }

    /*
    public NormalAdapter(Fragment fragment, int layoutResId, List<String> data, RecyclerView recyclerView) {
        super(fragment, layoutResId, data, recyclerView);


    }
       */
    @Override
    public void bingingItemView(BaseViewHolder holder, String s) {
        if (holder.getItemViewType() == ViewType.TYPE_EMPTY){
            holder.getView(R.id.image_empty).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"hhhhhhhhhhhh",Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            ImageView imageView = holder.getView(R.id.fragment2_image);
            //holder.setImageView(imageView,R.mipmap.ic_favorite_black_24dp);
            loadImage(mContext,imageView,s);

        }
    }


    private int  getScreenWidth1(Context context){
        DisplayMetrics displayMetrics  = context.getResources().getDisplayMetrics();
        int count = 1;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager){
            count = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        width = displayMetrics.widthPixels / count;
        height = displayMetrics.heightPixels;
        Log.e(TAG, "getScreenWidth: "+ width );
        return width;
    }



    public boolean isFirstLoadImage() {
        return firstLoadImage;
    }

    public void setFirstLoadImage(boolean firstLoadImage) {
        this.firstLoadImage = firstLoadImage;
    }


    private void loadImage(Context context,ImageView imageView,String s) {
        GlideApp.with(context)
                .load(s)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .dontAnimate()
                .into(imageView);
    }
}

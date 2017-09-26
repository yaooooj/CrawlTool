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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coustomtoolbar.ImageCache.GlideApp;
import com.example.coustomtoolbar.ImageCache.ImageCache;
import com.example.coustomtoolbar.R;

import java.util.List;


/**
 * Created by yaojian on 2017/8/1.
 */

public class ImageAdapter extends BaseAdapter<String,BaseViewHolder>{
    private static final String TAG = "ImageAdapter";
    private int width;
    private int height;
    private Fragment fragment;
    private Context mContext;
    private boolean firstLoadImage = true;
    private RecyclerView recyclerView = null;
    private LoadMoreListener loadMoreListener;

    public ImageAdapter(Fragment fragment, int layoutResId, List<String> data, RecyclerView recyclerView) {
        super(fragment, layoutResId, data, recyclerView);
        this.fragment = fragment;
    }

    /*
    public ImageAdapter(Context context, int layoutResId, List<String> data, RecyclerView recyclerView) {
        super(context, layoutResId, data, recyclerView);
        this.mContext = context;
        this.recyclerView = recyclerView;
        getScreenWidth1(context);
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
            //Log.e(TAG, "bingingItemView: " +  s);
            if (isFirstLoadImage()){
                loadImage(imageView,s);
            }else {
                loadMoreListener.loadMore(imageView,s);
            }


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



    private boolean isFirstLoadImage() {
        return firstLoadImage;
    }

    public void setFirstLoadImage(boolean firstLoadImage) {
        this.firstLoadImage = firstLoadImage;
    }


    public void setLoadMoreListener(LoadMoreListener loadMoreListener){
        this.loadMoreListener = loadMoreListener;
    }


    public interface LoadMoreListener{
        void loadMore(ImageView imageView,String s);
    }

    public void loadImage(ImageView imageView,String s) {
        GlideApp.with(fragment)
                .load(s)
                .placeholder(R.mipmap.ic_favorite_border_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .dontAnimate()
                .into(imageView);
    }
}

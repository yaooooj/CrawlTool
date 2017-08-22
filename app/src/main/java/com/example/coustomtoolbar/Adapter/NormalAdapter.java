package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private RecyclerView recyclerView = null;

    public NormalAdapter(Context context, int layoutResId, List<String> data, RecyclerView recyclerView) {
        super(context, layoutResId, data, recyclerView);
        this.recyclerView = recyclerView;
        getScreenWidth1(context);
        mImageCache = ImageCache.getInstance(context);

    }


    public int  getScreenWidth1(Context context){
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

            if (isFirstLoadImage()){
                Glide.with(context)

                        .load(s)
                        .into(imageView);

            }else {
                Glide.with(context).load(s).into(imageView);

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

package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coustomtoolbar.ImageCache.ImageCache;
import com.example.coustomtoolbar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by yaojian on 2017/6/29.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final String TAG = "MyAdapter";
    private Context context;
    private int itemCount = 0;
    private List<String> datas;
    private List<Integer> heights;
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOTER = 2;
    private int mHeaderCount = 1;
    private int mFooterCount = 1;
    private int width;
    private ImageCache imageCache;


    public MyAdapter(Context context, List<String> data) {
        this.context = context;
        datas = data;
        heights = new ArrayList<>();
        getScreenWidth(context);
        imageCache = ImageCache.getInstance();

    }
    public int  getScreenWidth(Context context){


        DisplayMetrics displayMetrics  = context.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;

        Log.e(TAG, "getScreenWidth: "+ width );
        return width;
    }
    public void addItem(String url){
        if (datas == null ){
            datas = new ArrayList<>();
        }
        datas.add(url);
        //notifyItemInserted(datas.size()-1);
        notifyDataSetChanged();
        itemCount++;
    }
    public void removeItem(int position){
        if (datas == null || datas.isEmpty()){
            return;
        }
        notifyItemRemoved(position);
        itemCount--;

    }

    public int getContentItemCount(){
        return datas.size();
    }


    public boolean isFooterView(int position){
        return mFooterCount != 0 && position < mFooterCount;
    }

    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();

        if (mFooterCount != 0 && position >=  dataItemCount){
            return ITEM_TYPE_FOOTER;
        }
        else {
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_TYPE_FOOTER){
            return new MyAdapter.MyFooterVIewHolder(
                    inflater.inflate(R.layout.footer_add_more,parent,false)
            );
        }else if (viewType == ITEM_TYPE_CONTENT){
            return new MyAdapter.MyViewHolder(
                    inflater.inflate(R.layout.fragment_item,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int screenSize = width / 3;

        if (holder instanceof MyAdapter.MyFooterVIewHolder){

        } else if (holder instanceof MyAdapter.MyViewHolder){
           // ((MyViewHolder) holder).textView.setText("madna");

            if (heights.size() <= datas.size() ){
                heights.add((int) (100+ Math.random()*300));
            }

            ViewGroup.LayoutParams params = ((MyViewHolder) holder).imageView.getLayoutParams();
            params.width =  screenSize;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //params.height = heights.get(position);
            ((MyViewHolder) holder).imageView.setMaxWidth(screenSize);
            ((MyViewHolder) holder).imageView.setMaxHeight((int)(screenSize * 5));
            ((MyViewHolder) holder).imageView.setLayoutParams(params);

            imageCache.showImage(((MyViewHolder) holder).imageView,datas.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams){
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams)lp;
            if (isLoadMore(holder.getLayoutPosition())){
                p.setFullSpan(true);
            }

        }
    }
    public boolean isLoadMore(int position){
        return position >= getContentItemCount();
    }

    private class MyFooterVIewHolder extends RecyclerView.ViewHolder{
       TextView mTextView;

         MyFooterVIewHolder(View itemView) {
            super(itemView);
             mTextView = (TextView)itemView.findViewById(R.id.add_more_data);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.image_recycler);


        }
    }
}
package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coustomtoolbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/6/29.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private int itemCount = 0;
    private List<Bitmap> datas;
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOTER = 2;
    private int mHeaderCount = 1;
    private int mFooterCount = 1;


    public MyAdapter(Context context, List<Bitmap> data) {
        this.context = context;
        datas = data;
    }
    public void addItem(Bitmap bitmap){
        if (datas == null ){
            datas = new ArrayList<>();
        }
        datas.add(bitmap);
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

        if (mFooterCount != 0 && position >= (mHeaderCount + dataItemCount)){
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
                    inflater.inflate(R.layout.layout_recycler_first_item,parent,false)
            );
        }else if (viewType == ITEM_TYPE_CONTENT){
            return new MyAdapter.MyViewHolder(
                    inflater.inflate(R.layout.fragment_item,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyAdapter.MyFooterVIewHolder){

        } else if (holder instanceof MyAdapter.MyViewHolder){
           // ((MyViewHolder) holder).textView.setText("madna");
            ((MyViewHolder) holder).imageView.setImageBitmap(datas.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


   private class MyFooterVIewHolder extends RecyclerView.ViewHolder{

         MyFooterVIewHolder(View itemView) {
            super(itemView);
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        MyViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.image_recycler);
            textView = (TextView)itemView.findViewById(R.id.textView_recycler);

        }
    }
}
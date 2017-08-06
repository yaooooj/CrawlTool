package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coustomtoolbar.R;

import java.util.List;

/**
 * Created by yaojian on 2017/7/25.
 */

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.Fargment2ViewHodler> {
    public static final String TAG = "MyAdapter2";
    private List<String> mData;
    private Context mContext;
    public MyAdapter2(Context context, List<String> mData) {
        this.mData = mData;
        this.mContext = context;
    }

    public void updata(int count){
        for (int i =0; i < count;i++){
            mData.add("it's  new beautiful day");
        }
        notifyDataSetChanged();
    }

    @Override
    public Fargment2ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {

        return new Fargment2ViewHodler(
                LayoutInflater.from(mContext).inflate(R.layout.fragment2_item_,parent,false));
    }

    @Override
    public void onBindViewHolder(Fargment2ViewHodler holder, int position) {
        holder.mImageView.setImageResource(R.mipmap.ic_favorite_black_24dp);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class Fargment2ViewHodler extends RecyclerView.ViewHolder{
        ImageView mImageView;
        public Fargment2ViewHodler(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.fragment2_image);
        }
    }


}

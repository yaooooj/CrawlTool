package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;

import com.example.coustomtoolbar.R;

import java.util.List;

/**
 * Created by SEELE on 2017/8/3.
 */

public class MainAdapter extends BaseAdapter<Integer,BaseViewHolder> {


    public MainAdapter(Context context, int layoutResId, List<Integer> data, RecyclerView recyclerView) {
        super(context, layoutResId, data, recyclerView);
    }

    @Override
    public void bindingItemView(Context context, BaseViewHolder holder, Integer integer) {

        holder.setImageViewResource(R.id.image_main,integer);
        //holder.setTextView(R.id.button_card_view,holder.getAdapterPosition()+ "");


    }
}

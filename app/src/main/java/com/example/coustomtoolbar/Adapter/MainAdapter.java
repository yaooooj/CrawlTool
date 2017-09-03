package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.example.coustomtoolbar.R;

import java.util.List;

/**
 * Created by SEELE on 2017/8/3.
 */

public class MainAdapter extends BaseAdapter<String,BaseViewHolder> {


    public MainAdapter(Context context, int layoutResId, List<String> data, RecyclerView recyclerView) {
        super(context, layoutResId, data, recyclerView);
    }

    @Override
    public void bingingItemView(BaseViewHolder holder, String s) {
        holder.setTextView(R.id.image_text,s);
    }


}

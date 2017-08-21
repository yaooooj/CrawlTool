package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.coustomtoolbar.R;

import java.util.List;

/**
 * Created by yaojian on 2017/8/21.
 */

public class CollapsingAdapter extends BaseAdapter<String,BaseViewHolder> {
    private List<String> list;
    public CollapsingAdapter(Context context, int layoutResId, List<String> data, RecyclerView recyclerView) {
        super(context, layoutResId, data, recyclerView);
        list = data;
    }

    @Override
    public void bindingItemView(Context context, BaseViewHolder holder, String s) {
            holder.setTextView(R.id.like_text_view,s);
    }
}

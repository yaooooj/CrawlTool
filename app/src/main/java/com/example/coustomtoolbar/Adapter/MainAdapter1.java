package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.example.coustomtoolbar.R;

import java.util.List;

/**
 * Created by SEELE on 2017/9/10.
 */

public class MainAdapter1 extends BaseAdapter<String,BaseViewHolder> {


    public MainAdapter1(Context context, int layoutResId, List<String> data, RecyclerView recyclerView) {
        super(context, layoutResId, data, recyclerView);
    }

    @Override
    public void bingingItemView(BaseViewHolder holder, String s) {
        if (holder.getItemViewType() == ViewType.TYPE_EMPTY) {

        }else {
            holder.setTextView(R.id.image_text,s);
        }

    }
}

package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.coustomtoolbar.R;

import java.util.List;

/**
 * Created by yaojian on 2017/8/1.
 */

public class NormalAdapter extends BaseAdapter<String,BaseViewHolder>{


    public NormalAdapter(Context context, int layoutResId, List<String> data) {
        super(context, layoutResId, data);
    }

    @Override
    public void bindingViewHolder(Context context, BaseViewHolder holder, String s) {
       //
        if (holder.getTag().equals("header")){
            //TextView textView = holder.getView(R.id.add_more_data);
            holder.setTextView(R.id.add_more_data,"more");

        }else {
            holder.setTextView(R.id.fragment2,"hahha");
        }
    }
}

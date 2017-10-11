package com.example.coustomtoolbar.Adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.example.coustomtoolbar.R;

import java.util.List;

/**
 * Created by SEELE on 2017/10/9.
 */

public class SetWrapperAdapter extends BaseAdapter<String,BaseViewHolder> {


    public SetWrapperAdapter(Fragment fragment, int layoutResId, List<String> data, RecyclerView recyclerView) {
        super(fragment, layoutResId, data, recyclerView);
    }

    @Override
    public void bingingItemView(BaseViewHolder holder, String s) {
        holder.itemView.setBackgroundResource(R.drawable.item_selector);
        if (holder.getItemViewType() == ViewType.TYPE_ITEM){
            holder.setTextView(R.id.set_wrapper_item_textview,s);
        }
    }


}

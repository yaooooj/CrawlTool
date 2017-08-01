package com.example.coustomtoolbar.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yaojian on 2017/8/1.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {
    SparseArray<View> mViews;
    View mItemView;
    public BaseViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mViews = new SparseArray<>();
    }

    public <T extends View> T getView(int viewResId){
        View view = mViews.get(viewResId);
        if (view == null){
            view = mItemView.findViewById(viewResId);
            mViews.put(viewResId,view);
        }
        return (T)view;
    }
    public BaseViewHolder setTextView(int resId,CharSequence text){
        TextView textView = getView(resId);
        textView.setText(text);
        return this;
    }
}

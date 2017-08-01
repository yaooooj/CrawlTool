package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/8/1.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private List<T> mData;
    private int layoutResId;
    private Context mContext;
    private List<Integer> headerViewList;
    private List<Integer> footerViewList;
    private boolean isFooter = false;
    private boolean isHeader = false;
    private static final int TYPE_HEADER = 100;
    private static final int TYPE_ITEM = 101;
    private static final int TYPE_FOOTER = 102;

    public BaseAdapter(Context context,int layoutResId, List<T> data) {
        mContext = context;
        mData = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0 ){
            this.layoutResId = layoutResId;
        }
        headerViewList = new ArrayList<>();
        footerViewList = new ArrayList<>();
    }

    public BaseAdapter(Context context,List<T> data) {
        this(context,0,data);
    }

    public BaseAdapter(Context context,int layoutResId) {
        this(context,layoutResId,null);
    }

    public void addData(T data){
        mData.add(data);
        notifyDataSetChanged();
    }

    public void remove(int position){
        mData.remove(position);
        int internalPosition = mData.size() + getHeaderViewCount();
        notifyItemRemoved(internalPosition);
        notifyItemRangeChanged(internalPosition,mData.size() - internalPosition);
    }

    public void setHeaderViewList(int view){
        if (headerViewList == null){
            headerViewList = new ArrayList<>();
        }else {
            if (view != 0){
                headerViewList.add(view);
            }
        }
    }

    public void setFooterViewList(int view){
        if (footerViewList == null){
            footerViewList = new ArrayList<>();
        }else {
            if (view != 0){
                footerViewList.add(view);
            }
        }
    }

    public int getHeaderViewCount(){
        return headerViewList.size();
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER){
            if (headerViewList.size() != 0){
                View view = LayoutInflater.from(mContext).inflate(headerViewList.get(0),parent,false);
                return new BaseViewHolder(view);
            }

        }
        else if (viewType == TYPE_FOOTER){
            if (footerViewList.size() != 0){
                View view = LayoutInflater.from(mContext).inflate(footerViewList.get(0),parent,false);
                return new BaseViewHolder(view);
            }
        }
        View view = LayoutInflater.from(mContext).inflate(layoutResId,parent,false);
        return new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        bindingViewHolder(mContext,holder,mData.get(position));
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (headerViewList != null){
            count += headerViewList.size();
        }
        if (footerViewList != null){
            count += footerViewList.size();
        }
        return mData.size() + count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < headerViewList.size()){
            return TYPE_HEADER;
        }else if (position > footerViewList.size() + mData.size()){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }

    public abstract void bindingViewHolder(Context context,BaseViewHolder holder,T t );

    private class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
    private class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}

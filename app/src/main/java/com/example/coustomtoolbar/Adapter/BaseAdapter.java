package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/8/1.
 */

public abstract class BaseAdapter<T,K extends BaseViewHolder> extends RecyclerView.Adapter<K> {
    private List<T> mData;
    private int layoutResId;
    private Context mContext;
    private LinearLayout headerViewList;
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
        //headerViewList = new ArrayList<>();
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
        //int internalPosition = mData.size() + getHeaderViewCount();
        int internalPosition = mData.size();
        notifyItemRemoved(internalPosition);
        notifyItemRangeChanged(internalPosition,mData.size() - internalPosition);
    }

    public void setHeaderViewList(LinearLayout view){

        /*
        if (headerViewList == null){
            headerViewList = new ArrayList<>();
        }else {

        }
        */
        if (view != null){
            headerViewList = view;
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
    /*
    public int getHeaderViewCount(){
        return headerViewList.size();
    }
        */

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {

        K baseViewHolder = null;
        switch (viewType){
            case TYPE_HEADER:
                baseViewHolder = createBaseViewHolder(headerViewList);
                break;
            default:
                baseViewHolder = onCreateDefViewHolder(parent, viewType);
        }
        ///View view = LayoutInflater.from(mContext).inflate(layoutResId,parent,false);
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        bindingViewHolder(mContext,holder,mData.get(position));


    }

    @Override
    public int getItemCount() {

        return mData.size() + 1 ;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 1){
            return TYPE_HEADER;
        }else if (position > footerViewList.size() + mData.size()){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }
    protected K onCreateDefViewHolder(ViewGroup parent, int viewType) {
        int layoutId = layoutResId;

        return createBaseViewHolder(parent, layoutId);
    }

    protected K createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return createBaseViewHolder(LayoutInflater.from(mContext).inflate(layoutResId,parent,false));
    }

    /**
     * if you want to use subclass of BaseViewHolder in the adapter,
     * you must override the method to create new ViewHolder.
     *
     * @param view view
     * @return new ViewHolder
     */
    @SuppressWarnings("unchecked")
    protected K createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        K k = createGenericKInstance(z, view);
        return null != k ? k : (K) new BaseViewHolder(view);
    }

    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                }
            }
        }
        return null;
    }

    /**
     * try to create Generic K instance
     *
     * @param z
     * @param view
     * @return
     */
    @SuppressWarnings("unchecked")
    private K createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (K) constructor.newInstance(view);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
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

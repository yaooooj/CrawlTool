package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
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

public abstract class BaseAdapter<T,K extends BaseViewHolder> extends RecyclerView.Adapter<K>
        implements View.OnClickListener,View.OnLongClickListener{
    private static final String TAG = "BaseAdapter";
    private List<T> mData;
    private int layoutResId;
    private Context mContext;
    private SparseIntArray headerViews;
    private SparseIntArray footerViews;
    private static final int TYPE_HEADER = 10000;
    private static final int TYPE_ITEM = 101;
    private static final int TYPE_FOOTER = 20000;

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BaseAdapter(Context context,int layoutResId, List<T> data) {
        mContext = context;
        mData = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0 ){
            this.layoutResId = layoutResId;
        }
        headerViews = new SparseIntArray();
        footerViews = new SparseIntArray();

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
    public void addData(List<T> datas){
        for (T data:datas){
            mData.add(data);
        }
        notifyDataSetChanged();
    }

    public void remove(int position){
        mData.remove(position);
        //int internalPosition = mData.size() + getHeaderViewCount();
        int internalPosition = mData.size();
        notifyItemRemoved(internalPosition);
        notifyItemRangeChanged(internalPosition,mData.size() - internalPosition);
    }

    public void setHeaderViewList(int view){
        if (view != 0){
            headerViews.put(headerViews.size() + TYPE_HEADER,view);
        }
    }

    public void setFooterViewList(int view){
        if (view != 0){
            footerViews.put(footerViews.size() + TYPE_FOOTER,view);
        }
    }

    public int getHeaderViewCount(){
        return headerViews.size();
    }

    public int getFooterViewCount(){
        return footerViews.size();
    }

    private boolean isHeader(int position){
        return position < getHeaderViewCount();
    }

    private boolean isFooter(int position){
        return position >= getHeaderViewCount() + mData.size();
    }
    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e(TAG, "onCreateViewHolder: " + viewType );
        K baseViewHolder = null;
        if (headerViews.get(viewType) != 0){
            baseViewHolder = createBaseViewHolder(
                    LayoutInflater.from(mContext).inflate(headerViews.get(viewType),parent,false));
        }else if (footerViews.get(viewType) != 0){
            baseViewHolder = createBaseViewHolder(
                    LayoutInflater.from(mContext).inflate(footerViews.get(viewType),parent,false));
        }else {
            baseViewHolder = onCreateDefViewHolder(parent, viewType);
        }

        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        if (isFooter(position)){
            return;
        }else if (isHeader(position)){
            return;
        }else {
            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);
            holder.itemView.setTag(holder.getAdapterPosition());
            bindingViewHolder(mContext,holder,mData.get(position - getHeaderViewCount()));
        }
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.onClick(view, (int) view.getTag());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (mOnItemLongClickListener != null){
            mOnItemLongClickListener.onLongClick(view, (int) view.getTag());
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return mData.size() + getHeaderViewCount() + getFooterViewCount() ;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)){
            return headerViews.keyAt(position);
        }else if (isFooter(position)){
            return footerViews.keyAt(position - getHeaderViewCount() - mData.size());
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


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener{
        void onClick(View view,int position);
    }
    public interface  OnItemLongClickListener{
        void onLongClick(View view,int position);

    }
}

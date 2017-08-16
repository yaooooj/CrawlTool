package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.ViewStubCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.RecyclerViewUtil.LoadMode;
import com.example.coustomtoolbar.RecyclerViewUtil.LoadMoreScrollListener;

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
    private SparseIntArray loadingViews;
    private int emptyView;
    private int loadingView;
    private LoadMode loadMode;
    private int firstVisibleItem;
    private int lastVisibleItem;
    private RecyclerView recyclerView;

    private boolean showFooter = false;
    private boolean showHeader = false;
    private boolean loading;

    private int width;
    private int height;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnLoadMoreListener mOnLoadMoreListener;


    public BaseAdapter(Context context,int layoutResId, List<T> data,RecyclerView recyclerView) {
        mContext = context;
        mData = data == null ? new ArrayList<T>() : data;
        if (layoutResId != 0 ){
            this.layoutResId = layoutResId;
        }
        if (recyclerView != null){
            this.recyclerView = recyclerView;
        }
        headerViews = new SparseIntArray();
        footerViews = new SparseIntArray();
        loadingViews = new SparseIntArray();

    }

    public BaseAdapter(Context context, List<T> data,RecyclerView recyclerView) {
        this(context,0,data,recyclerView);
    }

    public BaseAdapter(Context context,int layoutResId,RecyclerView recyclerView) {
        this(context,layoutResId,null,recyclerView);
    }

    public LoadMode getLoadMode() {
        return loadMode;
    }

    public void setLoadMode(LoadMode loadMode) {
        this.loadMode = loadMode;
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
    public void updataData(int position,T data){
        T t = mData.get(position);
        if (t != null && data != null){
            mData.set(position,data);
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
            headerViews.put(headerViews.size() + ViewType.TYPE_HEADER,view);
        }
    }

    public void setFooterViewList(int view){

        if (view != 0){
            footerViews.put(footerViews.size() + ViewType.TYPE_FOOTER,view);
        }
    }

    public int getEmptyView() {
        return emptyView;
    }

    public void setEmptyView(int emptyView) {
        this.emptyView = emptyView;
    }

    public int getLoadingView() {
        return loadingView;
    }

    private int getRealItemCount(){
        return mData.size();
    }
    public int getHeaderViewCount(){
        return headerViews.size();
    }

    public int getFooterViewCount(){
        return footerViews.size();
    }

    public void setLoadingView(int loadingView) {

        loadingViews.put(loadingViews.size(),loadingView);
    }

    private int getLoadingViewCount(){

        return loadingViews.size();
    }

    private boolean isHeader(int position){
        return position < getHeaderViewCount();
    }

    private boolean isFooter(int position){
        return position >= getHeaderViewCount() + getRealItemCount();
    }

    public boolean isShowFooter() {
        return showFooter;
    }

    public void setShowFooter(boolean showFooter) {
        this.showFooter = showFooter;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        setShowFooter(true);
        notifyItemInserted(getRealItemCount() + getHeaderViewCount() +1);
    }

    private boolean isEmpty(){
        return getRealItemCount() == 1;
    }
    public int  getScreenWidth(Context context){

        DisplayMetrics displayMetrics  = context.getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        Log.e(TAG, "getScreenWidth: "+ width );
        return width;
    }

    @Override
    public K onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.e(TAG, "onCreateViewHolder: " + viewType );
        K baseViewHolder = null;
        if (headerViews.get(viewType) != 0){
            baseViewHolder = createBaseViewHolder(parent,headerViews.get(viewType));
        }else if (footerViews.get(viewType) != 0){
            if (!isLoading()){
                baseViewHolder = createBaseViewHolder(parent,footerViews.get(viewType));
            }

        }else if (viewType == ViewType.TYPE_EMPTY){
            baseViewHolder = createBaseViewHolder(parent,getEmptyView());
        }else if (viewType == ViewType.TYPE_LOADING){
            if (isLoading()){
                baseViewHolder = createBaseViewHolder(parent,loadingViews.get(0));
            }
        }else {
            baseViewHolder = onCreateDefViewHolder(parent, viewType);
        }
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (isEmpty()){
         if ( viewType == ViewType.TYPE_EMPTY){
             bindingItemView(mContext,holder,null);
         }
        }else {

            if (isFooter(position)){

            }else if (isHeader(position)){

            }else if (holder.getItemViewType() == ViewType.TYPE_LOADING){

            }
            else {

                holder.itemView.setOnClickListener(this);
                holder.itemView.setOnLongClickListener(this);
                holder.itemView.setTag(holder.getAdapterPosition() - getHeaderViewCount());
                bindingItemView(mContext,holder,mData.get(position - getHeaderViewCount()));
            }
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
        if (getRealItemCount() == 0){
            return 1;
        }else if (isShowFooter()){
            if (isLoading()){
                return getRealItemCount()+ getHeaderViewCount()  + getLoadingViewCount() ;
            }else if (!isLoading()){
                return getRealItemCount()+ getHeaderViewCount() + getFooterViewCount();
            }
        }
        return getRealItemCount()+ getHeaderViewCount();
    }

    @Override
    public int getItemViewType(int position) {

        if (isEmpty()){
            return ViewType.TYPE_EMPTY;
        }else {
            if (isHeader(position)){
                return headerViews.keyAt(position);
            }else if (isFooter(position)){
                if (isLoading()){
                    Log.e(TAG, "getItemViewType: " + "Loading" );
                    return ViewType.TYPE_LOADING;
                }else {
                    Log.e(TAG, "getItemViewType: " + "Footer");
                    return footerViews.keyAt(position - getHeaderViewCount() - mData.size());
                }
            }else {
                return ViewType.TYPE_ITEM;
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(K holder) {
        super.onViewAttachedToWindow(holder);
        int viewType = holder.getItemViewType();
        if (viewType >= ViewType.TYPE_HEADER
                || viewType >= ViewType.TYPE_FOOTER || viewType == ViewType.TYPE_LOADING ){
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams){
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams)lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (viewType >= ViewType.TYPE_HEADER || viewType >= ViewType.TYPE_FOOTER
                            || viewType == ViewType.TYPE_LOADING || viewType == ViewType.TYPE_EMPTY ){
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }

    }

    protected K onCreateDefViewHolder(ViewGroup parent, int viewType) {
        int layoutId = layoutResId;

        return createBaseViewHolder(parent, layoutId);
    }

    protected K createBaseViewHolder(ViewGroup parent, int layoutResId) {
        return createBaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutResId,parent,false));
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
    public void setLoadMoreListener(final OnLoadMoreListener onLoadMoreListener){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                /*
                if (layoutManager instanceof StaggeredGridLayoutManager){
                    ((StaggeredGridLayoutManager) layoutManager).invalidateSpanAssignments();
                }
                */
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount() - getHeaderViewCount();
                Log.e(TAG, "onScrollStateChanged: " + visibleItemCount +" + " + totalItemCount + " + " + lastVisibleItem );
                if (LoadMode.PULLUP == getLoadMode()){
                    if ( newState == RecyclerView.SCROLL_STATE_IDLE ){
                        onLoadMoreListener.setImage();
                    }
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING){
                        if (totalItemCount - lastVisibleItem == 0){
                            onLoadMoreListener.loadMore();
                        }

                    }
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
               // Log.e(TAG, "onScrolled: " + dx );
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    if (layoutManager instanceof LinearLayoutManager) {
                        firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof GridLayoutManager) {
                        firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                        lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        int[] positions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                        // ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(positions);
                        firstVisibleItem = getFirstPosition(((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(positions));
                        lastVisibleItem = getLastPosition(((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(positions));
                    }
                }
            }
        });
    }
    public int getFirstPosition(int[] position){
        int first = position[0];
        for (int value : position){
            if (value < first){
                first = value;
            }
        }
        return first;
    }
    public int getLastPosition(int[] position){
        int last = position[0];
        for (int value : position){
            if (value > last){
                last = value;
            }
        }
        return last;
    }
    public abstract void bindingItemView(Context context, BaseViewHolder holder, T t );



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
    public interface OnLoadMoreListener{
        void loadMore();
        void setImage();
    }
}

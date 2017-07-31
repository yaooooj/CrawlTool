package com.example.coustomtoolbar.RecyclerViewUtil;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.example.coustomtoolbar.Adapter.MyAdapter2;

/**
 * Created by SEELE on 2017/7/30.
 */

public abstract class LoadMoreScrollListener extends RecyclerView.OnScrollListener {
    public static final String TAG = "LoadMoreScrollListener";
    private LoadMode mode;
    private int firstVisibleItem;
    private int lastVisibleItem;
    private boolean isLoading = false;

    private OnLoadMoreListener mOnLoadMoreListener;



    public LoadMoreScrollListener(LoadMode mode) {

        this.mode = mode;

    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        Log.e(TAG, "onScrollStateChanged: " + newState );

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        Log.e(TAG, "onScrollStateChanged: " + newState );
        if ( newState == RecyclerView.SCROLL_STATE_IDLE ){
            if (lastVisibleItem + 1 == totalItemCount){
                if (LoadMode.PULLUP == mode){
                    onLoadMore();
                /*
                if (mOnLoadMoreListener != null){
                    mOnLoadMoreListener.loadMore();
                    return;
                }
                */
                }
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null){
            if (layoutManager instanceof LinearLayoutManager){
                firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

            }else if (layoutManager instanceof GridLayoutManager){
                firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            }else if (layoutManager instanceof StaggeredGridLayoutManager){
                    int[] positions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                   // ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(positions);
                    firstVisibleItem = getFirstPostion(((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(positions));
                    lastVisibleItem = getLastPosition(((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(positions));
            }
        }
    }

    public int getFirstPostion(int[] position){
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


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }


    public void setMode(LoadMode mode) {
        mode = mode;
    }


    public abstract void onLoadMore();

}

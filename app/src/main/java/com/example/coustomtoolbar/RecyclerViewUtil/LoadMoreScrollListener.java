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
    private int span = 0;

    private OnLoadMoreListener mOnLoadMoreListener;

    public LoadMoreScrollListener(LoadMode mode) {
        this.mode = mode;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
       // Log.e(TAG, "onScrollStateChanged: " + newState );
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int count = adapter.getItemCount();
        Log.e(TAG, "onScrollStateChanged: "  + visibleItemCount +" + " + totalItemCount + "  + " + lastVisibleItem);
        if (LoadMode.PULLUP == mode){
            if ( newState == RecyclerView.SCROLL_STATE_IDLE ) {
                        onLoadMore();

            }else if (newState == RecyclerView.SCROLL_STATE_DRAGGING){
                if (span != 0){
                    if (totalItemCount - lastVisibleItem <= span){
                        onDraggerLoadMore();
                    }
                }else {
                    if (totalItemCount - lastVisibleItem == 0){
                        onDraggerLoadMore();
                    }
                }

            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof LinearLayoutManager) {
                firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

            } else if (layoutManager instanceof GridLayoutManager) {
                firstVisibleItem = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                lastVisibleItem = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                span = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
                int[] positions = new int[span];
                // ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(positions);
                firstVisibleItem = getFirstPosition(((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(positions));

                lastVisibleItem = getLastPosition(((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(positions));
            }
        }
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


    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }


    public void setMode(LoadMode mode) {
        mode = mode;
    }


    public abstract void onLoadMore();

    public abstract void onDraggerLoadMore();

}

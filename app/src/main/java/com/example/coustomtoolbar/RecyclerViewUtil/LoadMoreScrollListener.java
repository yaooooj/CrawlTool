package com.example.coustomtoolbar.RecyclerViewUtil;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by SEELE on 2017/7/30.
 */

public abstract class LoadMoreScrollListener extends RecyclerView.OnScrollListener {
    private LoadMode mode;
    private int firstVisibleItem;
    private int lastVisibleItem;
    private boolean isLoading = false;

    private OnLoadMoreListener mOnLoadMoreListener;

    private int[] positions;

    public LoadMoreScrollListener(LoadMode mode) {

        this.mode = mode;
    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (LoadMode.PULLDOWN != mode && LoadMode.PULLUP !=null){
            return;
        }
        if (recyclerView.getAdapter() == null){
            return;
        }

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int visibleItenCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if (layoutManager instanceof StaggeredGridLayoutManager){

        }else


        if (visibleItenCount > 0
                && newState == RecyclerView.SCROLL_STATE_SETTLING
                && lastVisibleItem > totalItemCount - 1){
            if (isLoading){
                return;
            }

            if (LoadMode.PULLUP == mode){
                if (mOnLoadMoreListener != null){
                    mOnLoadMoreListener.loadMore();
                    return;
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

                if (positions == null){
                    positions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(positions);
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(positions);

                    firstVisibleItem = getFirstPostion(positions);
                    lastVisibleItem = getLastPosition(positions);
                }
            }
        }
    }

    public int getFirstPostion(int[] position){
        int first = positions[0];
        for (int value : positions){
            if (value < first){
                first = value;
            }
        }
        return first;
    }
    public int getLastPosition(int[] position){
        int last = positions[0];
        for (int value : positions){
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

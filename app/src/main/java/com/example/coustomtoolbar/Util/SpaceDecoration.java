package com.example.coustomtoolbar.Util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by yaojian on 2017/7/21.
 */

public class SpaceDecoration extends RecyclerView.ItemDecoration {
    private int right;
    private int bottom;
    private int left = 0 ;

    public SpaceDecoration(int right,int bottom) {
        this.right = right;
        this.bottom = bottom;
        //this.left = right;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int childCount = parent.getAdapter().getItemCount();
        int childPosition = parent.getChildAdapterPosition(view);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int spanCount = ((StaggeredGridLayoutManager)layoutManager).getSpanCount();

        if ((childPosition + 1) % spanCount == 0){
            outRect.set(left,0,0,bottom);
        }
        else if ((childCount - (childCount % spanCount)) <= childPosition){
            outRect.set(left,0,right,0);
        }

        outRect.set(left,0,right,bottom);

    }
}

package com.example.coustomtoolbar.RecyclerViewUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SEELE on 2017/7/25.
 */

public class DividerGridItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[] {android.R.attr.listDivider};
    private Drawable mDivider;


    public DividerGridItemDecoration(Context context){
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        drawHorization(c,parent);

        drawVetical(c,parent);
    }

    private int getSpanCount(RecyclerView parent){
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        if (layoutManager instanceof StaggeredGridLayoutManager){
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }else if (layoutManager instanceof GridLayoutManager){
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        }

        return spanCount;
    }

    public void drawHorization(Canvas canvas,RecyclerView parent){

        int childCount = parent.getChildCount();
        for (int i = 0;i < childCount;i++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getLeft() - params.getMarginStart();
            int right = child.getRight() + params.getMarginEnd() + mDivider.getIntrinsicWidth();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left,right,top,bottom);
            mDivider.draw(canvas);
        }
    }

    public void drawVetical(Canvas c,RecyclerView parent){
        int childCount = parent.getChildCount();
        for (int i = 0;i < childCount;i++){
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int left = child.getRight() + params.getMarginEnd();
            int right = left + mDivider.getIntrinsicWidth();
            int top = child.getTop() - params.topMargin;
            int bottom = child.getBottom() + params.bottomMargin;

            mDivider.setBounds(left,right,top,bottom);
            mDivider.draw(c);
        }
    }

    private boolean isLastColum(RecyclerView parent,int pos,int spanCount,int childCount){

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            if ((pos + 1) % spanCount == 0){
                return true;
            }
        }else if (layoutManager instanceof StaggeredGridLayoutManager){
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();

            if (orientation == StaggeredGridLayoutManager.VERTICAL){
                if ((pos + 1) % spanCount == 0){
                    return  true;
                }
            }else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent,int pos,int spanCount,int childCount){
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager){

            childCount = childCount - childCount % spanCount;
            if (pos >= childCount){
                return true;
            }
        }else if (layoutManager instanceof StaggeredGridLayoutManager){
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();

            if (orientation == StaggeredGridLayoutManager.VERTICAL){
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount){
                    return true;
                }

            }else {
                if ((pos + 1) % spanCount == 0){
                    return  true;
                }
            }
        }

        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int childPosition = parent.getChildAdapterPosition(view);
        if (isLastRaw(parent,childPosition,spanCount,childCount)){
            outRect.set(0,0,mDivider.getIntrinsicWidth(),0);
        }else if (isLastColum(parent,childPosition,spanCount,childCount)){
            outRect.set(0,0,0,mDivider.getIntrinsicHeight());
        }else {
            outRect.set(0,0,mDivider.getIntrinsicWidth(),mDivider.getIntrinsicHeight());
        }


    }


}

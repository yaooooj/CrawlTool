package com.example.coustomtoolbar.RecyclerViewUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

/**
 * Created by yaojian on 2017/7/21.
 */

public class SpaceDecoration extends RecyclerView.ItemDecoration {
    private int dividerLeft = 1;
    private int dividerRight = 1;
    private int dividerHeight = 1;
    private static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    private static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    private int orientation;
    private Paint dividerPaint;
    public SpaceDecoration(Context context,int orientation) {
        this.orientation = orientation;
        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setColor(Color.GRAY);
    }

    public void setOrientation(int orientation){
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
            throw  new IllegalArgumentException("invalid orientation");
        }
        this.orientation = orientation;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        int childCount = parent.getAdapter().getItemCount();
        int childPosition = parent.getChildAdapterPosition(view);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof  StaggeredGridLayoutManager){
            int spanCount = ((StaggeredGridLayoutManager)layoutManager).getSpanCount();
            if ((childPosition + 1) % spanCount == 0){
                outRect.set(dividerLeft,0,0,dividerHeight);
            }
            else if ((childCount - (childCount % spanCount)) <= childPosition){
                outRect.set(dividerLeft,0,dividerRight,0);
            }
            outRect.set(dividerLeft,0,dividerRight,dividerHeight);
        }else if (layoutManager instanceof LinearLayoutManager){
            outRect.bottom = dividerHeight;
        }



    }
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        Log.v("item_decoration","onDraw");
        if (orientation ==VERTICAL_LIST){
            drawVertical(c,parent);
        }else {
            drawHorizontal(c,parent);
        }
    }

    public void drawVertical(Canvas canvas, RecyclerView parent){
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();

        for (int i = 0;i < childCount; i++){
            final View view = parent.getChildAt(i);
            final int top = view.getBottom();
            final int bottom = view.getBottom() + dividerHeight;
            canvas.drawRect(left,top,right,bottom,dividerPaint);
        }
    }

    public void drawHorizontal(Canvas canvas,RecyclerView parent){
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View view = parent.getChildAt(i);
            final int left = view.getRight();
            final int right = view.getRight() + dividerRight;
            canvas.drawRect(left,top,right,bottom,dividerPaint);
        }

    }
}

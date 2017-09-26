package com.example.coustomtoolbar.CoustomView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * Created by SEELE on 2017/9/25.
 */

public abstract class FreshViewPager extends ViewGroup {

    public View footer;
    public View header;

    public PullFooter pullFooter;
    public PullHeader pullHeader;

    public int bottomScroll;
    private int lastChildIndex;


    public FreshViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        lastChildIndex = getChildCount() - 1;
    }

    public void setHeader(PullHeader pullHeader) {
        this.pullHeader = pullHeader;
    }

    public void setFooter(PullFooter pullFooter) {
        this.pullFooter = pullFooter;
    }
    public void addHeader(View header) {
        this.header = header;
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(header, layoutParams);
    }

    public void addFooter(View footer) {
        this.footer = footer;
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(footer, layoutParams);
    }




    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0;i < getChildCount();i++){
            View child = getChildAt(i);
            if (child.getVisibility() == GONE){
                continue;
            }
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int contentHeight = 0;

        for (int index = 0;index < getChildCount();index++){
            View child = getChildAt(index);

            if (child.getVisibility() == GONE){
                continue;
            }
            if (child == header){
                child.layout(0,0 - child.getMeasuredHeight(),child.getMeasuredWidth(),0);
            }
            else if(child == footer){
                child.layout(0,contentHeight,getMeasuredWidth(),contentHeight + child.getMeasuredHeight());
            }
            else {

                child.layout(0,contentHeight,child.getMeasuredWidth(),contentHeight + child.getMeasuredHeight());
                if (index <= lastChildIndex){
                    if (child instanceof ScrollView){
                        contentHeight += getMeasuredHeight();
                        continue;
                    }
                    contentHeight += child.getMeasuredHeight();
                }
            }


        }

        bottomScroll = contentHeight - getMeasuredHeight();
    }
}




















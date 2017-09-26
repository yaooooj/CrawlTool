package com.example.coustomtoolbar.CoustomView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;

/**
 * Created by SEELE on 2017/9/25.
 */

public abstract class pdViewPager extends FreshViewPager {
    public int lastYMove;
    public int lastYIntercpt;
    private int lastChildIndex;



    public pdViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;

        int y = (int) ev.getY();

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastYMove = y;
                intercept = false;
                break;

            case MotionEvent.ACTION_MOVE:
                if (y > lastYIntercpt){
                    View child = getFirstVisibleChild();
                    if (child == null){
                        intercept = false;
                    }else if (child instanceof RecyclerView){
                        intercept = rvPullDownIntercept(child);
                    }else if (child instanceof AdapterView){
                        intercept = avPullDownIntercept(child);
                    }else if (child instanceof ScrollView){
                        intercept = svPullDownIntercept(child);
                    }
                }else if (y < lastYIntercpt){
                    View child = getLastVisibleChild();
                    if (child == null){
                        intercept = false;
                    }else if (child instanceof RecyclerView){
                        intercept = rvPullUpIntercept(child);
                    }else if (child instanceof AdapterView){
                        intercept = avPullUpIntercept(child);
                    }else if (child instanceof ScrollView){
                        intercept = svPullUpIntercept(child);
                    }
                }else {
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
            default:
                break;
        }

        lastYIntercpt = y;

        return intercept;
    }

    private View getFirstVisibleChild() {

        for (int i =0; i < getChildCount();i++){
            View child = getChildAt(i);
            if (child.getVisibility() == GONE){
                continue;
            }else {
                return child;
            }
        }
        return null;
    }

    private View getLastVisibleChild() {
        for (int i = lastChildIndex; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            } else {
                return child;
            }
        }
        return null;
    }

    private boolean rvPullDownIntercept(View child) {
        boolean intercpt = false;
        RecyclerView recyclerChild = (RecyclerView) child;
        if (recyclerChild.computeVerticalScrollOffset() < 0 ){
            intercpt = true;
        }
        return intercpt;
    }


    private boolean rvPullUpIntercept(View child) {
        boolean intercpt = false;
        RecyclerView recyclerView = (RecyclerView) child;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeHorizontalScrollRange()){
            intercpt = true;
        }
        return intercpt;
    }

    public boolean avPullDownIntercept(View child) {
        boolean intercept = true;
        AdapterView adapterChild = (AdapterView) child;
        // 判断AbsListView是否已经到达内容最顶部
        if (adapterChild.getFirstVisiblePosition() != 0
                || adapterChild.getChildAt(0).getTop() != 0) {
            // 如果没有达到最顶端，则仍然将事件下放
            intercept = false;
        }
        return intercept;
    }

    public boolean avPullUpIntercept(View child) {
        boolean intercept = false;
        AdapterView adapterChild = (AdapterView) child;

        // 判断AbsListView是否已经到达内容最底部
        if (adapterChild.getLastVisiblePosition() == adapterChild.getCount() - 1
                && (adapterChild.getChildAt(adapterChild.getChildCount() - 1).getBottom() == getMeasuredHeight())) {
            // 如果到达底部，则拦截事件
            intercept = true;
        }
        return intercept;
    }

    public boolean svPullDownIntercept(View child) {
        boolean intercept = false;
        if (child.getScrollY() <= 0) {
            intercept = true;
        }
        return intercept;
    }

    public boolean svPullUpIntercept(View child) {
        boolean intercept = false;
        ScrollView scrollView = (ScrollView) child;
        View scrollChild = scrollView.getChildAt(0);

        if (scrollView.getScrollY() >= (scrollChild.getHeight() - scrollView.getHeight())) {
            intercept = true;
        }
        return intercept;
    }

}














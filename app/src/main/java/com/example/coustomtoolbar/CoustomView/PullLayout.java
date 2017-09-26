package com.example.coustomtoolbar.CoustomView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by SEELE on 2017/9/26.
 */

public class PullLayout extends pdViewPager {

    private static final int MIN_DISTENCE = 50;

    private OnPullListener mListener;

    private PullStatus mStatus = PullStatus.DEFAULT;

    private float damp = 0.5f;

    private int SCROLl_TIME = 300;
    private boolean isRefreshSuccess = false;
    private boolean isLoadSuccess = false;



    public void  setOnPullListener(OnPullListener listener){
        this.mListener = listener;
    }



    public PullLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int y = (int)ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                int dy = y - lastYMove;

                if (getScrollY() < 0){
                    if (header != null){
                        performScroll(dy);
                        if (Math.abs(getScrollY()) > header.getMeasuredHeight()){
                            updateStatus(PullStatus.DOWN_AFTER);
                        }else {
                            updateStatus(PullStatus.DOWN_BEFORE);
                        }
                    }
                }else {
                    if (footer != null){
                        performScroll(dy);
                        if ( getScrollY() >= bottomScroll + footer.getMeasuredHeight()){
                            updateStatus(PullStatus.UP_AFTER);
                        }else {
                            updateStatus(PullStatus.UP_BEFORE);
                        }
                    }

                }
                lastYMove = y;
                break;
            case MotionEvent.ACTION_UP:
                switch (mStatus){
                    case DOWN_BEFORE:
                        scrolltoDefaultStatus(PullStatus.REFRESH_CANCEL_SCROLLING);
                        break;
                    case DOWN_AFTER:
                        scrollToRefreshStatus();
                        break;
                    case UP_BEFORE:
                        scrolltoDefaultStatus(PullStatus.REFRESH_CANCEL_SCROLLING);
                        break;
                    case UP_AFTER:
                        scrollToRefreshStatus();
                        break;
                    default:
                        break;
                }
        }

        lastYIntercpt = 0;
        postInvalidate();
        return true;
    }

    private void  onDefault(){
        isRefreshSuccess = false;
        isLoadSuccess = false;
    }

    public void performScroll(int dy) {
        scrollBy(0, (int) (-dy * damp));
    }

    private void updateStatus(PullStatus status) {
        this.mStatus = status;
        int scrollY = getScrollY();
        //LogUtil.print("status=" + status);
        // 判断本次触摸系列事件结束时,Layout的状态
        switch (status) {
            //默认状态
            case DEFAULT:
                onDefault();
                break;
            //下拉刷新
            case DOWN_BEFORE:
                pullHeader.onDownBefore(scrollY);
                break;
            case DOWN_AFTER:
                pullHeader.onDownAfter(scrollY);
                break;
            case REFRESH_SCROLLING:
                pullHeader.onRefreshScrolling(scrollY);
                break;
            case REFRESH_DOING:
                pullHeader.onRefreshDoing(scrollY);
                mListener.onRefresh();
                break;
            case REFRESH_COMPLETE_SCROLLING:
                pullHeader.onRefreshCompleteScrolling(scrollY, isRefreshSuccess);
                break;
            case REFRESH_CANCEL_SCROLLING:
                pullHeader.onRefreshCancelScrolling(scrollY);
                break;
            //上拉加载更多
            case UP_BEFORE:
                pullFooter.onUpBefore(scrollY);
                break;
            case UP_AFTER:
                pullFooter.onUpAfter(scrollY);
                break;
            case LOADMORE_SCROLLING:
                pullFooter.onLoadScrolling(scrollY);
                break;
            case LOADMORE_DOING:
                pullFooter.onLoadDoing(scrollY);
                mListener.onLoadMore();
                break;
            case LOADMORE_COMPLETE_SCROLLING:
                pullFooter.onLoadCompleteScrolling(scrollY, isLoadSuccess);
                break;
            case LOADMORE_CANCEL_SCROLLING:
                pullFooter.onLoadCancelScrolling(scrollY);
                break;
        }
    }

    //滚动到加载状态
    private void scrolltoLoadStatus() {
        int start = getScrollY();
        int end = footer.getMeasuredHeight() + bottomScroll;
        performAnim(start, end, new AnimListener() {
            @Override
            public void onDoing() {
                updateStatus(PullStatus.LOADMORE_SCROLLING);
            }

            @Override
            public void onEnd() {
                updateStatus(PullStatus.LOADMORE_DOING);
            }
        });
    }

    //滚动到默认状态
    private void scrolltoDefaultStatus(final PullStatus pullStatus){
        int start = getScrollY();
        int end = 0;
        performAnim(start,end, new AnimListener() {
            @Override
            public void onDoing() {

            }

            @Override
            public void onEnd() {

            }
        });
    }
    //停止刷新
    public void stopRefresh(boolean isSuccess) {
        isRefreshSuccess = isSuccess;
        scrolltoDefaultStatus(PullStatus.REFRESH_COMPLETE_SCROLLING);
    }

    //停止加载更多
    public void stopLoadMore(boolean isSuccess) {
        isLoadSuccess = isSuccess;
        scrolltoDefaultStatus(PullStatus.LOADMORE_COMPLETE_SCROLLING);
    }

    //滚动到刷新状态
    private void scrollToRefreshStatus(){
        int start = getScrollY();
        int end = -MIN_DISTENCE;

        performAnim(start, end, new AnimListener() {
            @Override
            public void onDoing() {
                updateStatus(PullStatus.REFRESH_SCROLLING);
            }

            @Override
            public void onEnd() {
                updateStatus(PullStatus.REFRESH_DOING);
            }
        });
    }
    //执行动画
    private void performAnim(int start, int end, final AnimListener listener){

        ValueAnimator animator = ValueAnimator.ofInt(start,end);
        animator.setDuration(SCROLl_TIME).start();

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                scrollTo(0,value);
                postInvalidate();
                listener.onDoing();

            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    interface AnimListener{

        void onDoing();

        void onEnd();
    }
}

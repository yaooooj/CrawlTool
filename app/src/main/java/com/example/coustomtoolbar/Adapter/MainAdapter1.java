package com.example.coustomtoolbar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coustomtoolbar.Activity.MainActivity;
import com.example.coustomtoolbar.ImageCache.GlideApp;
import com.example.coustomtoolbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEELE on 2017/9/10.
 */

public class MainAdapter1 extends BaseAdapter<String,BaseViewHolder> {

    private Activity mActivity;

    public MainAdapter1(Activity activity, int layoutResId, List<String> data, RecyclerView recyclerView) {
        super(activity, layoutResId, data, recyclerView);
        this.mActivity = activity;
    }

    @Override
    public void bingingItemView(BaseViewHolder holder, String s) {
        if (holder.getItemViewType() == ViewType.TYPE_EMPTY) {

        }else if (holder.getItemViewType() == ViewType.TYPE_ITEM){
            holder.setTextView(R.id.image_text,s);
        }else {
            //holder.setViewPager(R.id.main_viewpager);
            //ViewPager viewPager = holder.getView(R.id.main_viewpager);
          //viewPager.setAdapter(new MainViewPagerAdpter());
        }

    }

    private class MainViewPagerAdpter extends PagerAdapter {
        List<Integer> headerUrl;
        List<ImageView> mImageViews;


        public MainViewPagerAdpter() {
            if (headerUrl == null){
                headerUrl = new ArrayList<>();
            }
            headerUrl.add(R.mipmap.bigbitmap);
            headerUrl.add(R.mipmap.chrysanthemum);
            headerUrl.add(R.mipmap.desert);
            headerUrl.add(R.mipmap.hydrangeas);
            headerUrl.add(R.mipmap.koala);
            if (mImageViews == null){
                mImageViews = new ArrayList<>();
            }
            for (int i = 0; i < 5; i++) {
                mImageViews.add(new ImageView(mActivity));
            }
        }

        @Override
        public int getCount() {
            return headerUrl.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT
            );
            ImageView imageView = mImageViews.get(position);
            imageView.setLayoutParams(params);
            GlideApp.with(mActivity)
                    .load(headerUrl.get(position))
                    .placeholder(R.mipmap.ic_favorite_border_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .dontAnimate()
                    .into(imageView);
            container.addView(imageView);


            return imageView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViews.get(position));
        }
    }
}

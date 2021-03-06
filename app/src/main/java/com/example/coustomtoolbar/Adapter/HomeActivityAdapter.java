package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coustomtoolbar.Fragment.BlankFragment;
import com.example.coustomtoolbar.ImageCache.GlideApp;
import com.example.coustomtoolbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEELE on 2017/10/12.
 */

public class HomeActivityAdapter extends RecyclerView.Adapter implements View.OnClickListener,View.OnLongClickListener {

    private List<String> data;
    private Context mContext;
    private Fragment mFragment;
    private int layoutResId;
    private LayoutInflater inflate;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    public HomeActivityAdapter(Context context, int layoutResId, List<String> data){
        this.data = data;
        this.mContext = context;
        this.layoutResId = layoutResId;
        inflate = LayoutInflater.from(context);

    }

    public HomeActivityAdapter(Fragment fragment, int layoutResId, List<String> data){
        this.data = data;
        this.mFragment = fragment;
        this.layoutResId = layoutResId;
        inflate = LayoutInflater.from(fragment.getContext());
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if (viewType == 1){
            viewHolder = new ViewPagerHoldeVew(inflate.inflate(R.layout.blank_item2_layout,parent,false));
        }else {
            viewHolder = new ItemViewHolderView(inflate.inflate(layoutResId,parent,false));
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setBackgroundResource(R.drawable.item_selector);
            if (holder instanceof  ViewPagerHoldeVew){
                ((ViewPagerHoldeVew) holder).mViewPager.setAdapter(new MainViewPagerAdpter());
                holder.itemView.setOnClickListener(this);
                holder.itemView.setOnLongClickListener(this);
                holder.itemView.setTag(holder.getLayoutPosition());
            }

            if (holder instanceof ItemViewHolderView){
                holder.itemView.setOnClickListener(this);
                holder.itemView.setOnLongClickListener(this);
                holder.itemView.setTag(holder.getLayoutPosition());
                ((ItemViewHolderView) holder).mTextView.setText(data.get(position));
            }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (position % 5 == 0){
            return 1;
        }else {
            return 2;
        }
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int viewType = holder.getItemViewType();
        if (viewType ==  1){
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams){
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams)lp;
                p.setFullSpan(true);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.onClick(data, (Integer) v.getTag());
        }

    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null){
            mOnItemLongClickListener.onLongClick(v, (Integer) v.getTag());
        }

        return true;
    }

    private static class ViewPagerHoldeVew extends RecyclerView.ViewHolder{
        ViewPager mViewPager;

        public ViewPagerHoldeVew(View itemView) {
            super(itemView);
            mViewPager = (ViewPager) itemView.findViewById(R.id.blank_item_viewpager);
        }
    }

    private class ItemViewHolderView  extends RecyclerView.ViewHolder{
        TextView mTextView;
        public ItemViewHolderView(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.image_text);
        }
    }

    public interface OnItemClickListener{
        void onClick(List<String> url,int position);
    }

    public interface  OnItemLongClickListener{
        void onLongClick(View view,int position);

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
                mImageViews.add(new ImageView(mFragment.getActivity()));
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
            GlideApp.with(mFragment)
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

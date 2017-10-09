package com.example.coustomtoolbar.Fragment;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coustomtoolbar.Activity.MainActivity;
import com.example.coustomtoolbar.Adapter.MainAdapter1;
import com.example.coustomtoolbar.Adapter.SetWrapperAdapter;
import com.example.coustomtoolbar.Adapter.ViewType;
import com.example.coustomtoolbar.Bean.AllCategory;
import com.example.coustomtoolbar.Bean.PictureCategory;
import com.example.coustomtoolbar.CoustomView.FooterView;
import com.example.coustomtoolbar.CoustomView.HeaderView;
import com.example.coustomtoolbar.CoustomView.FreshViewPager;
import com.example.coustomtoolbar.CoustomView.OnPullListener;
import com.example.coustomtoolbar.CoustomView.SetWrapperFragment;
import com.example.coustomtoolbar.DataBaseUtil.DBManager;
import com.example.coustomtoolbar.DataBaseUtil.SQLiteDbHelper;
import com.example.coustomtoolbar.ImageCache.GlideApp;
import com.example.coustomtoolbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AlertDialog dialog;
    private View dialogView;


    private List<String> pictureCategory;
    private Cursor cursor;
    private DBManager dbManager;
    private MainAdapter1 mAdapter;
    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialogView = View.inflate(getActivity(), R.layout.layout_alert_dialog, null);
        View view  = inflater.inflate(R.layout.fragment_blank, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.blank_viewpager);
        MainViewPagerAdpter1 mainViewPagerAdpter1 = new MainViewPagerAdpter1();
        viewPager.setAdapter(mainViewPagerAdpter1);

        ViewPager viewPager1 = (ViewPager) view.findViewById(R.id.blank_viewpager_1);
        viewPager1.setAdapter(new MainViewPagerAdpter1());

        initData();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.blank_recyclview);
        mAdapter = new MainAdapter1(getActivity(), R.layout.main_base_layout,  pictureCategory, recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.GONE);
        FreshViewPager pullLayout =(FreshViewPager)view.findViewById(R.id.fresh_layout);
        pullLayout.addFooter(new FooterView(getContext()));
        pullLayout.addHeader(new HeaderView(getContext()));

        ArrayList<String> data = new ArrayList<>();
        data.add("Set Wrapper");
        data.add("Set Lock Wrapper");
        data.add("Set Both");






        pullLayout.setOnPullListener(new OnPullListener() {
            @Override
            public boolean onRefresh() {
                SetWrapperFragment setWrapperFragment = SetWrapperFragment.newInstence(123445,null);

                setWrapperFragment.show(getFragmentManager(),"dialog");

                return true;
            }

            @Override
            public boolean onLoadMore() {
               recyclerView.setVisibility(View.VISIBLE);
                return true;
            }


        });

        return view;

    }


    public void showDialog(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment pre = getFragmentManager().findFragmentByTag("SetWrapperFragment");
        if (pre != null){
            ft.remove(pre);
        }
        ft.addToBackStack(null);

        SetWrapperFragment setWrapperFragment = new SetWrapperFragment();
        setWrapperFragment.show(ft,"SetWrapperFragment");
    }
    private void initData() {
        dbManager = DBManager.Instence(getActivity());
        pictureCategory = new ArrayList<>();
        Cursor cursor = dbManager.queryCategory(SQLiteDbHelper.TABLE_ALL_CATEGORY, "category");
        while (cursor.moveToNext()) {
            pictureCategory.add(cursor.getString(cursor.getColumnIndex("category")));
        }
        cursor.close();
    }
    private class MainViewPagerAdpter1 extends PagerAdapter {
        List<Integer> headerUrl;
        List<ImageView> mImageViews;


        public MainViewPagerAdpter1() {
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
                mImageViews.add(new ImageView(getActivity()));
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
            GlideApp.with(BlankFragment.this)
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

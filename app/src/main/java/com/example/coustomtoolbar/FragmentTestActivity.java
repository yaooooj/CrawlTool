package com.example.coustomtoolbar;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.coustomtoolbar.Fragment.Fragment1;
import com.example.coustomtoolbar.Fragment.Fragment2;
import com.example.coustomtoolbar.Fragment.Fragment3;
import com.example.coustomtoolbar.Fragment.PlusOneFragment;
import com.example.coustomtoolbar.Util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class FragmentTestActivity extends AppCompatActivity {
    private ScreenUtil screenUtil = new ScreenUtil();
    private ImageView mImageView;
    private List<Fragment> mFragments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);

        screenUtil.setColor(Color.parseColor("#f19388"));
        screenUtil.StatusView(getWindow());
        mImageView = (ImageView)findViewById(R.id.fragment_test_finish_layout);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
        ViewPager viewPager = (ViewPager)findViewById(R.id.fragment_test_view_pager_layout);
        ViewPagerTestAdapter viewPagerAdapter = new ViewPagerTestAdapter(getSupportFragmentManager(),mFragments);
        viewPager.setAdapter(viewPagerAdapter);
        TabLayout mTab = (TabLayout)findViewById(R.id.fragment_test_tab_layout);
        mTab.setupWithViewPager(viewPager);

    }

    public void initData(){
        mFragments = new ArrayList<>();
        for (int i =0 ; i < 5; i ++){
            mFragments.add(new Fragment1());
        }

    }


    private class ViewPagerTestAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private  final String[] mTitles= new String[]{"Home","Classify","Setting","2222","1111"};
        ViewPagerTestAdapter(FragmentManager fm, List<Fragment> fragment) {
            super(fm);
            fragments =fragment;
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}

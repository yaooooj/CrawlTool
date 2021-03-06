package com.example.coustomtoolbar.Activity;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.example.coustomtoolbar.Fragment.Fragment1;
import com.example.coustomtoolbar.Fragment.Fragment3;
import com.example.coustomtoolbar.Fragment.Fragment4;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.Util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class Coordinator extends AppCompatActivity {
    private String TAG = "Coordinator";
    private List<String> mData;
    private List<Fragment> mFragment;
    private ScreenUtil screenUtil = new ScreenUtil();
    private Toolbar toolbar;
    private ImageView finishView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        screenUtil.setColor(Color.parseColor("#f19388"));
        screenUtil.setStatusView(getWindow());
        toolbar = (Toolbar)findViewById(R.id.toolbar_coordinator);
        finishView = (ImageView)findViewById(R.id.finish);
        finishView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();

        findViewById(R.id.float_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"I'm a SnackBar",Snackbar.LENGTH_SHORT)
                        .setAction("cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }).show();
            }
        });

        ViewPager viewPager = (ViewPager)findViewById(R.id.view_pager_coordinator);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),mFragment);
        viewPager.setAdapter(viewPagerAdapter);
        TabLayout mTab = (TabLayout)findViewById(R.id.tab_layout_coordinator);
        mTab.setupWithViewPager(viewPager);
    }

    public void initData(){
        mFragment = new ArrayList<>();
        mFragment.add(new Fragment1());
       ;
        mFragment.add(new Fragment3());
        mFragment.add(new Fragment4());
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter{
        private List<Fragment> fragments;
        private  final String[] mTitles= new String[]{"Home","Classify","Setting","haha"};
        ViewPagerAdapter(FragmentManager fm,List<Fragment> fragment) {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}

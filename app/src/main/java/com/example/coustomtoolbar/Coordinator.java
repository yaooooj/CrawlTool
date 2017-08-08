package com.example.coustomtoolbar;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import com.example.coustomtoolbar.Fragment.Fragment1;
import com.example.coustomtoolbar.Fragment.Fragment2;
import com.example.coustomtoolbar.Fragment.Fragment3;
import com.example.coustomtoolbar.Fragment.PlusOneFragment;
import com.example.coustomtoolbar.Util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.logging.Handler;

public class Coordinator extends AppCompatActivity {
    private String TAG = "Coordinator";
    private List<String> mData;
    private List<Fragment> mFragment;
    private ScreenUtil screenUtil = new ScreenUtil();
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        screenUtil.setColor(Color.parseColor("#f19388"));
        screenUtil.StatusView(getWindow());
        toolbar = (Toolbar)findViewById(R.id.toolbar_coordinator);
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
        mFragment.add(new Fragment2());
        mFragment.add(new Fragment3());
        mFragment.add(new PlusOneFragment());
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




}

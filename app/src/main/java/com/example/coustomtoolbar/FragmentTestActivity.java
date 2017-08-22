package com.example.coustomtoolbar;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.coustomtoolbar.Bean.PassCategory;
import com.example.coustomtoolbar.Fragment.Fragment1;
import com.example.coustomtoolbar.Fragment.Fragment3;
import com.example.coustomtoolbar.Fragment.Fragment4;
import com.example.coustomtoolbar.Util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class FragmentTestActivity extends AppCompatActivity {
    private static final String TAG = "FragmentTestActivity";
    private ScreenUtil screenUtil = new ScreenUtil();
    private List<Fragment> mFragments;
    private List<PassCategory> categories = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);
        screenUtil.setColor(Color.parseColor("#f19388"));
        screenUtil.StatusView(getWindow());
        ImageView mImageView = (ImageView)findViewById(R.id.fragment_test_finish_layout);
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
        Bundle bundle = getIntent().getBundleExtra("intentBundle");
        if (bundle != null){
            categories = bundle.getParcelableArrayList("category");
        }
        mFragments = new ArrayList<>();
        if (categories != null){
            for (int i =0 ; i < categories.size(); i ++){
                mFragments.add(putArgumentsToFragment(
                        categories.get(i).getName(),categories.get(i).getId())
                );
            }
        }
    }

    public Fragment putArgumentsToFragment(String key,String id){
        Bundle bundleToFragment = new Bundle();
        //Fragment4 fragment = new Fragment4();
        //Fragment1 fragment = new Fragment1();
        Fragment3 fragment = new Fragment3();
        bundleToFragment.putString("name",key);
        bundleToFragment.putString("id",id);
        fragment.setArguments(bundleToFragment);
        return fragment;
    }

    private class ViewPagerTestAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
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
            return categories.get(position).getName();
        }
    }
}

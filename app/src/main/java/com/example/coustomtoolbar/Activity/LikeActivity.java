package com.example.coustomtoolbar.Activity;

import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.coustomtoolbar.Adapter.CollapsingAdapter;
import com.example.coustomtoolbar.Bean.PassCategory;
import com.example.coustomtoolbar.Fragment.Fragment3;
import com.example.coustomtoolbar.Fragment.LikeFragment;
import com.example.coustomtoolbar.Fragment.LoginFragment;
import com.example.coustomtoolbar.Fragment.ZoomFragment;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.Util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class LikeActivity extends AppCompatActivity implements
        LikeFragment.OnFragmentInteractionListener,
        Fragment3.OnFragmentInteractionListener,
        ZoomFragment.OnFragmentInteractionListener{

    private static final String TAG ="LikeActivity";
    private ScreenUtil screenUtil = new ScreenUtil();
    private List<Fragment> mFragments;
    private List<PassCategory> categories = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);


        initData();

        LikeFragment likeFragment = new LikeFragment();
        Bundle bundleToLikeFragment = new Bundle();

        bundleToLikeFragment.putParcelableArrayList("category",
                (ArrayList<? extends Parcelable>) categories);

        likeFragment.setArguments(bundleToLikeFragment);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.like_activtiy,likeFragment)
                .commit();

    }

    public void initData(){
        Bundle bundle = getIntent().getBundleExtra("intentBundle");
        if (bundle != null){
            categories = bundle.getParcelableArrayList("category");
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(View view, int position, int resId) {
        ZoomFragment zoomFragment = new ZoomFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.like_activtiy,zoomFragment)
                .addToBackStack(null)
                .commit();
    }
}

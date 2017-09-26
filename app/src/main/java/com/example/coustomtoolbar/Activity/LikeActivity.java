package com.example.coustomtoolbar.Activity;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.coustomtoolbar.Bean.PassCategory;
import com.example.coustomtoolbar.Fragment.Fragment3;
import com.example.coustomtoolbar.Fragment.LikeFragment;
import com.example.coustomtoolbar.Fragment.ZoomFragment;
import com.example.coustomtoolbar.R;

import java.util.List;

public class LikeActivity extends AppCompatActivity implements
        LikeFragment.OnFragmentInteractionListener,
        Fragment3.OnFragmentInteractionListener,
        ZoomFragment.OnFragmentInteractionListener{

    private static final String TAG ="LikeActivity";
    private List<PassCategory> categories = null;
    private LikeFragment likeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_like);

        initData();
        likeFragment = LikeFragment.newInstance(categories,null);

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
    public void onFragmentInteraction(View view, List<String> url, int position) {


        if (url == null && position < 0){
            return;
        }
        ZoomFragment  zoomFragment = ZoomFragment.newInstance(url,position);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.like_activtiy,zoomFragment)
                .hide(likeFragment)
                .addToBackStack(null)
                .show(zoomFragment)
                .commit();
    }
}

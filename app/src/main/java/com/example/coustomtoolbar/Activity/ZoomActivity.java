package com.example.coustomtoolbar.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.coustomtoolbar.Fragment.LoginFragment;
import com.example.coustomtoolbar.Fragment.ZoomFragment;
import com.example.coustomtoolbar.R;

public class ZoomActivity extends AppCompatActivity implements ZoomFragment.OnFragmentInteractionListener {
    private ZoomFragment zoomFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        zoomFragment = new ZoomFragment();
        init();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.zoom_activity,zoomFragment)
                .commit();
    }

    private void init(){
        Bundle bundle = getIntent().getBundleExtra("bundle");
        zoomFragment.setArguments(bundle);
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

package com.example.coustomtoolbar.Activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.coustomtoolbar.Fragment.LoginFragment;
import com.example.coustomtoolbar.Fragment.ZoomFragment;
import com.example.coustomtoolbar.R;

public class ZoomActivity extends AppCompatActivity implements ZoomFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        ZoomFragment zoomFragment = new ZoomFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.zoom_activity,zoomFragment)
                .commit();
    }



    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

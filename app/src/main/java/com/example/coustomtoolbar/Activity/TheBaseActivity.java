package com.example.coustomtoolbar.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.coustomtoolbar.Fragment.BlankFragment;
import com.example.coustomtoolbar.R;

public class TheBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_base);
        BlankFragment fragment = BlankFragment.newInstance(null,null);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.the_main_activity,fragment)
                .commit();
    }
}

package com.example.coustomtoolbar.Activity;


import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.coustomtoolbar.Fragment.LoginFragment;
import com.example.coustomtoolbar.Fragment.SignFragment;
import com.example.coustomtoolbar.R;

public class LoginActivity extends AppCompatActivity implements
        LoginFragment.OnFragmentInteractionListener,SignFragment.OnFragmentInteractionListener {

    private FragmentManager manager;
    private FragmentTransaction mTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        manager = getSupportFragmentManager();
        mTransaction = manager.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        mTransaction.add(R.id.login_activtiy,loginFragment);
        //mTransaction.addToBackStack("loginFragment");
        mTransaction.commit();
    }


    @Override
    public void onFragmentInteraction(int resId) {
        if (resId == R.id.sign_fragment){
            SignFragment fragment = new SignFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.login_activtiy,fragment);
            fragmentTransaction.addToBackStack("loginFragment");
            fragmentTransaction.commit();
        }
        if (resId == R.id.login_fragment){
            LoginFragment loginFragment = new LoginFragment();
            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.login_activtiy,loginFragment);
            //mTransaction.replace(R.id.login_activtiy,loginFragment);
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }



}

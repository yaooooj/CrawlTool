package com.example.coustomtoolbar.Activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.coustomtoolbar.Fragment.SettingFragment;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.Util.ScreenUtil;

public class SettingActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    private static final  String TAG = "SettingActivity";
    private ScreenUtil screenUtil = new ScreenUtil();
    private static final String KEY_AUTO_UPDATA = "pref_auto_updata";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initStatuCoclor();

        SettingFragment settingFragment = new SettingFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.setting,settingFragment)
                .commit();


    }


    public void initStatuCoclor(){
        screenUtil.setColor(Color.parseColor("#f19388"));
        screenUtil.setStatusView(getWindow());
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}

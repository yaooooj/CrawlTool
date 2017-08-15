package com.example.coustomtoolbar;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.coustomtoolbar.ImageCache.DownloadBitmapExecutor;
import com.example.coustomtoolbar.ImageCache.ImageUrl;
import com.example.coustomtoolbar.Util.ScreenUtil;

import java.util.concurrent.ThreadPoolExecutor;

public class SettingActivity extends AppCompatActivity {
    private ScreenUtil screenUtil = new ScreenUtil();
    private ThreadPoolExecutor executor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        screenUtil.setColor(Color.parseColor("#f19388"));
        screenUtil.StatusView(getWindow());
        //executor = DownloadBitmapExecutor.getInstanceExecutor();
        Button button = (Button)findViewById(R.id.setting_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}

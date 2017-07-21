package com.example.coustomtoolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coustomtoolbar.Bean.TaskModel;
import com.example.coustomtoolbar.DataBaseUtil.DBManager;
import com.example.coustomtoolbar.Util.ScreenUtil;
import com.example.coustomtoolbar.Util.SystemTime;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private String TAG = "MainActivity1";
    private Toolbar mToolbar;
    private boolean isShowToolbar = true;
    private DBManager dbManager;
    private int  isFirstTimeInit = 0;
    private SharedPreferences preference;
    private int count;
    private CardView cardView_1;
    private CardView cardView_2;
    private CardView cardView_3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        initDataBase();
        initToolbar(isShowToolbar);
        initButton();

    }

    private void initDataBase(){
        dbManager = DBManager.Instence(MainActivity.this);
        firstTimeInit();
    }

    private void initToolbar(boolean  isShowToolbar){
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        int statusHeight = ScreenUtil.getStatusHeight(this);
        Log.e(TAG,"The Status Height is = " + statusHeight);
        //setToolbarPaddingTop();
    }
    public void initButton(){
        TextView textView_1 = (TextView) findViewById(R.id.textView_1);
        TextView textView_2 = (TextView) findViewById(R.id.button_coo);
        TextView textView_3 = (TextView) findViewById(R.id.button_card_view);
        cardView_1 = (CardView)findViewById(R.id.card_view_1);
        cardView_1.setOnClickListener(this);

        cardView_2 = (CardView)findViewById(R.id.card_view_2);
        cardView_2.setOnClickListener(this);

        cardView_3 = (CardView)findViewById(R.id.card_view_3);
        cardView_3.setOnClickListener(this);

    }

    public void firstTimeInit(){
        preference = getSharedPreferences("count",MODE_PRIVATE);
        count = preference.getInt("count",0);
        if (count == 0){
            writeInitParamsToSharePreferences();
            Log.e(TAG, "firstTimeInit: "+ "first init database" );
        }
    }
    public void writeInitParamsToSharePreferences(){
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt("count",++count);
        editor.apply();
    }

    public void isShowToolbar(boolean show){
        isShowToolbar = show;
    }
    public void setToolbarPaddingTop(){
        int paddingTop = mToolbar.getPaddingTop();
        int paddingLeft = mToolbar.getPaddingTop();
        int paddingRight = mToolbar.getPaddingRight();
        int paddingBottom = mToolbar.getPaddingBottom();
        int statusHeight = ScreenUtil.getStatusHeight(this);
        Log.d(TAG,"The Status Height is = " + statusHeight);
        ViewGroup.LayoutParams params =  mToolbar.getLayoutParams();
        int height = params.height;
        paddingTop += statusHeight;
        height += statusHeight;
        params.height = height;
        Log.d(TAG,"The Padding Top is = " + paddingTop);
        mToolbar.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_view_1:
                Intent intent = new Intent(MainActivity.this,BaseActivity.class);
                startActivity(intent);
                break;
            case R.id.card_view_2:
                Intent intent_coo = new Intent(MainActivity.this,Coordinator.class);
                startActivity(intent_coo);
                //Toast.makeText(MainActivity.this,"hit the button_coo",Toast.LENGTH_SHORT).show();
                break;
            case R.id.card_view_3:
                Intent intent_card_view = new Intent(MainActivity.this,AddTaskActivity.class);
                startActivity(intent_card_view);
                //Toast.makeText(MainActivity.this,"hit the button_coo",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }
}

package com.example.coustomtoolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coustomtoolbar.Adapter.BaseAdapter;
import com.example.coustomtoolbar.Adapter.MainAdapter;
import com.example.coustomtoolbar.Bean.AllCategory;
import com.example.coustomtoolbar.Bean.PictureCategory;
import com.example.coustomtoolbar.DataBaseUtil.DBManager;
import com.example.coustomtoolbar.DataBaseUtil.SQLiteDbHelper;
import com.example.coustomtoolbar.RecyclerViewUtil.DividerItemDecoration;
import com.example.coustomtoolbar.RecyclerViewUtil.LoadMode;
import com.example.coustomtoolbar.Util.OkHttp3Util;
import com.example.coustomtoolbar.Util.ScreenUtil;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;

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
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private OkHttp3Util okHttp3Util;
    private Gson gson;
    private AllCategory allCategory;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            PictureCategory picture = (PictureCategory)msg.obj;
            allCategory = picture.getShowapi_res_body();
            for (int i = 0;i < allCategory.getList().size();i++ ){
                dbManager.addCateory(allCategory.getList().get(i).getName());
                for (int j =0;j < allCategory.getList().get(i).getList().size();j++){
                    dbManager.addConcreteCategory(new String[] {allCategory.getList().get(i).getList().get(j).getId()
                                    ,allCategory.getList().get(i).getList().get(j).getName()}
                            );

                }
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        initStatusColor();
        initDataBase();
       // initToolbar(isShowToolbar);
        initImageView();
        initRecycler();

    }
    public void initStatusColor(){
        ScreenUtil screenUtil = new ScreenUtil();
        screenUtil.setColor(Color.parseColor("#dedede"));
        screenUtil.StatusView(getWindow());
    }
    private void initDataBase(){
        dbManager = DBManager.Instence(MainActivity.this);
        okHttp3Util = new OkHttp3Util();
        gson = new Gson();
        firstTimeInit();
    }

    private void initToolbar(boolean  isShowToolbar){
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        int statusHeight = ScreenUtil.getStatusHeight(this);
        Log.e(TAG,"The Status Height is = " + statusHeight);
        //setToolbarPaddingTop();
    }
    public void initImageView(){
        int statusHeight = ScreenUtil.getStatusHeight(this);
        Log.e(TAG,"The Status Height is = " + statusHeight);
        imageView1 = (ImageView)findViewById(R.id.nvg);
        imageView2 = (ImageView)findViewById(R.id.favorite);
        imageView3 = (ImageView)findViewById(R.id.setting);
        imageView3.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView1.setOnClickListener(this);
    }

    public void initRecycler(){
        List<Integer> bitmaps = new ArrayList<>();
        for (int i = 0; i < 10;i++){
            bitmaps.add(R.mipmap.ic_favorite_black_24dp);
        }
        mRecyclerView = (RecyclerView)findViewById(R.id.main_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mAdapter = new MainAdapter(this,R.layout.main_base_layout,bitmaps,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(MainActivity.this,"this " + position,Toast.LENGTH_SHORT).show();
                if (position == 0){
                    intentActivity(Coordinator.class);
                }
            }
        });
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }

    public void firstTimeInit(){
        preference = getSharedPreferences("count",MODE_PRIVATE);
        count = preference.getInt("count",0);
        if (count == 0){
            writeInitParamsToSharePreferences();
            okHttp3Util.executeGet(OkHttp3Util.URL,handler,PictureCategory.class);
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
    public void intentActivity(Class activity){
        Intent intent = new Intent(MainActivity.this,activity);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nvg:
                intentActivity(SettingActivity.class);
                break;
            case R.id.favorite:
                break;
            case R.id.setting:
                intentActivity(SettingActivity.class);
            default:
                break;
        }

    }
}

package com.example.coustomtoolbar.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.GlideBuilder;
import com.example.coustomtoolbar.Adapter.BaseAdapter;
import com.example.coustomtoolbar.Adapter.MainAdapter1;
import com.example.coustomtoolbar.Bean.AllCategory;
import com.example.coustomtoolbar.Bean.PassCategory;
import com.example.coustomtoolbar.Bean.PictureCategory;
import com.example.coustomtoolbar.DataBaseUtil.DBManager;
import com.example.coustomtoolbar.DataBaseUtil.SQLiteDbHelper;
import com.example.coustomtoolbar.NetUtil.OkHttp3Util;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.Util.ScreenUtil;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

import static android.graphics.Color.TRANSPARENT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity1";
    private static final int NULL_BUNDLE = -1;
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
    //private MainAdapter mAdapter;
    private MainAdapter1 mAdapter;
    private OkHttp3Util okHttp3Util;
    private Gson gson;
    private AllCategory allCategory;
    private List<String> pictureCategory;
    private Cursor cursor;
    private GlideBuilder builder;
    private AlertDialog dialog;
    private View view;
    private SharedPreferences.Editor editor;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            PictureCategory picture = (PictureCategory)msg.obj;
            allCategory = picture.getShowapi_res_body();
            for (int i = 0;i < allCategory.getList().size();i++ ){
                dbManager.addCategory(allCategory.getList().get(i).getName());

                //updata the main activity recycler data
                mAdapter.addData(allCategory.getList().get(i).getName());
                for (int j =0;j < allCategory.getList().get(i).getList().size();j++){
                    dbManager.addConcreteCategory(new String[] {
                                    allCategory.getList().get(i).getList().get(j).getId()
                                    ,allCategory.getList().get(i).getList().get(j).getName()}
                    );
                }
            }

            dialog.dismiss();
            writeInitParamsToSharePreferences();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        view = View.inflate(this,R.layout.layout_alert_dialog,null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(this,R.xml.argument1,false);
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        .setApplicationId("565a11c9e57a1f1a61b20d5fb2d08134")
        ////请求超时时间（单位为秒）：默认15s
        .setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        .setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        .setFileExpiration(2500)
        .build();
        Bmob.initialize(config);

        Stetho.initializeWithDefaults(this);
        initStatusColor();
        initShowDialog();
        initDataBase();
        initGlide();
        initToolbar(isShowToolbar);
        initImageView();
        initRecycler();
        //initData();

    }

    public void initStatusColor(){
        ScreenUtil screenUtil = new ScreenUtil();
        //screenUtil.setColor(Color.parseColor("#dedede"));
        screenUtil.setColor(TRANSPARENT);
        screenUtil.StatusView(getWindow());
    }

    private void initShowDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialog = builder.create();

        dialog.setView(view);
        dialog.show();
    }

    private void initDataBase(){
        dbManager = DBManager.Instence(MainActivity.this);
        okHttp3Util = new OkHttp3Util(getApplicationContext());
        pictureCategory = new ArrayList<>();
        gson = new Gson();
        //Glide.get(getApplicationContext()).onConfigurationChanged();
        firstTimeInit();
    }

    private void initGlide(){


    }

    private void initToolbar(boolean  isShowToolbar){
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        int statusHeight = ScreenUtil.getStatusHeight(this);
        Log.e(TAG,"The Status Height is = " + statusHeight);
    }

    private void initData(){
        while (true){
            cursor = dbManager.queryCategory(SQLiteDbHelper.TABLE_ALL_CATEGORY,"category");

                while (cursor.moveToNext()){
                    pictureCategory.add(cursor.getString(cursor.getColumnIndex("category")));
                    Log.e(TAG, "handleMessage: "+cursor.getString(cursor.getColumnIndex("category")) );
                }
                mAdapter.addData(pictureCategory);

            if (pictureCategory != null){
                break;
            }
            cursor.close();
        }

    }
    public void initImageView(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.tab_layout);
        //layout.setAlpha((float) 0.0);
        //layout.setBackgroundColor(Color.parseColor("#00000000"));
        imageView1 = (ImageView)findViewById(R.id.nvg);
        imageView2 = (ImageView)findViewById(R.id.favorite);
        imageView3 = (ImageView)findViewById(R.id.setting);

        //imageView1.setBackgroundColor(Color.parseColor("#10000000"));
        //imageView2.setAlpha((float) 0.0);
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
        mAdapter = new MainAdapter1(getApplicationContext(),R.layout.main_base_layout,pictureCategory,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        //mAdapter.setHeaderViewList();
        mAdapter.setEmptyView(R.layout.empty_layout);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<String>() {
            @Override
            public void onClick(View view, List<String> url, int position) {
                Toast.makeText(MainActivity.this,"this " + position,Toast.LENGTH_SHORT).show();
                intentActivity(LikeActivity.class,position);
                if (position == 0){
                    intentActivity(Coordinator.class,NULL_BUNDLE);
                }
            }
        });
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }

    public void firstTimeInit(){
        preference = getSharedPreferences("count",MODE_PRIVATE);
        count = preference.getInt("count",0);
        if (count == 0){

            okHttp3Util.executeGet(OkHttp3Util.URL,handler,PictureCategory.class,2);
            Log.e(TAG, "firstTimeInit: "+ "first init database" );
        }else {
            Cursor cursor = dbManager.queryCategory(SQLiteDbHelper.TABLE_ALL_CATEGORY,"category");
            while (cursor.moveToNext()){
                pictureCategory.add(cursor.getString(cursor.getColumnIndex("category")));
                Log.e(TAG, "firstTimeInit: " + cursor.getString(cursor.getColumnIndex("category")) );
            }
            dialog.dismiss();
            cursor.close();
        }
    }
    public void writeInitParamsToSharePreferences(){
        editor = preference.edit();
        editor.putInt("count",++count);
        editor.apply();
    }

    public void intentActivity(Class activity,int position){
        Intent intent = new Intent(MainActivity.this,activity);
        if (position != -1){
            Bundle bundle = new Bundle();
            //bundle.putParcelable("category",getConcreteCategory(position));
            bundle.putParcelableArrayList(
                    "category", (ArrayList<? extends Parcelable>) getConcreteCategory(position));
            intent.putExtra("intentBundle",bundle);
        }
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nvg:
                intentActivity(LoginActivity.class,NULL_BUNDLE);
                break;
            case R.id.favorite:
                intentActivity(LikeActivity.class,NULL_BUNDLE);
                break;
            case R.id.setting:
                intentActivity(SettingActivity.class,NULL_BUNDLE);
            default:
                break;
        }

    }

    public List<PassCategory> getConcreteCategory(int id){
        final int realId1 = id * 1000;
        final int realId2 = id * 1000 + 999;
        PassCategory category = null;
        List<PassCategory> mCategory = new ArrayList<>();
        Cursor cursor = dbManager.queryCategoryId(
                "category_id,category_name",SQLiteDbHelper.TABLE_CONCRETE_CATEGORY,"category_id > " + realId1 + " and category_id < " + realId2);
        while (cursor.moveToNext()){
            Log.e(TAG, "getConcreteCategory: " + cursor.getString(cursor.getColumnIndex("category_id")));
            Log.e(TAG, "getConcreteCategory: " +  cursor.getString(cursor.getColumnIndex("category_name")));
            category = new PassCategory();
            category.setId(cursor.getString(cursor.getColumnIndex("category_id")));
            category.setName(cursor.getString(cursor.getColumnIndex("category_name")));
            mCategory.add(category);
        }
        cursor.close();
        if (category != null){
            return mCategory;
        }
        return  null;
    }
}

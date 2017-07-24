package com.example.coustomtoolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.coustomtoolbar.Bean.PictureCategory;
import com.example.coustomtoolbar.DataBaseUtil.DBManager;
import com.example.coustomtoolbar.DataBaseUtil.SQLiteDbHelper;
import com.example.coustomtoolbar.Util.OkHttp3Util;
import com.example.coustomtoolbar.Util.ScreenUtil;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;

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
    private OkHttp3Util okHttp3Util;
    private Gson gson;
    //private AllCategory allCategory;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            PictureCategory picture = (PictureCategory)msg.obj;
            /*allCategory = picture.getShowapi_res_body();
            for (int i = 0;i < allCategory.getList().size();i++ ){
                dbManager.addCateory(allCategory.getList().get(i).getName());
                for (int j =0;j < allCategory.getList().get(i).getList().size();j++){
                    dbManager.addConcreteCategory(new String[] {allCategory.getList().get(i).getList().get(j).getId()
                                    ,allCategory.getList().get(i).getList().get(j).getName()}
                            );

                }
            }
             */
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        initDataBase();
        initToolbar(isShowToolbar);
        initButton();
        initRecycler();

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

    public void initRecycler(){

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
                /*
                Cursor cu = dbManager.queryCategory(SQLiteDbHelper.TABLE_ALL_CATEGORY,"category");
                if(cu != null){
                    while (cu.moveToNext()){
                        String category = cu.getString(cu.getColumnIndex("category"));
                        Log.e(TAG, "handleMessage: " + category );
                    }
                }
                */
                Cursor cursor = dbManager.queryCategory(SQLiteDbHelper.TABLE_CONCRETE_CATEGORY,"category_id");
                if (cursor != null){
                    while (cursor.moveToNext()){
                        //String id = cursor.getInt(cursor.getColumnIndex("category_id"));
                        String id = cursor.getString(cursor.getColumnIndex("category_id"));
                        Log.e(TAG, "handleMessage: " + id );
                    }
                }

                break;
            default:
                break;
        }

    }
}

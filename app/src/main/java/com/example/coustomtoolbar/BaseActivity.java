package com.example.coustomtoolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.example.coustomtoolbar.Adapter.BaseTaskAdapter;
import com.example.coustomtoolbar.Bean.TaskModel;
import com.example.coustomtoolbar.DataBaseUtil.DBManager;
import com.example.coustomtoolbar.Util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends AppCompatActivity {
    public static final String TAG = "BaseActivity";
    private List<TaskModel> mData;
    private ScreenUtil screenUtil = new ScreenUtil();
    private Toolbar toolbar;
    private RecyclerView rv1;
    private BaseTaskAdapter adapter;
    private DBManager dbManager;
    private RecyclerView.LayoutManager layoutManager;
    private Menu mMenu;
    private FloatingActionButton float_btu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initStatusColor();
        initToolbar();
        initFloatButton();
        initData();
        initDatabase();
        initRecyclerView();
        initService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: " );
        adapter.updata();
    }

    public void initStatusColor(){
        screenUtil.setColor(Color.parseColor("#f19388"));
        screenUtil.StatusView(getWindow());
        Log.e("BaseActivity","has been execute");
    }
    public void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_base);
        toolbar.setTitle("SystemCreate");
        setSupportActionBar(toolbar);
        setToolbarNavigation();

    }
    public void initFloatButton(){
        float_btu = (FloatingActionButton)findViewById(R.id.float_btn);
        hideFloatButton();
    }

    //
    // 初始化RecyclerView数据
    public void initData(){
       // mData = new ArrayList<>();
        //taskModel.setTask_num(0);
       // mData.add(new TaskModel());
    }

    public void initDatabase(){
        dbManager = DBManager.Instence(BaseActivity.this);

        if (dbManager.queryWithSQL() == null){
            mData = new ArrayList<>();
        }else {
            mData = dbManager.queryWithSQL();
        }


    }
    public void initRecyclerView(){
        rv1 = (RecyclerView)findViewById(R.id.view_recycler_base);
        adapter = new BaseTaskAdapter(BaseActivity.this,mData,rv1);
        layoutManager = new LinearLayoutManager(BaseActivity.this,
                LinearLayoutManager.VERTICAL,false);
        rv1.setLayoutManager(layoutManager);
        rv1.setItemAnimator(new DefaultItemAnimator());
        rv1.setAdapter(adapter);
        setHeaderView(rv1);
        adapter.setOnBaseItemClickListener(new BaseTaskAdapter.OnBaseItemClickListener() {
            @Override
            public void itemClick(View v, int position) {

                Log.e(TAG, "itemClick: "+position );
            }

            @Override
            public void itemLOngClick(View v, int position) {
                hideNavigation();
                hideMenu();
                visibleFloatButton();
                adapter.setCheckVisible(true);
                Toast.makeText(BaseActivity.this,"Long Click",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "itemLongClick: "+position );
            }
        });
    }
    private void initService(){
        Intent intent = new Intent(this,MyService.class);
        startService(intent);
    }


    public void hideFloatButton(){
        float_btu.setVisibility(View.GONE);
    }


    public void visibleFloatButton(){
        float_btu.setVisibility(View.VISIBLE);
        float_btu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleNavigation();            //显示标题栏返回键
                setMenuVisible();               //显示标题栏添加按钮
                hideFloatButton();              //隐藏FLOAT ACTION BUTTON
                adapter.setCheckVisible(false); //隐藏ITEM 中的CHECKBOX按钮
                adapter.removeItem();           //移除选中的标题栏
                Toast.makeText(BaseActivity.this,"delete success",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setHeaderView(RecyclerView recyclerView){
        View header = LayoutInflater.from(BaseActivity.this).inflate(R.layout.layout_base_item_header,recyclerView,false);
        adapter.setHeaderView(header);
    }
    public void setToolbarNavigation(){
        visibleNavigation();
    }

    public void hideNavigation(){
        toolbar.setNavigationIcon(R.mipmap.ic_undo_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleNavigation();
                setMenuVisible();
                hideFloatButton();
               // hideCheckBox();
                adapter.setCheckVisible(false);
            }
        });
       // toolbar.setNavigationContentDescription("SelectAll");
    }
    public void visibleNavigation(){
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);
        //add = menu.findItem(R.id.add);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.mMenu = menu;
        setMenuVisible();
        return super.onPrepareOptionsMenu(menu);
    }

    public void setMenuVisible(){
        if (mMenu != null){
                    mMenu.findItem(R.id.add).setVisible(true);
                    mMenu.findItem(R.id.remove).setVisible(false);
        }
    }

    public void hideMenu(){
        if (mMenu != null){
            mMenu.findItem(R.id.add).setVisible(false);
            mMenu.findItem(R.id.remove).setVisible(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Intent intentToTaskActivity = new Intent(BaseActivity.this,AddTaskActivity.class);
                startActivity(intentToTaskActivity);
                break;
            case R.id.remove:
                Toast.makeText(BaseActivity.this,"remove",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }


}

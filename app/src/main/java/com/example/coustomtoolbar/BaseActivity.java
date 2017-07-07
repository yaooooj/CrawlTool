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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;
import com.example.coustomtoolbar.Adapter.BaseTaskAdapter;
import com.example.coustomtoolbar.Bean.TaskModel;
import com.example.coustomtoolbar.DataBaseUtil.DBManager;
import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends AppCompatActivity {
    public static final String TAG = "BaseActivity";
    private List<TaskModel> mData;
    private ScreenUtil screenUtil = new ScreenUtil();
    private int count = 0;
    private Toolbar toolbar;
    private RecyclerView rv1;
    private BaseTaskAdapter adapter;
    private DBManager dbManager;
    private RecyclerView.LayoutManager layoutManager;
    private Menu mMenu;
    private FloatingActionButton float_btu;
    private CheckBox checkBox;
    private SparseArray checkList;
    private int size;
    private List<Integer> mBoxPositionList;

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
        mData = new ArrayList<>();
        mData = dbManager.queryWithSQL();
        for (int i =1; i< mData.size();i++){
            Log.e(TAG, "initDatabase: "+mData.get(i).getTask_name()+"this is: = " +i );
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
                setCheckList();
                visibleCheckBox();
                //adapter.countRadioButton();
                Toast.makeText(BaseActivity.this,"Long Click",Toast.LENGTH_SHORT).show();
                Log.e(TAG, "itemLongClick: "+position );
            }
        });
    }
    public void setCheckList(){
        if ( adapter.getCheckList() != null){
            checkList = adapter.getCheckList();
            size = adapter.getCheckList().size();

        }
    }

    public void setCheckBoxPosition(){

        for (int i =0;i < size;i++){
            mBoxPositionList = new ArrayList<>();
            mBoxPositionList.add(checkList.keyAt(i));
        }
    }
    public void hideCheckBox(){
        for (int i =0;i < size;i++){
            checkBox = (CheckBox) checkList.valueAt(i);
            checkBox.setVisibility(View.GONE);
        }
    }
    public void visibleCheckBox(){
        for (int i =0;i < size;i++){
            checkBox = (CheckBox) checkList.valueAt(i);
            checkBox.setVisibility(View.VISIBLE);
        }
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
                hideCheckBox();                 //隐藏ITEM 中的CHECKBOX按钮
                adapter.removeItem(mBoxPositionList);           //移除选中的标题栏
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
                hideCheckBox();
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

package com.example.coustomtoolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bigkoo.pickerview.TimePickerView;
import com.example.coustomtoolbar.Adapter.AddTaskAdapter;
import com.example.coustomtoolbar.Bean.CardViewBean;
import com.example.coustomtoolbar.Bean.CardViewModel;
import com.example.coustomtoolbar.Bean.TaskModel;
import com.example.coustomtoolbar.DataBaseUtil.DBManager;
import com.example.coustomtoolbar.Util.DividerItemDecoration;
import com.example.coustomtoolbar.Util.ScreenUtil;
import com.example.coustomtoolbar.Util.SystemTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String TAG = "AddTaskActivity";
    private ScreenUtil screenUtil;
    private Toolbar toolbar;
    private RecyclerView rv_add_task;
    private AddTaskAdapter addTaskAdapter;
    private List<CardViewBean> mData;
    private CardViewBean cardViewBean;
    private TimePickerView pvTime;
    private TextView device;
    private Button button;
    private int editTextPosition;
    private LinearLayout lp;  //取消EditText的焦点，使LinerLayout获取焦点
    private DBManager dbManager;
    private TaskModel taskModel;
    private List<String> mTaskModelList;
    private SystemTime systemTime;
    private int itemCount;
    private String[] edit;
    private List<TaskModel> taskModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_task);
        initStatusColor();
        initToolbar();
        initTextView();
        initButton();
        initDatabase();
        initData();
        initRecyclerView();
        initTimePicker();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_task,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_task:
                setTaskModel();
                //getTaskModel();
                //Toast.makeText(AddTaskActivity.this, "save success", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public void initStatusColor(){
        screenUtil = new ScreenUtil();
        screenUtil.setColor(Color.parseColor("#f19388"));
        screenUtil.StatusView(getWindow());
        Log.e("BaseActivity","has been execute");
    }
    public void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_add_task);
        toolbar.setTitle("SystemCreate");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initTextView(){
        device = (TextView)findViewById(R.id.device);
    }

    public void initButton(){
        button = (Button)findViewById(R.id.confirm);
        button.setOnClickListener(AddTaskActivity.this);
        lp = (LinearLayout)findViewById(R.id.root_view);
    }

    public void initTimePicker(){
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
               // String textView = addTaskAdapter.getTextView();
                addTaskAdapter.updata(getTime(date));
                addTaskAdapter.setDataAfterChang(getTime(date),AddTaskAdapter.DATA_TYPE,getPosition());

            }
        })
                .setLabel("年","月","日","时","分","秒")
                .isCenterLabel(false)
                .build();
        addTaskAdapter.setTimePicker(pvTime);

    }

    public void initDatabase(){
        dbManager = DBManager.Instence(AddTaskActivity.this);
        taskModelList = new ArrayList<>();
    }
    public void initData(){
        systemTime = SystemTime.getInstance();
        mData = new ArrayList<>();
        //给输入栏中设置默认的值
        String[] text = new String[]{"TaskName:","Creator:","Operator:","StartTime:","EndTime:"};
        edit = new String[]{"Create a new task","Jobs","Tom",systemTime.getTimeWithFormat(),systemTime.getAfterDay()};
        for (int i = 0; i < 5;i++){
            cardViewBean = new CardViewBean();
            cardViewBean.setTextName(text[i]);
            cardViewBean.setEditName(edit[i]);
            //设置默认的值传递给列表中，传给recycler
            mData.add(cardViewBean);
            //db.addWithSQL();
        }
    }
    public void initRecyclerView(){
        rv_add_task = (RecyclerView)findViewById(R.id.view_recycler_add_task);
        addTaskAdapter = new AddTaskAdapter(AddTaskActivity.this,mData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AddTaskActivity.this,
                LinearLayoutManager.VERTICAL,false);
        rv_add_task.setLayoutManager(layoutManager);
        rv_add_task.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        addTaskAdapter.setOnItemClickListener(new AddTaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setPosition(position);
            }

            @Override
            public void onItemLongClick() {

            }
        });
        rv_add_task.setAdapter(addTaskAdapter);

    }

    private String getTime(Date data){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return format.format(data);
    }
    public void setPosition(int position){
        this.editTextPosition = position;
    }
    public int getPosition(){
        return editTextPosition;
    }

    public void setTaskModel(){
        mTaskModelList = addTaskAdapter.getEditText();
        taskModel = new TaskModel();
        //自动改变任务名的名字，避免重复
        if (mTaskModelList.get(0).equals(edit[0])){
            taskModel.setTask_name(mTaskModelList.get(0)+"  "+  systemTime.getTimeWithFormat());
        }else {
            taskModel.setTask_name(mTaskModelList.get(0)+"  "+ systemTime.getTimeWithFormat());
        }
        taskModel.setCreator(mTaskModelList.get(1));
        taskModel.setOperator(mTaskModelList.get(2));
        taskModel.setStart_time(mTaskModelList.get(3));
        taskModel.setEnd_time(mTaskModelList.get(4));
        taskModel.setTask_num(itemCount);
        taskModel.setCreate_time(systemTime.getTimeWithFormat());
    }
    public TaskModel getTaskModel(){
        if (taskModel != null){
            return taskModel;
        }
        else {
            return null;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm:
                setTaskModel();
                Intent intent = new Intent(AddTaskActivity.this,BaseActivity.class);
                startActivity(intent);
                if (taskModel != null){
                    taskModelList.add(taskModel);
                }
                if (taskModelList != null){
                    dbManager.addWithSQL(taskModelList);
                }
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            if (isShouldHideInput(v,ev)){

                InputMethodManager imm =
                        (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null){
                    imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                }
                lp.requestFocus();
            }
            return super.dispatchTouchEvent(ev);
        }

        if (getWindow().superDispatchTouchEvent(ev)){
            return true;
        }

        return false;
    }
    public boolean isShouldHideInput(View v,MotionEvent e){

        if (v != null && (v instanceof EditText)){
            int[] leftop = {0,0};
            v.getLocationInWindow(leftop);
            int left = leftop[0];
            int top = leftop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (e.getX() > left && e.getX() <right && e.getY() > top && e.getY() < bottom){
                return false;
            }
            else {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: "+ "has been stop" );
    }
}

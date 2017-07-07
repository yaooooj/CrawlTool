package com.example.coustomtoolbar.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coustomtoolbar.Adapter.MyAdapter;
import com.example.coustomtoolbar.Bean.CardViewBean;
import com.example.coustomtoolbar.Bean.TaskBean;
import com.example.coustomtoolbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/6/23.
 */

public class Fragment1 extends Fragment implements View.OnClickListener{
    private TaskBean taskBean;
    private List<TaskBean> mData;
    private MyAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_fragment_1,container,false);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar_coordinator);
       // toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
        initData();
        //RecyclerView
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
       adapter = new MyAdapter(getActivity(),mData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
               getActivity(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setOnClickListener(this);
        return view;
    }
    public void updata(List<TaskBean> data){
        mData = data;

    }
    public void initData(){
        mData = new ArrayList<>();
        for (int i = 0;i <= 40;i++){
            taskBean = new TaskBean();
            taskBean.setTask_name("Task Name Is " + i);
            taskBean.setTask_num(i);
            mData.add(taskBean);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.rv:


        }
    }
    public MyAdapter getAdapter(){
        return adapter;
    }
}

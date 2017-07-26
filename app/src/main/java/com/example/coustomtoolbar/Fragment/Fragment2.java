package com.example.coustomtoolbar.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coustomtoolbar.Adapter.MyAdapter;
import com.example.coustomtoolbar.Adapter.MyAdapter2;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.Util.DividerGridItemDecoration;
import com.example.coustomtoolbar.Util.SpaceDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/6/23.
 */

public class Fragment2 extends Fragment {

    private View view;
    private List<String> mData;
    private MyAdapter2 adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fragment_1,container,false);
        initData();
        //RecyclerView


        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
        adapter = new MyAdapter2(getActivity(),mData);
        RecyclerView.LayoutManager layoutManager =
                new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new SpaceDecoration(5,5));

        return view;
    }

    public void initData(){
        mData = new ArrayList<>();
        for (int i =0; i < 10;i++){
            mData.add("it's beautiful day");
        }
    }
}

package com.example.coustomtoolbar.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coustomtoolbar.Adapter.MyAdapter2;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.RecyclerViewUtil.LoadMode;
import com.example.coustomtoolbar.RecyclerViewUtil.LoadMoreScrollListener;
import com.example.coustomtoolbar.RecyclerViewUtil.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/6/23.
 */

public class Fragment2 extends Fragment {
    private static final String TAG = "Fragment1";
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
        final RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
        adapter = new MyAdapter2(getActivity(),mData);
        final RecyclerView.LayoutManager layoutManager =
                new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        //RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new SpaceDecoration(5,5));

        return view;
    }

    public void initData(){
        mData = new ArrayList<>();
        for (int i =0; i < 20;i++){
            mData.add("it's beautiful day");
        }
    }
}

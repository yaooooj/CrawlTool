package com.example.coustomtoolbar.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coustomtoolbar.Adapter.BaseAdapter;
import com.example.coustomtoolbar.Adapter.NormalAdapter;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.RecyclerViewUtil.LoadMode;
import com.example.coustomtoolbar.RecyclerViewUtil.LoadMoreScrollListener;
import com.example.coustomtoolbar.RecyclerViewUtil.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/6/23.
 */

public class Fragment3 extends Fragment {
    RecyclerView recyclerView;
    private List<String> stringList;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fragment_3,null);
        initData();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        //RecyclerView.LayoutManager layoutManager =
          //     new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        //RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL,false);
        //recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        //recyclerView.addItemDecoration(new SpaceDecoration(5,5));
        final NormalAdapter adapter = new NormalAdapter(getContext(),R.layout.fragment2_item_,stringList,recyclerView);
        adapter.setEmptyView(R.layout.empty_layout);
        adapter.setHeaderViewList(R.layout.footer_add_more);
        adapter.setFooterViewList(R.layout.footer_no_more_data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getContext(),"item click " + position,Toast.LENGTH_SHORT).show();
                adapter.updataData(position,"new data");
            }
        });
        adapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(),"item long click " + position,Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setLoadMode(LoadMode.PULLUP);
        adapter.setLoadMoreListener(new BaseAdapter.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                List<String> datas = new ArrayList<String>();
                for (int i=0;i<20;i++){
                    datas.add("New Data " + i);
                }
                adapter.addData(datas);
            }
        });
        return view;
    }
    public void initData(){
        if (stringList == null){
            stringList = new ArrayList<>();
        }
        for (int i = 0; i <= 20;i++){
           // stringList.add("hahha");
        }
    }
}

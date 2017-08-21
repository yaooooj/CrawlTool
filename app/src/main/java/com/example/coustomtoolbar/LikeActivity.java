package com.example.coustomtoolbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListAdapter;

import com.example.coustomtoolbar.Adapter.CollapsingAdapter;

import java.util.ArrayList;
import java.util.List;

public class LikeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CollapsingAdapter adapter;
    private List<String> strings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_toolbar_layout);
        init();
        recyclerView = (RecyclerView)findViewById(R.id.collapsing_recycler);
        adapter = new CollapsingAdapter(
                getApplicationContext(),R.layout.like_item_layout,strings,recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(
                this,LinearLayoutManager.VERTICAL,false
        ));
        recyclerView.setAdapter(adapter);
    }
    public void init(){
        strings = new ArrayList<>();
        for (int i=0 ; i < 20 ; i++){
            strings.add("A"  + i);
        }
    }
}

package com.example.coustomtoolbar.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coustomtoolbar.Adapter.MyAdapter;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.Util.SpaceDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yaojian on 2017/6/23.
 */

public class Fragment1 extends Fragment{
    private static final String TAG = "Fragment1";
    private List<String> mData;
    private MyAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private View view;
    private Response response;
    private static final String URL  = "http://ali-pic.showapi.com/852-1/24554039&a2e7ba852ad505736d69a6e05f49d1ed";
    private static final String URLTest = "https://api.github.com/gists/c2a7c39532239ff261be";
    private static final String APPCODE = "a2e7ba852ad505736d69a6e05f49d1ed";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.layout_fragment_1,container,false);
        initData();
        //RecyclerView

        initSwipeRefreshLayout();

        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
       adapter = new MyAdapter(getActivity(),mData);
        RecyclerView.LayoutManager layoutManager =
                new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new SpaceDecoration(16));

        return view;
    }

    public void initSwipeRefreshLayout(){
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();

                        Request request = new Request.Builder()
                                .url(URL)
                                .build();

                            Call call = client.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.e(TAG, "onFailure: "+ "failure execute  request" );
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response.isSuccessful()){
                                        Log.e(TAG, "run: "+ "successful" );

                                    }
                                }
                            });
                            refreshLayout.setRefreshing(false);
                        }


                       // mData.add("1");
                       // adapter.notifyDataSetChanged();

                     },1000);
            }
        });
    }

    public void initData(){
        mData = new ArrayList<>();
    }

}

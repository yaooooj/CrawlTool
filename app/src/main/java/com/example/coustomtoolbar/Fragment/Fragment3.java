package com.example.coustomtoolbar.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coustomtoolbar.Adapter.BaseAdapter;
import com.example.coustomtoolbar.Adapter.NormalAdapter;
import com.example.coustomtoolbar.Bean.PictureBean;
import com.example.coustomtoolbar.ImageCache.ImageUrl;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.RecyclerViewUtil.LoadMode;
import com.example.coustomtoolbar.NetUtil.OkHttp3Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/6/23.
 */

public class Fragment3 extends BaseFragment {
    private static final String TAG = "Fragment3";
    RecyclerView recyclerView;
    private List<String> stringList;
    private View view;
    private SwipeRefreshLayout refreshLayout;
    private NormalAdapter adapter;
    private OkHttp3Util okHttp3Util;
    private PictureBean pictureBean;

    private ImageUrl imageUrl;
    private List<String> urls;
    int count = 0;

    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "id";
    private String mParam1;
    private String mParam2;
    private static int page = 2;
    private static int type = 4001;

    private static final String APIKEY = "42731";
    private static final String APISECRET = "96039fbf84ee42afaad5d66f14159c31";
    private static String URL_PICTURE;
    private Handler handler3 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what ){
                case 2:
                    pictureBean = (PictureBean)msg.obj;
                    Log.e(TAG, "onResponse: "+  pictureBean.getShowapi_res_body().getPagebean().getAllNum() );
                    imageUrl = new ImageUrl(pictureBean);
                    urls = imageUrl.getBitmapList();
                    adapter.setFirstLoadImage(true);

                    updata();
                    break;
                default:
                    break;
            }

        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mParam1 = getArguments().getString(ARG_PARAM1);
        mParam2 = getArguments().getString(ARG_PARAM2);
        Log.e(TAG, "onCreate: "+ mParam2 +" "+ mParam1  );
        URL_PICTURE = "http://route.showapi.com/852-2?page="+ page +
                "&showapi_appid="+APIKEY+"&type="+mParam2+"&showapi_sign="+APISECRET;

        Log.e(TAG, "onCreate: " + mParam2 + " " + getUserVisibleHint() );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fragment_3,null);
        Log.e(TAG, "onCreateView: " + mParam2 + " " + getUserVisibleHint()  );

        initData();
        initSwipeRefreshLayout();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        //recyclerView.setLayoutManager(
       //         new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        adapter = new NormalAdapter(getActivity().getApplicationContext(),R.layout.fragment2_item_,urls,recyclerView);

        //adapter.setEmptyView(R.layout.empty_layout);
        adapter.setHeaderViewList(R.layout.footer_add_more);
        adapter.setFooterViewList(R.layout.footer_no_more_data);
        adapter.setLoadingView(R.layout.layout_loading);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getContext(),"item click " + position,Toast.LENGTH_SHORT).show();
                //adapter.updataData(position,"new data");
            }
        });
        adapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener() {
            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(),"item long click " + position,Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    public void initSwipeRefreshLayout(){
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //updata();
            }
        });
    }

    public void initData(){
        if (stringList == null){
            stringList = new ArrayList<>();
        }

    }

    private void updata(){
        if (urls != null){
            adapter.addData(urls);
        }
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    @Override
    public void onFragmentFirstVisible() {
        if (okHttp3Util == null){
            okHttp3Util = new OkHttp3Util(getContext());
        }
        okHttp3Util.executeGet(URL_PICTURE,handler3, PictureBean.class,2);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView: " );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " );
    }
}

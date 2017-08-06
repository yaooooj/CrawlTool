package com.example.coustomtoolbar.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.example.coustomtoolbar.ImageCache.ImageCache;
import com.example.coustomtoolbar.ImageCache.ImageUrl;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.RecyclerViewUtil.LoadMode;
import com.example.coustomtoolbar.RecyclerViewUtil.LoadMoreScrollListener;
import com.example.coustomtoolbar.RecyclerViewUtil.SpaceDecoration;
import com.example.coustomtoolbar.Util.OkHttp3Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/6/23.
 */

public class Fragment3 extends Fragment {
    private static final String TAG = "Fragment3";
    RecyclerView recyclerView;
    private List<String> stringList;
    private View view;
    private SwipeRefreshLayout refreshLayout;
    private NormalAdapter adapter;
    private OkHttp3Util okHttp3Util;
    private PictureBean pictureBean;
    private ImageCache imageCache;
    private ImageUrl imageUrl;
    private static int page = 2;
    private static int type = 4001;
    private static final String APIKEY = "42731";
    private static final String APISECRET = "96039fbf84ee42afaad5d66f14159c31";
    private static final String URL_PICTURE = "http://route.showapi.com/852-2?page="+ page + "&showapi_appid="+APIKEY+"&type="+type+"&showapi_sign="+APISECRET;
    Handler handler3 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what ){
                case 2:
                    pictureBean = (PictureBean)msg.obj;
                    Log.e(TAG, "onResponse: "+  pictureBean.getShowapi_res_body().getPagebean().getAllNum() );
                    imageUrl = new ImageUrl(pictureBean);
                    break;
                default:
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fragment_3,null);
        initData();
        initSwipeRefreshLayout();
        if (okHttp3Util == null){
            okHttp3Util = new OkHttp3Util();
        }
        okHttp3Util.executeGet(URL_PICTURE,handler3, PictureBean.class,2);


        recyclerView = (RecyclerView) view.findViewById(R.id.rv);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        recyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        adapter = new NormalAdapter(
                getContext(),R.layout.fragment2_item_,stringList,recyclerView);

        //adapter.setEmptyView(R.layout.empty_layout);
        adapter.setHeaderViewList(R.layout.footer_add_more);
        adapter.setFooterViewList(R.layout.footer_no_more_data);
        adapter.setLoadingView(R.layout.layout_loading);
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
                Log.e(TAG, "loadMore: "+ "have some move" );

            }
        });

        /*
        recyclerView.addOnScrollListener(new LoadMoreScrollListener(LoadMode.PULLUP) {
            @Override
            public void onLoadMore() {
                Log.e(TAG, "onLoadMore: " + "has more data" );
            }
        });
        */
        return view;
    }
    public void initSwipeRefreshLayout(){
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                imageUrl.praserUrl();
                updata(imageUrl.getBitmapList());
            }
        });
    }
    public void updata(List<String> bitmap1){
        refreshLayout.setRefreshing(false);
        if (bitmap1 != null){

            for (int i =0; i < 10;i++){
                adapter.addData(bitmap1.get(i));
                Log.e(TAG, "updata: " + bitmap1.get(i) );
            }

        }
    }
    public void initData(){
        if (stringList == null){
            stringList = new ArrayList<>();
        }
        imageCache = ImageCache.getInstance();
        imageCache.setMaxWidth(1080 / 3);
        for (int i = 0; i <= 10;i++){
            //stringList.add("hahha");
        }
    }
}

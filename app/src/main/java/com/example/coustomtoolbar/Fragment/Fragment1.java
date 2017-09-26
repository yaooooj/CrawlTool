package com.example.coustomtoolbar.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.coustomtoolbar.Bean.PictureBean;
import com.example.coustomtoolbar.ImageCache.ImageUrl;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.NetUtil.OkHttp3Util;
import com.example.coustomtoolbar.RecyclerViewUtil.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/6/23.
 */

public class Fragment1 extends Fragment{
    private static final String TAG = "Fragment1";
    private List<String> mData;
    private MyAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private View view;
    private OkHttp3Util okHttp3Util;
    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "id";
    private String mParam1;
    private String mParam2;
    private static final int page = 4;
    private static final String APIKEY = "42731";
    private static final String APISECRET = "96039fbf84ee42afaad5d66f14159c31";
    private final String URL_PICTURE = "http://route.showapi.com/852-2?page="+ page +
            "&showapi_appid="+APIKEY+"&type="+4001+"&showapi_sign="+APISECRET;

    private ImageUrl imageUrl;
    private Handler handler2 = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what ){
                case 1:
                    PictureBean pictureBean = (PictureBean)msg.obj;
                    Log.e(TAG, "onResponse: "+  pictureBean.getShowapi_res_body().getPagebean().getAllNum() );
                    imageUrl = new ImageUrl(pictureBean);
                    updata(imageUrl.getBitmapList());
                    break;
                default:
                    break;
            }
        }
    };

    public Fragment1() {
        //Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        mParam1 = getArguments().getString(ARG_PARAM1);
        mParam2 = getArguments().getString(ARG_PARAM2);
        Log.e(TAG, "onCreate: "+ mParam2 +" "+ mParam1  );
        String URL_PICTURE = "http://route.showapi.com/852-2?page="+ page +
                "&showapi_appid="+APIKEY+"&type="+mParam2+"&showapi_sign="+APISECRET;
                */
        if (okHttp3Util == null){
            okHttp3Util = new OkHttp3Util(getContext());
        }
        okHttp3Util.executeGet(URL_PICTURE,handler2, PictureBean.class,1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fragment_1,container,false);
        initData();
        //RecyclerView


        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
        //adapter = new MyAdapter(getContext(),mData);
        adapter = new MyAdapter(this,mData);
        rv.setLayoutManager(
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        );

        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new SpaceDecoration(5,5));
        initSwipeRefreshLayout();
        return view;
    }
    public void initSwipeRefreshLayout(){
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updata(imageUrl.getBitmapList());
            }
        });
    }

    public void initData(){
        mData = new ArrayList<>();

    }

    public void updata(List<String> bitmap1){

        if (bitmap1 != null){
            Log.e(TAG, "updata: " + bitmap1.size() );
            for (int i =0; i < 40;i++){
                Log.e(TAG, "updata: " + bitmap1.get(i) );
                adapter.addItem(bitmap1.get(i));
            }
        }
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

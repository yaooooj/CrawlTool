package com.example.coustomtoolbar.Fragment;

import android.graphics.Bitmap;
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
import com.example.coustomtoolbar.ImageCache.ImageCache;
import com.example.coustomtoolbar.ImageCache.ImageUrl;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.NetUtil.OkHttp3Util;
import com.example.coustomtoolbar.RecyclerViewUtil.SpaceDecoration;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

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
    private Gson gson;
    private OkHttp3Util okHttp3Util;
    private List<String> bitmapList;
    private Bitmap bitmap;
    private PictureBean pictureBean;
    private ImageCache imageCache;
    private static int page = 2;
    private static int type = 4001;
    private static final String APIKEY = "42731";
    private static final String APISECRET = "96039fbf84ee42afaad5d66f14159c31";
    private static final String URL_PICTURE = "http://route.showapi.com/852-2?page="+ page + "&showapi_appid="+APIKEY+"&type="+type+"&showapi_sign="+APISECRET;
    private static final String URLTest = "https://api.github.com/gists/c2a7c39532239ff261be";
    private static final String APPCODE = "a2e7ba852ad505736d69a6e05f49d1ed";
    private static final String URL  = "http://route.showapi.com/852-1?&showapi_appid="+APIKEY+"&showapi_sign="+APISECRET;
    private ImageUrl imageUrl;
    private Handler handler2 = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what ){
                case 1:
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
        view = inflater.inflate(R.layout.layout_fragment_1,container,false);
        initData();
        //RecyclerView
        if (okHttp3Util == null){
            okHttp3Util = new OkHttp3Util(getContext());
        }
        okHttp3Util.executeGet(URL_PICTURE,handler2, PictureBean.class,1);

        initSwipeRefreshLayout();
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
       adapter = new MyAdapter(getActivity(),mData);
        RecyclerView.LayoutManager layoutManager =
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new SpaceDecoration(5,5));


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

    public void initData(){
        mData = new ArrayList<>();
        gson = new Gson();
        bitmapList = new ArrayList<>();
        imageCache = ImageCache.getInstance(getContext());
        imageCache.setMaxWidth(1080 / 3);

    }

    public void updata(List<String> bitmap1){
        refreshLayout.setRefreshing(false);
        if (bitmap1 != null){
            Log.e(TAG, "updata: " + bitmap1.size() );
            for (int i =0; i < 40;i++){
                adapter.addItem(bitmap1.get(i));
            }

        }
    }




}

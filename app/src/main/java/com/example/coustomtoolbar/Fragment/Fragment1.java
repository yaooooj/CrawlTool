package com.example.coustomtoolbar.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.coustomtoolbar.Bean.PictureBody;
import com.example.coustomtoolbar.Bean.PictureCategory;
import com.example.coustomtoolbar.Bean.PictureContentList;
import com.example.coustomtoolbar.Bean.PictureList;
import com.example.coustomtoolbar.Bean.PicturePageBean;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.Util.OkHttp3Util;
import com.example.coustomtoolbar.Util.SpaceDecoration;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Response;

/**
 * Created by yaojian on 2017/6/23.
 */

public class Fragment1 extends Fragment{
    private static final String TAG = "Fragment1";
    private List<Bitmap> mData;
    private MyAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private View view;
    private Response response;
    private Gson gson;
    private OkHttp3Util okHttp3Util;
    private boolean success_code = false;
    private int error_code;
    private int all_page;
    private List<String> bitmapList;
    private Bitmap bitmap;
    private PictureBean pictureBean;
    private PictureBody pictureBody;
    private PicturePageBean picturePageBean;
    private PictureContentList pictureContentList;
    private PictureList pictureList;
    private static int page = 1;
    private static int type = 4001;
    private static final String URLTest = "https://api.github.com/gists/c2a7c39532239ff261be";
    private static final String APPCODE = "a2e7ba852ad505736d69a6e05f49d1ed";
    private static final String APIKEY = "42731";
    private static final String APISECRET = "96039fbf84ee42afaad5d66f14159c31";
    private static final String URL  = "http://route.showapi.com/852-1?&showapi_appid="+APIKEY+"&showapi_sign="+APISECRET;
    private static final String URL_PICTURE = "http://route.showapi.com/852-2?page="+ page + "&showapi_appid="+APIKEY+"&type="+type+"&showapi_sign="+APISECRET;
    private Handler handler2 = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            pictureBean = (PictureBean)msg.obj;
            Log.e(TAG, "onResponse: "+  pictureBean.getShowapi_res_body().getPagebean().getAllNum() );
            if (pictureBean != null) {
                if (pictureBean.getShowapi_res_body() != null) {
                    pictureBody = pictureBean.getShowapi_res_body();
                }
                if (pictureBody.getPagebean() != null) {
                    picturePageBean = pictureBody.getPagebean();
                }
                if (picturePageBean.getContentlist() != null) {
                    for (int i = 0; i < picturePageBean.getContentlist().size(); i++) {
                        pictureContentList = picturePageBean.getContentlist().get(i);
                        if (pictureContentList.getLists() != null) {
                            for (int j = 0; j < pictureContentList.getLists().size(); j++) {
                                pictureList = pictureContentList.getLists().get(j);
                               // Log.e(TAG, "run: " + pictureList.getSmall());
                                bitmapList.add(pictureContentList.getLists().get(0).getMiddle());
                            }
                        }
                    }
                }
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
        if (okHttp3Util == null){
            okHttp3Util = new OkHttp3Util();
        }
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        okHttp3Util.executeGet(URL_PICTURE,handler2,PictureBean.class);
                        if (success_code){
                            getBitMap(bitmapList.get(0));
                            Log.e(TAG, "getBitMap: " + bitmapList.get(0) );
                            updata();
                        }
                        }
                       // mData.add("1");
                       // adapter.notifyDataSetChanged();

                     },3000);
            }
        });
    }

    public void initData(){
        mData = new ArrayList<>();
        gson = new Gson();
        bitmapList = new ArrayList<>();

    }

    public void getBitMap(final String url){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL imageUrl = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) imageUrl.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(5000);
                    con.setDoInput(true);
                    con.connect();
                    InputStream in = con.getInputStream();

                    bitmap = BitmapFactory.decodeStream(in);
                    Log.e(TAG, "run: "+ bitmap.toString() );
                    in.close();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void updata(){
        refreshLayout.setRefreshing(false);
        if (bitmap != null){
            adapter.addItem(bitmap);
            setSuccess_code(false);
            Log.e(TAG, "getBitMap: " + "load image failed" );
        }
    }

    public static String getTAG() {
        return TAG;
    }

    public boolean isSuccess_code() {
        return success_code;
    }

    public void setSuccess_code(boolean success_code) {
        this.success_code = success_code;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public int getAll_page() {
        return all_page;
    }

    public void setAll_page(int all_page) {
        this.all_page = all_page;
    }
}

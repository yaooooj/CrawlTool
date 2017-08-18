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

import com.example.coustomtoolbar.Adapter.MyAdapter2;
import com.example.coustomtoolbar.Bean.PictureBean;
import com.example.coustomtoolbar.ImageCache.ImageUrl;
import com.example.coustomtoolbar.NetUtil.OkHttp3Util;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.RecyclerViewUtil.SpaceDecoration;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by yaojian on 2017/6/23.
 */

public class Fragment2 extends Fragment {
    private static final String TAG = "Fragment1";
    private View view;
    private List<Bitmap> mData;
    private MyAdapter2 adapter;
    private SwipeRefreshLayout refreshLayout;
    private ThreadPoolExecutor executor = null;
    private OkHttp3Util okHttp3Util;
    private PictureBean pictureBean;
    private ImageUrl imageUrl;
    private static int page = 2;
    private static int type = 4001;
    private static final String APIKEY = "42731";
    private static final String APISECRET = "96039fbf84ee42afaad5d66f14159c31";
    private static final String URL_PICTURE = "http://route.showapi.com/852-2?page="+ page +
            "&showapi_appid="+APIKEY+"&type="+type+"&showapi_sign="+APISECRET;

    private Handler handler3 = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
        okHttp3Util.executeGet(URL_PICTURE,handler3, PictureBean.class,1);

        final RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
        adapter = new MyAdapter2(getActivity(),mData);
        final RecyclerView.LayoutManager layoutManager =
                new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        //RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.addItemDecoration(new SpaceDecoration(5,5));

        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Future<Bitmap> bitmap = null;
                for (int i = 0 ;i < 10;i ++){
                    bitmap = executor.submit(new DownBitmap(imageUrl.getBitmapList().get(i)));
                    try {
                        Bitmap bitmap1 = bitmap.get();
                        if (bitmap1 != null){
                            adapter.updata(bitmap1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        return view;
    }

    public void initData(){
        mData = new ArrayList<>();
    }

    private class DownBitmap implements Callable<Bitmap>{
        String url;
        public DownBitmap(String url) {
            this.url = url;
        }
        @Override
        public Bitmap call() throws Exception {
            Bitmap bitmap = null;
            HttpURLConnection con = null;
            URL imageUrl = null;
            try {
                imageUrl = new URL(url);
                con = (HttpURLConnection) imageUrl.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setDoInput(true);
                con.connect();
                InputStream in = con.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }
    private class downloadBitmap implements Runnable {
        String url;
        public downloadBitmap(String url) {
            this.url = url;
        }
        @Override
        public void run() {
            Bitmap bitmap = null;
            HttpURLConnection con = null;
            URL imageUrl = null;
            try {
                imageUrl = new URL(url);
                con = (HttpURLConnection) imageUrl.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setDoInput(true);
                con.connect();
                InputStream in = con.getInputStream();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}

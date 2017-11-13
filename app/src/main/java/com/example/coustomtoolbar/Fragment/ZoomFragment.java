package com.example.coustomtoolbar.Fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.coustomtoolbar.CoustomView.FreshViewPager;
import com.example.coustomtoolbar.CoustomView.HeaderView;
import com.example.coustomtoolbar.CoustomView.OnPullListener;
import com.example.coustomtoolbar.ImageCache.GlideApp;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.Util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ZoomFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ZoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ZoomFragment extends BaseFragment{

    private static final String TAG = "ZoomFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int initPosition;
    private List<String> urls;

    private ScreenUtil screenUtil = new ScreenUtil();
    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private List<ImageView> mViews;

    public ZoomFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param data Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ZoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ZoomFragment newInstance(List<String> data, int param2) {
        ZoomFragment fragment = new ZoomFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("urls", (ArrayList<String>) data);
        args.putInt("position", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            initPosition = getArguments().getInt("position");
            if (getArguments().getStringArrayList("urls") != null){
                urls = getArguments().getStringArrayList("urls");
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zoom, container, false);
        //Window window = getActivity().getWindow();
       // getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
        ///window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.zoom_viewpager);
        mViews = new ArrayList<>();
        for (int i = 0;i < urls.size() - initPosition;i++){
            ImageView imageView = new ImageView(getActivity());
            mViews.add(imageView);
        }
        viewPager.setAdapter(new ZoomViewPagerAdapter(urls));


        FreshViewPager freshViewPager = (FreshViewPager) view.findViewById(R.id.zoom_frashviewpager);
        freshViewPager.addHeader(new HeaderView(getActivity()));
        freshViewPager.setOnPullListener(new OnPullListener() {
            @Override
            public boolean onRefresh() {
                SetWrapperFragment setWrapperFragment = SetWrapperFragment.newInstence("" + 123445,null);
                setWrapperFragment.show(getFragmentManager(),"dialog");
                return true;
            }

            @Override
            public boolean onLoadMore() {
                return false;
            }
        });


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView: " + "zoom fragment" );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " + "zoom fragment" );
    }


    private class ZoomViewPagerAdapter extends PagerAdapter{
        private List<String> urls;

        public ZoomViewPagerAdapter(List<String> urls) {
            this.urls = urls;
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewPager.LayoutParams.MATCH_PARENT,ViewPager.LayoutParams.MATCH_PARENT
            );
            ImageView imageView = mViews.get(position);
            imageView.setLayoutParams(params);
            if (position + initPosition >= urls.size()){
                Toast.makeText(getActivity(),"this is all",Toast.LENGTH_SHORT).show();
            }else {
                GlideApp.with(ZoomFragment.this)
                        .load(urls.get(position + initPosition))
                        .placeholder(R.mipmap.ic_favorite_border_black_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .dontAnimate()
                        .into(imageView);
                container.addView(imageView);
            }

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView imagview = mViews.get(position);

            container.removeView(imagview);
        }
    }





    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}

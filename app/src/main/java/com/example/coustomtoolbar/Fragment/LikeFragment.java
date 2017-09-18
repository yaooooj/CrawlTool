package com.example.coustomtoolbar.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.coustomtoolbar.Bean.PassCategory;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.Util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LikeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LikeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikeFragment extends Fragment {

    private static final String TAG = "LikeFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ScreenUtil screenUtil = new ScreenUtil();

    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private List<PassCategory> categories = null;

    public LikeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LikeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LikeFragment newInstance(String param1, String param2) {
        LikeFragment fragment = new LikeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categories = getArguments().getParcelableArrayList("category");
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mFragments = new ArrayList<>();
        if (categories != null){
            for (int i =0 ; i < categories.size(); i ++){
                mFragments.add(putArgumentsToFragment(
                        categories.get(i).getName(),categories.get(i).getId())
                );
            }
        }
    }

    public Fragment putArgumentsToFragment(String key,String id){
        Bundle bundleToFragment = new Bundle();
        //Fragment4 fragment = new Fragment4();
        //Fragment1 fragment = new Fragment1();
        Fragment3 fragment = new Fragment3();
        bundleToFragment.putString("name",key);
        bundleToFragment.putString("id",id);
        fragment.setArguments(bundleToFragment);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_like, container, false);
        screenUtil.setColor(Color.parseColor("#f19388"));
        screenUtil.StatusView(getActivity().getWindow());

        ImageView mImageView = (ImageView)view.findViewById(R.id.like_image_view);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mViewPager = (ViewPager) view.findViewById(R.id.like_viewpager);
        LikeViewPagerAdapter viewPagerAdapter = new LikeViewPagerAdapter(
                getActivity().getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(viewPagerAdapter);
        TabLayout mTab = (TabLayout)view.findViewById(R.id.like_tab_layout);
        mTab.setupWithViewPager(mViewPager);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: " );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.e(TAG, "onDestroyView: " );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }

    private class LikeViewPagerAdapter extends FragmentStatePagerAdapter{
        private List<Fragment> mFragments;
        public LikeViewPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return categories.get(position).getName();
        }
    }
}

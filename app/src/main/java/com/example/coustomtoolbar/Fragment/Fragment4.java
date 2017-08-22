package com.example.coustomtoolbar.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.coustomtoolbar.R;
import com.google.android.gms.plus.PlusOneButton;


public class Fragment4 extends Fragment {

    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "id";
    private String mParam1;
    private String mParam2;
    private  int page = 2;
    private  int type = 4001;
    private static final String APIKEY = "42731";
    private static final String APISECRET = "96039fbf84ee42afaad5d66f14159c31";
    private final String URL_PICTURE = "http://route.showapi.com/852-2?page="+ page +
            "&showapi_appid="+APIKEY+"&type="+type+"&showapi_sign="+APISECRET;
    public Fragment4() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plus_one, container, false);

        //Find the +1 button
       // mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}

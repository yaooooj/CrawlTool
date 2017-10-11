package com.example.coustomtoolbar.Fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coustomtoolbar.Adapter.BaseAdapter;
import com.example.coustomtoolbar.Adapter.SetWrapperAdapter;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.RecyclerViewUtil.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SEELE on 2017/10/9.
 */

public  class SetWrapperFragment extends DialogFragment {


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_wrapper_fragment_layout,container);
        ArrayList<String> data = new ArrayList<>();
        data.add("Set Wrapper");
        data.add("Set Lock Wrapper");
        data.add("Set Both");
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.wrapper_fragment_recyclerview);
        SetWrapperAdapter adapter = new SetWrapperAdapter(this,R.layout.set_wrapper_item_layout,data,recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpaceDecoration(getContext(),SpaceDecoration.VERTICAL_LIST));
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<String>() {
            @Override
            public void onClick(View view, List<String> url, int position) {
                Toast.makeText(getContext(),"this is "+ position,Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }



    public static SetWrapperFragment newInstence(int title, RecyclerView recyclerView){
        SetWrapperFragment setWrapperFragment = new SetWrapperFragment();
        Bundle args = new Bundle();
        args.putInt("title",title);
        args.putParcelable("recycler", (Parcelable) recyclerView);
        //mRecyclerView = recyclerView;
        setWrapperFragment.setArguments(args);
        return setWrapperFragment;
    }

/*
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");
        RecyclerView recyclerView = getArguments().getParcelable("recycler");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(" "  + title);
            builder.setView(recyclerView);
        return builder.create();
    }
    */
}

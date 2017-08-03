package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coustomtoolbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/8/1.
 */

public class NormalAdapter extends BaseAdapter<String,BaseViewHolder>{
    private static final String TAG = "NormalAdapter";

    public NormalAdapter(Context context, int layoutResId, List<String> data, RecyclerView recyclerView) {
        super(context, layoutResId, data, recyclerView);
    }


    @Override
    public void bindingItemView(final Context context, final BaseViewHolder holder, String s) {
        if (holder.getItemViewType() == ViewType.TYPE_EMPTY){
            holder.getView(R.id.image_empty).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"hhhhhhhhhhhh",Toast.LENGTH_SHORT).show();
                    List<String> datas = new ArrayList<String>();
                    for (int i=0;i<20;i++){
                        datas.add("New Data " + i);
                    }
                    addData(datas);
                }
            });
        }else {
            holder.setTextView(R.id.fragment2,s);
        }
    }
}

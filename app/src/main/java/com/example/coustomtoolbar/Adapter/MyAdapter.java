package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coustomtoolbar.Bean.CardViewBean;
import com.example.coustomtoolbar.Bean.TaskBean;
import com.example.coustomtoolbar.Fragment.Fragment1;
import com.example.coustomtoolbar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/6/29.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private int itemCount = 0;
    private List<TaskBean> datas;
    private TaskBean taskBean;
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_FOOTER = 2;
    private int mHeaderCount = 1;
    private int mFooterCount = 1;


    public MyAdapter(Context context, List<TaskBean> data) {
        this.context = context;
        datas = data;
    }
    public void addItem(int position){
        if (datas == null ){
            datas = new ArrayList<>();
        }
        taskBean = new TaskBean() ;
        taskBean.setTask_num(position);
        taskBean.setTask_name(" New Task Name Is " + itemCount);
        datas.add(taskBean);
        notifyItemInserted(position);
        itemCount++;
    }
    public void removeItem(int position){
        if (datas == null || datas.isEmpty()){
            return;
        }
        notifyItemRemoved(position);
        itemCount--;

    }

    public int getContentItemCount(){
        return datas.size();
    }

    public boolean isHeaderView(int position){
        return mHeaderCount != 0 && position < mHeaderCount;
    }

    public boolean isFooterView(int position){
        return mFooterCount != 0 && position < mFooterCount;
    }

    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();

        if (mHeaderCount != 0 && position < mHeaderCount){
            return ITEM_TYPE_HEADER;
        }
        else if (mFooterCount != 0 && position >= (mHeaderCount + dataItemCount)){
            return ITEM_TYPE_FOOTER;
        }
        else {
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM_TYPE_HEADER){
            return  new MyAdapter.MyHeaderViewHolder(
                    inflater.inflate(R.layout.layout_recycler_first_item,parent,false));
        }else if (viewType == ITEM_TYPE_FOOTER){
            return new MyAdapter.MyFooterVIewHolder(
                    inflater.inflate(R.layout.layout_recycler_first_item,parent,false)
            );
        }else if (viewType == ITEM_TYPE_CONTENT){
            return new MyAdapter.MyViewHolder(
                    inflater.inflate(R.layout.layout_recycler_item,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyAdapter.MyHeaderViewHolder){
            ((MyAdapter.MyHeaderViewHolder) holder).button_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context,"add_item",Toast.LENGTH_SHORT).show();
                }
            });
            ((MyAdapter.MyHeaderViewHolder) holder).button_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"remove_item",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (holder instanceof MyAdapter.MyFooterVIewHolder){

        } else if (holder instanceof MyAdapter.MyViewHolder){
            ((MyAdapter.MyViewHolder) holder).textView.setText(datas.get(position).getTask_name());
            ((MyAdapter.MyViewHolder) holder).textTaskNumber.setText(position+1+"");
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



    class MyHeaderViewHolder extends RecyclerView.ViewHolder{
        Button button_add;
        Button button_remove;
        public MyHeaderViewHolder(View itemView) {
            super(itemView);
            button_add = (Button)itemView.findViewById(R.id.add);
            button_remove = (Button)itemView.findViewById(R.id.remove);
        }
    }

    class MyFooterVIewHolder extends RecyclerView.ViewHolder{

        public MyFooterVIewHolder(View itemView) {
            super(itemView);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView  textTaskNumber;
        MyViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView)itemView.findViewById(R.id.task_name);
            textTaskNumber = (TextView) itemView.findViewById(R.id.task_number);
        }
    }
}
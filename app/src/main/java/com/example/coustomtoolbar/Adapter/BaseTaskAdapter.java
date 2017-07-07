package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coustomtoolbar.Bean.TaskBean;
import com.example.coustomtoolbar.Bean.TaskModel;
import com.example.coustomtoolbar.DataBaseUtil.DBManager;
import com.example.coustomtoolbar.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by yaojian on 2017/7/5.
 */

public class BaseTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{
    public static final String TAG = "BaseTaskAdapter";
    private Context context;
    private List<TaskModel> datas;
    private int itemCount = 0;
    private RecyclerView recycler;
    private OnBaseItemClickListener onBaseItemClickListener;
    public static final int HEADER_VIEW = 0;
    public static final int CONTENT_VIEW = 1;
    private int MyHeaderCount = 1;
    private View headerView;
    private CheckBox checkBox;
    private SparseArray<CheckBox> mCheckList;
    private List<Integer> mPositionList;
    private DBManager dbManager;

    public BaseTaskAdapter(Context context, List<TaskModel> data,RecyclerView recycler) {
        this.context = context;
        this.recycler = recycler;
        datas = data;
        mCheckList = new SparseArray<>();
        mPositionList = new ArrayList<>();
        dbManager = DBManager.Instence(context);
    }
    public interface OnBaseItemClickListener{
        void itemClick(View v,int position);
        void itemLOngClick(View v,int position);
    }

    public void setOnBaseItemClickListener(OnBaseItemClickListener onBaseItemClickListener){
        this.onBaseItemClickListener = onBaseItemClickListener;
    }


    public void removeItem(List<Integer> position){
        if (datas == null || datas.isEmpty()){
            return;
        }
        for (int i =0; i < position.size();i++){

            datas.remove(i);
            dbManager.delete(i);
        }


        datas = dbManager.queryWithSQL();
        notifyDataSetChanged();

    }

    public void setHeaderView(View view){
        if (view != null){
            headerView = view;
        }
    }

    //获取ItemView中的CheckBox列表
   public SparseArray getCheckList(){
       return mCheckList;
   }

    public void setPositionToList(int position) {

        mPositionList.add(position);
        Log.e(TAG, "setPositionToList: "+ position );
    }

    public void removePositionFromList(){

        mPositionList.remove( mPositionList.size() -1);
    }


    @Override
    public int getItemViewType(int position) {
        //int dataItemCount = getContentItemCount();
        if (MyHeaderCount !=0 && position < MyHeaderCount){
            return HEADER_VIEW;
        }else {
            return CONTENT_VIEW;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0){
            return  new BaseHeaderViewHolder(headerView);
        }else {
            return new ReViewHolder(LayoutInflater.from(context).inflate(R.layout.a,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ReViewHolder){

            ((ReViewHolder) holder).task_num.setText(datas.get(position).getCreator());
            ((ReViewHolder) holder).task_name.setText(datas.get(position).getTask_name());
            ((ReViewHolder) holder).task_num.setText(""+position);

            //将CHECKBOX和位置传递出去
            mCheckList.put(position,((ReViewHolder) holder).checkBox);

            ((ReViewHolder) holder).checkBox.setVisibility(View.GONE);
            ((ReViewHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        setPositionToList(holder.getAdapterPosition());
                    }else {
                        removePositionFromList();
                    }
                }
            });
            ((ReViewHolder) holder).upload.setVisibility(View.GONE);

            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);
            holder.itemView.setTag(holder.getAdapterPosition());
        }
       else if (holder instanceof BaseHeaderViewHolder){

        }
    }

    @Override
    public void onClick(View v) {
        if (onBaseItemClickListener != null){

            onBaseItemClickListener.itemClick(v,(int)v.getTag());

            //onBaseItemClickListener.itemLOngClick(v,(int)v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (onBaseItemClickListener != null){
            onBaseItemClickListener.itemLOngClick(v,(int)v.getTag());
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



    private class ReViewHolder extends RecyclerView.ViewHolder{
        TextView task_num;
        TextView task_name;
        TextView operator;
        TextView creator;
        TextView end_time;
        CheckBox checkBox;
        TextView upload;

        ReViewHolder(View itemView) {
            super(itemView);
            task_num = (TextView) itemView.findViewById(R.id.task_number);
            task_name = (TextView) itemView.findViewById(R.id.task_name);
            operator  = (TextView)itemView.findViewById(R.id.operator);
            creator = (TextView)itemView.findViewById(R.id.creator);
            end_time = (TextView)itemView.findViewById(R.id.end_time);
             checkBox = (CheckBox) itemView.findViewById(R.id.radio_button);
            upload = (TextView)itemView.findViewById(R.id.is_upload);
        }
    }
    private class BaseHeaderViewHolder extends RecyclerView.ViewHolder{
        Button delete_button;
        TextView checkBox_textView;
        CheckBox checkBox;
        public BaseHeaderViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox)itemView.findViewById(R.id.base_check_box);
            checkBox_textView = (TextView)itemView.findViewById(R.id.base_checkbox_text_view);
            delete_button = (Button)itemView.findViewById(R.id.base_delete_item);
        }
    }
}
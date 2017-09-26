package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coustomtoolbar.Bean.TaskModel;
import com.example.coustomtoolbar.DataBaseUtil.DBManager;
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.Util.SystemTime;

import java.util.ArrayList;
import java.util.List;

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
    private static final int HEADER_VIEW = 0;
    private static final int CONTENT_VIEW = 1;
    private static final int FOOTER_VIEW = 2;
    private static final int MyHeaderCount = 1;
    private static final int MyFooterCount = 1;
    private static final int ADD_MORE_DATA = 3;
    private static final int ADD_ERROR = 4;
    private static final int ADD_NO_MORE = 5;
    private int  status = 5;
    private Load load;
    private int mPageSize = 10;
    private int mPagePosition = 0;
    private View headerView;
    private CheckBox checkBox;
    private SystemTime systemTime;


    // 选中的checkbox位置列表
    private List<Integer> mPositionList;

    private DBManager dbManager;
    private boolean isVisible = false;

    public BaseTaskAdapter(Context context, List<TaskModel> data,RecyclerView recycler) {
        this.context = context;
        this.recycler = recycler;
        datas = data;
        mPositionList = new ArrayList<>();
        dbManager = DBManager.Instence(context);
        systemTime = SystemTime.getInstance();
    }
    public interface OnBaseItemClickListener{
        void itemClick(View v,int position);
        void itemLOngClick(View v,int position);
    }

    public interface Load{
        void load(int pagePosition,int pageSize,ILoadCallBack callback);
    }
    public interface ILoadCallBack{
        void onSuccess();

        void onFailure();
    }

    public void setOnBaseItemClickListener(OnBaseItemClickListener onBaseItemClickListener){
        this.onBaseItemClickListener = onBaseItemClickListener;
    }
    public void requestData(int pagePosition,int pageSize){
        if (load != null){
            load.load(pagePosition, pageSize, new ILoadCallBack() {
                @Override
                public void onSuccess() {
                    notifyDataSetChanged();
                    mPagePosition = (mPagePosition + 1) * mPageSize;
                    status = ADD_MORE_DATA;
                }

                @Override
                public void onFailure() {
                    status = ADD_ERROR;
                }
            });
        }
    }

    public void updata(){
        datas = dbManager.queryWithSQL();
        String time = systemTime.getTimeWithFormat();
        for (int i = 0;i < datas.size();i++ ){
            TaskModel taskModel = datas.get(i);
            if (taskModel.getEnd_time().compareTo(time) < 0 ){
                dbManager.delete(datas.get(i).getTask_name());
                datas.remove(i);
                notifyDataSetChanged();
            }
        }
        notifyDataSetChanged();

    }
    public void removeItem(){
        if (datas == null || datas.isEmpty()){
            Toast.makeText(context,"no data",Toast.LENGTH_SHORT).show();
        }else {
            if (mPositionList != null){
                for (int i =0; i < mPositionList.size() ;i++){

                    //删除数据库中的数据
                    String task_name = datas.get(mPositionList.get(i)).getTask_name();

                    //删除显示列表中的数据
                    datas.remove((int)mPositionList.get(i));

                    Log.e(TAG, "removeItem: "+task_name );
                    dbManager.delete(task_name);

                }
                datas = dbManager.queryWithSQL();
                notifyDataSetChanged();
            }

        }
    }

    public void setHeaderView(View view){
        if (view != null){
            headerView = view;
        }
    }

    //获取ItemView中的CheckBox列表

    //获取选中的checkbox
    private void setPositionToList(int position) {

        mPositionList.add(position);

    }
    //移除取消选中的checkbox
    private void removePositionFromList(){

        mPositionList.remove( mPositionList.size() -1);

    }



    public void setCheckVisible(boolean isVisible){
        this.isVisible = isVisible;
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        //int dataItemCount = getContentItemCount();
        if (  position < MyHeaderCount){
            return HEADER_VIEW;
        }
        else if (position >=  getItemCount() - 1 ){
            if (status == ADD_MORE_DATA){
               return ADD_MORE_DATA;
            }else if (status == ADD_ERROR){
                return ADD_ERROR;
            }else {
                return ADD_NO_MORE;
            }
        }
        else {
            return CONTENT_VIEW;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW){
            return  new BaseHeaderViewHolder(headerView);
        }else if (viewType == ADD_MORE_DATA ){
            return new FooterAddMoreViewHolder(LayoutInflater.from(context).inflate(R.layout.footer_add_more ,parent,false));
        }else if (viewType == ADD_ERROR){
            return new FooterErrorViewHolder(LayoutInflater.from(context).inflate(R.layout.footer_error,parent,false));
        }else if (viewType == ADD_NO_MORE){
            return new FooterAddNoMoreViewHolder(LayoutInflater.from(context).inflate(R.layout.footer_no_more_data,parent,false));
        }
        else {
            return new ReViewHolder(LayoutInflater.from(context).inflate(R.layout.a,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof BaseHeaderViewHolder && position == 0){
            ((BaseHeaderViewHolder) holder).textView.setText("Total:"+datas.size());
        }
        else if (holder instanceof FooterAddMoreViewHolder && position == getItemCount() -1 ){

        }
        else if (holder instanceof FooterAddNoMoreViewHolder && position == getItemCount() -1){

        }
        else if (holder instanceof FooterErrorViewHolder && position == getItemCount() -1){

        }
        else if (holder instanceof ReViewHolder){
           int realPosition =datas.size() - position;
           ((ReViewHolder) holder).task_num.setText(datas.get(realPosition).getCreator());
           ((ReViewHolder) holder).task_name.setText(datas.get(realPosition).getTask_name());
           ((ReViewHolder) holder).task_num.setText(""+position);
           ((ReViewHolder) holder).operator.setText("Operator: "+datas.get(realPosition).getOperator());
           ((ReViewHolder) holder).creator.setText("Creator: "+datas.get(realPosition).getCreator());
           ((ReViewHolder) holder).end_time.setText("End_time: "+datas.get(realPosition).getEnd_time());
           if (isVisible){
               ((ReViewHolder) holder).checkBox.setVisibility(View.VISIBLE);
           }else {
               ((ReViewHolder) holder).checkBox.setVisibility(View.GONE);
               ((ReViewHolder) holder).checkBox.setChecked(false);
           }

           ((ReViewHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if (isChecked){
                       setPositionToList(datas.size() - holder.getAdapterPosition());
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
        return MyHeaderCount + datas.size() + MyFooterCount;
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
        TextView textView;

        private BaseHeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.base_checkbox_text_view);
            delete_button = (Button)itemView.findViewById(R.id.base_delete_item);
        }
    }
    private class FooterAddMoreViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        private FooterAddMoreViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.add_more_data);
        }
    }
    private class FooterErrorViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        private FooterErrorViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.add_error);
        }
    }
    private class FooterAddNoMoreViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        private FooterAddNoMoreViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.no_more_data);
        }
    }
}
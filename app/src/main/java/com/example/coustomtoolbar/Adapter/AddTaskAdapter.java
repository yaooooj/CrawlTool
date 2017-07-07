package com.example.coustomtoolbar.Adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.bigkoo.pickerview.TimePickerView;
import com.example.coustomtoolbar.Bean.CardViewBean;
import com.example.coustomtoolbar.Bean.TaskModel;
import com.example.coustomtoolbar.Observer.Subject;
import com.example.coustomtoolbar.Observer.TaskObserver;
import com.example.coustomtoolbar.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yaojian on 2017/7/3.
 */

public class AddTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context context;
    private List<CardViewBean> datas;
    private int itemCount = 0;
    private CardViewBean cardViewBean;
    private TimePickerView pvTime;
    private int mPosition;
    private OnItemClickListener onItemClickListener;
    private TaskModel taskModel;
    private List<TaskModel> mListTask;
    private String TAG = "AddTaskAdapter";
    private List<String> mListName = new ArrayList<>();;
    public static final int ORDINAL_TYPE = 1;
    public static final int DATA_TYPE = 2;


    public interface OnItemClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick();
    }
    public AddTaskAdapter(Context context,List<CardViewBean> data) {
        this.context = context;
        datas = data;
        mListTask = new ArrayList<>();
        taskModel = new TaskModel();

    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setTimePicker(TimePickerView pvTime){
        this.pvTime = pvTime;
    }

    public void updata(String data){

        datas.get(getPosition()).setEditName(data);
        //notifyItemInserted(3);
        notifyItemChanged(getPosition());
    }
    public void setPosition(int position){
        this.mPosition = position;
    }
    public int  getPosition() {
        return mPosition;
    }

    public void isFocus(EditText editText){
        if (!editText.isFocused()){
            editText.requestFocus();
            editText.setCursorVisible(true);
        }else {
            if (editText.isFocused()){
                editText.setCursorVisible(false);
            }
        }
    }

    public List<String> getEditText(){
        //setTaskModel(editText.getText().toString(),position);
        for (int i = 0;i < 5;i++){
            Log.e(TAG, "getEditText: "+mListName.get(i));
        }
        return mListName;
    }

    public void setDataAfterChang(String text,int type,int position){
        //mListName.set(position,text);
        if (type == ORDINAL_TYPE){
            mListName.set(position,text);
        }if (type == DATA_TYPE){
            mListName.set(getPosition(),text);
        }

    }

    public void setEditText(String text,int type,int position){
        if (type == ORDINAL_TYPE){
            mListName.add(text);
        }
        if (type == DATA_TYPE){
            mListName.add(text);
        }

    }

    @Override
    public int getItemViewType(int position) {

        return position > 2  ? DATA_TYPE : ORDINAL_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ORDINAL_TYPE){
            return new AddTaskViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.recycler_add_task_item,parent,false));
        }else if (viewType == DATA_TYPE){
            return  new DataViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.recycler_data_item,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddTaskViewHolder){
            ((AddTaskViewHolder) holder).textView1.setText(datas.get(position).getTextName());
            ((AddTaskViewHolder) holder).editView1.setText(datas.get(position).getEditName());

            String text = ((AddTaskViewHolder) holder).editView1.getText().toString();
            setEditText(text,ORDINAL_TYPE,holder.getAdapterPosition());

            ((AddTaskViewHolder) holder).editView1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    InputMethodManager imm =
                            (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (hasFocus){
                        setPosition(holder.getAdapterPosition());
                        //Toast.makeText(context,""+holder.getAdapterPosition(),Toast.LENGTH_SHORT).show();

                    }else {
                        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                        String text = ((AddTaskViewHolder) holder).editView1.getText().toString();
                        //setTaskModel(text,holder.getAdapterPosition());
                        setDataAfterChang(text,ORDINAL_TYPE,holder.getAdapterPosition());
                    }
                }
            });
            ((AddTaskViewHolder) holder).editView1.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        setPosition(holder.getAdapterPosition());
                        switch (holder.getAdapterPosition()){
                            case 0:
                                if (holder.getAdapterPosition() == 1){
                                    isFocus(((AddTaskViewHolder) holder).editView1);
                                }
                                break;
                            case 1:
                                if (holder.getAdapterPosition() == 1){
                                    isFocus(((AddTaskViewHolder) holder).editView1);
                                }
                                break;
                            case 2:
                                if (holder.getAdapterPosition() == 1){
                                    isFocus(((AddTaskViewHolder) holder).editView1);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    return false;
                }
            });
            holder.itemView.setOnClickListener(this);
            holder.itemView.setTag(holder.getAdapterPosition());
        }
        else if (holder instanceof DataViewHolder){
            ((DataViewHolder) holder).textView2.setText(datas.get(position).getTextName());
            ((DataViewHolder) holder).editView2.setText(datas.get(position).getEditName());

            final String text = ((DataViewHolder) holder).editView2.getText().toString();
            setEditText(text,DATA_TYPE,holder.getAdapterPosition());

            ((DataViewHolder) holder).button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pvTime.setDate(Calendar.getInstance());
                    pvTime.show();
                    setPosition(holder.getAdapterPosition());
                    //setDataAfterChang(text,DATA_TYPE,holder.getAdapterPosition());
                    //Toast.makeText(context,"pick data",Toast.LENGTH_SHORT).show();
                }
            });
            holder.itemView.setOnClickListener(this);
            holder.itemView.setTag(holder.getAdapterPosition());
        }
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null){
            onItemClickListener.onItemClick(v,(Integer) v.getTag());
            Toast.makeText(context,""+v.getTag(),Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public int getItemCount() {
        return datas.size();
    }

    class AddTaskViewHolder extends RecyclerView.ViewHolder{
        EditText editView1;
        TextView textView1;

        public AddTaskViewHolder(View itemView) {
            super(itemView);
            editView1 = (EditText) itemView.findViewById(R.id.task_name_edit);
            textView1 = (TextView) itemView.findViewById(R.id.task_name_text);
        }
    }
    class DataViewHolder extends RecyclerView.ViewHolder{
        TextView editView2;
        TextView textView2;
        ImageButton button2;
        public DataViewHolder(View itemView) {
            super(itemView);
            editView2 = (TextView) itemView.findViewById(R.id.task_name_edit);
            textView2 = (TextView) itemView.findViewById(R.id.task_name_text);
            button2 = (ImageButton)itemView.findViewById(R.id.data_packer);
        }
    }
}

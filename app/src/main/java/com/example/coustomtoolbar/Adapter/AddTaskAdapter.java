package com.example.coustomtoolbar.Adapter;

import android.content.Context;
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
import com.example.coustomtoolbar.R;
import com.example.coustomtoolbar.Util.SystemTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yaojian on 2017/7/3.
 */

public class AddTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{
    private Context context;
    private int itemCount = 0;
    private TimePickerView pvTime;
    private int mPosition;
    private OnItemClickListener onItemClickListener;
    private TaskModel taskModel;
    private List<TaskModel> mListTask;
    private List<CardViewBean> datas;
    private String TAG = "AddTaskAdapter";
    private List<String> mListName = new ArrayList<>();
    private String text;
    private String startTime;
    private String endTime;
    private SystemTime systemTime;
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
    //获取data picker 选择的日期
    public void setTimePicker(TimePickerView pvTime){
        this.pvTime = pvTime;
    }

    //选择日期后，更新列表中显示的日期
    public void updata(String data){

        if (getTextView().compareTo("StartTime") == 0){
            Log.e(TAG, "updata: "+1 );
            startTime = data;
            datas.get(getPosition()).setEditName(data);
            notifyItemChanged(getPosition());
        }else {
            if (startTime == null){
                systemTime = SystemTime.getInstance();
                startTime = systemTime.getTimeWithFormat();
            }
            if (startTime.compareTo(data) < 0){
                datas.get(getPosition()).setEditName(data);
                notifyItemChanged(getPosition());
            }else {
                Toast.makeText(context,"endTime more than startTime",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void setPosition(int position){
        this.mPosition = position;
    }
    private int  getPosition() {
        return mPosition;
    }
    private void setTextView(String textView){
        this.text = textView;
        Log.e(TAG, "setTextView: " + textView );
    }
    public String getTextView(){
        return text;
    }

    private void isFocus(EditText editText){
        if (!editText.isFocused()){
            //请求获取焦点
            editText.requestFocus();
            //隐藏输入栏中的光标
            editText.setCursorVisible(true);
        }else {
            if (editText.isFocused()){
                editText.setCursorVisible(false);
            }
        }
    }
    //返回edit view中的内容
    public List<String> getEditText(){
        //setTaskModel(editText.getText().toString(),position);
        //for (int i = 0;i < 5;i++){
        //    Log.e(TAG, "getEditText: "+mListName.get(i));
       // }
        return mListName;
    }
    //如果改变了edit view中的内容，则需要将改变后的内容存入列表中
    public void setDataAfterChang(String text,int type,int position){

        //mListName.set(position,text);
        if (type == ORDINAL_TYPE){
            mListName.set(position,text);
        }if (type == DATA_TYPE){
            mListName.set(getPosition(),text);
        }

    }
    //如果edit view 中的内容没有改变，则将初始化设置的内容存入列表中
    private void setEditText(String text,int type,int position){
        //如果是操作任务名或操作人的字符，就加入列表中
        if (type == ORDINAL_TYPE){
            mListName.add(text);
        }
        //如果是日期，加入列表中
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AddTaskViewHolder){
            ((AddTaskViewHolder) holder).textView1.setText(datas.get(position).getTextName());
            ((AddTaskViewHolder) holder).editView1.setText(datas.get(position).getEditName());
            //recycler 初始化的时候，会将所有的edit view 设置初值，这时可以一次性获取所有输入栏中的值
            //并将他们存入一个数组中；档列表中的输入栏被改变以后，或重新设置一遍输入栏中的值，一样可以获取
            //全部输入栏中的值
            String text = ((AddTaskViewHolder) holder).editView1.getText().toString();
            setEditText(text,ORDINAL_TYPE,holder.getAdapterPosition());

            //档输入栏失去焦点后，将改变的值，设置给text view显示
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

            //设置当输入栏失去焦点后，不在显示光标
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
            //给输入栏设置监听接口
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
                    setTextView(((DataViewHolder) holder).textView2.getText().toString());
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

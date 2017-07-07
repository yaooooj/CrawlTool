package com.example.coustomtoolbar.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yaojian on 2017/7/4.
 */

public class TaskModel implements Parcelable {
    private  int task_num;
    private  String task_name;
    private  String operator;
    private String creator;
    private  String start_time;
    private String end_time;
    private  int task_count;
    private String create_time;

    //Constructor
    public TaskModel(){

    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    public int getTask_num() {
        return task_num;
    }

    public void setTask_num(int task_num) {
        this.task_num = task_num;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getTask_count() {
        return task_count;
    }

    public void setTask_count(int task_count) {
        this.task_count = task_count;
    }

    public static final Creator<TaskModel> CREATOR = new Creator<TaskModel>() {
        @Override
        public TaskModel createFromParcel(Parcel in) {
            TaskModel taskModel = new TaskModel();
            taskModel.task_num = in.readInt();
            taskModel.task_name = in.readString();
            taskModel.operator = in.readString();
            taskModel.start_time = in.readString();
            taskModel.end_time = in.readString();
            taskModel.task_count = in.readInt();
            taskModel.creator = in.readString();
            taskModel.create_time = in.readString();
            return taskModel;
        }

        @Override
        public TaskModel[] newArray(int size) {

            return new TaskModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(task_num);
        dest.writeString(task_name);
        dest.writeString(operator);
        dest.writeString(start_time);
        dest.writeString(end_time);
        dest.writeInt(task_count);
        dest.writeString(creator);
        dest.writeString(create_time);
    }
}

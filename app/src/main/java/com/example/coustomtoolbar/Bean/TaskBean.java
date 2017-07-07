package com.example.coustomtoolbar.Bean;

/**
 * Created by yaojian on 2017/6/29.
 */

public class TaskBean {
    private  int task_num;
    private  String task_name;
    private  String operator;
    private  String time;
    private  int task_count;

    public TaskBean() {

    }

    public void setTask_count(int task_count) {
        this.task_count = task_count;
    }

    public int getTask_count() {
        return task_count;
    }

    public void setTask_num(int task_num) {
        this.task_num = task_num;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTask_num() {
        return task_num;
    }

    public String getTask_name() {
        return task_name;
    }

    public String getOperator() {
        return operator;
    }

    public String getTime() {
        return time;
    }
}

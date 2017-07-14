package com.example.coustomtoolbar.DataBaseUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.coustomtoolbar.Bean.TaskBean;
import com.example.coustomtoolbar.Bean.TaskModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaojian on 2017/6/30.
 */

public class DBManager {
    private static final String TAG = "DBManager";
    private static DBManager dbManager;
    private Context context;
    private List<TaskModel> mTaskModelList;

    private DBManager(Context context){
        this.context = context;
    }

    public static DBManager Instence(Context context){
        if (dbManager == null){
            dbManager = new DBManager(context);
        }
        return dbManager;
    }

    public void  addWithSQL (List<TaskModel> taskModels){
        SQLiteDatabase db = new SQLiteDbHelper(context).getWritableDatabase();
        db.beginTransaction();
        try {
            for (TaskModel taskModel: taskModels){
                db.execSQL("INSERT INTO "+SQLiteDbHelper.TABLE_TASk +" VALUES(null,?,?,?,?,?,?,?,?)",new Object[]{
                        taskModel.getTask_num(),
                        taskModel.getTask_name(),
                        taskModel.getOperator(),
                        taskModel.getStart_time(),
                        taskModel.getEnd_time(),
                        taskModel.getTask_count(),
                        taskModel.getCreator(),
                        taskModel.getCreate_time()});
            }
            Log.e(TAG, "addWithSQL: " +"add data success" );
            db.setTransactionSuccessful();

        }finally {
            db.endTransaction();
        }
    }
    public List<TaskModel> queryWithSQL(){
        SQLiteDatabase db = new SQLiteDbHelper(context).getReadableDatabase();
        TaskModel ta ;
        List<TaskModel> mTaskModelList = new ArrayList<>();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("select distinct * from Task",null);
            while (cursor.moveToNext()){
                ta = new TaskModel();
                ta.setTask_num(cursor.getInt(cursor.getColumnIndex("task_num")));
                ta.setTask_name(cursor.getString(cursor.getColumnIndex("task_name")));
                ta.setOperator(cursor.getString(cursor.getColumnIndex("operator")));
                ta.setStart_time(cursor.getString(cursor.getColumnIndex("start_time")));
                ta.setEnd_time(cursor.getString(cursor.getColumnIndex("end_time")));
                ta.setTask_count(cursor.getInt(cursor.getColumnIndex("task_count")));
                ta.setCreator(cursor.getString(cursor.getColumnIndex("creator")));
                ta.setCreate_time(cursor.getString(cursor.getColumnIndex("create_time")));
                mTaskModelList.add(ta);
            }

            db.setTransactionSuccessful();
            cursor.close();
            Log.e(TAG, "queryWithSQL: "+"query data success" );

        }finally {
            db.endTransaction();
        }
        return mTaskModelList;
    }


    public void delete(String task_name){
        SQLiteDatabase db = new SQLiteDbHelper(context).getReadableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("delete from Task where task_name = ?",new Object[] {task_name});
            db.setTransactionSuccessful();
            Log.e(TAG, "delete: "+ task_name +"success" );
        }finally {
            db.endTransaction();
        }
    }
    public void deleteAll(){
        SQLiteDatabase db = new SQLiteDbHelper(context).getReadableDatabase();
        db.beginTransaction();
        try {
            db.execSQL("delete * from Task");
            db.setTransactionSuccessful();
            Log.e(TAG, "deleteAll: "+ "delete success" );
        }finally {
            db.endTransaction();
        }
    }



}























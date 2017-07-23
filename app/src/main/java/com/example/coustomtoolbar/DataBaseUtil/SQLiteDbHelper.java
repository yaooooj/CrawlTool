package com.example.coustomtoolbar.DataBaseUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

/**
 * Created by yaojian on 2017/6/29.
 */

public class SQLiteDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "task.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_TASk = "Task";
    public static final String TABLE_ALL_CATEGORY = "AllCategory";
    public static final String TABLE_CONCRETE_CATEGORY = "ConcreteCategory";

    private static final String TASK_CREATE_TABLE_SQL = "CREATE TABLE "
            + TABLE_TASk +"("
            + "id integer PRIMARY KEY AUTOINCREMENT,"
            + "task_num integer,"
            + "task_name varchar(255) check(length(task_name) > 1),"
            + "operator varchar(20),"
            + "start_time varchar(50) not null,"
            + "end_time varchar(50) not null,"
            + "task_count integer,"
            + "creator text,"
            + "create_time text"
            + ");";
    private static final String ALL_CATEGORY_SQL = "CREATE TABLE "
            + TABLE_ALL_CATEGORY +"("
            + "id integer PRIMARY KEY AUTOINCREMENT,"
            + "category text"
            +");";
    private static final String CONCRETE_CATEGORY_SQL = "CREATE TABLE "
            + TABLE_CONCRETE_CATEGORY + "("
            + "id integer primary key autoincrement,"
            + "category_id text,"
            + "category_name text"
            + ");";

    public SQLiteDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASK_CREATE_TABLE_SQL);
        db.execSQL(ALL_CATEGORY_SQL);
        db.execSQL(CONCRETE_CATEGORY_SQL);
        Log.d("SQLiteDbHelper","Create database success");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package com.example.coustomtoolbar.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yaojian on 2017/7/7.
 */

public class SystemTime {
    private String cruentTime;
    public static final int MIN_TIME = 1;
    public static  final int MAX_TIME = 2;
    public static  final  int CRUENT_TIME = 0;
    public SystemTime() {

    }
    public void getTimeWithFormat(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH-mm-ss");
        Date curData = new Date(System.currentTimeMillis());
        cruentTime = format.format(curData);
    }
    public String getTimeWithCalender(int type){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        if (type == CRUENT_TIME){

            return year+"-"+month+"-"+day+"  "+hour+":"+minute+":"+second+"";
        }
        else if (type == MIN_TIME){

            return 0+"-"+0+"-"+0+"  "+0+":"+0+":"+0+"";
        }
        else if (type ==MAX_TIME){

            return 9999+"-"+99+"-"+99+"  "+99+":"+99+":"+99+"";
        }
        else {

            return null;
        }
    }
}

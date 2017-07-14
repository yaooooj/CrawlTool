package com.example.coustomtoolbar.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yaojian on 2017/7/7.
 */

public class SystemTime {
    private String curentTime;
    private static SystemTime systemTime;
    private SystemTime() {

    }
    public static SystemTime getInstance(){
            if (systemTime == null){
                systemTime = new SystemTime();
            }
        return systemTime;
    }
    public String getTimeWithFormat(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        Date curData = new Date();
        curentTime = format.format(curData);
        return curentTime;
    }
    public String getAfterDay(){

        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").parse(getTimeWithFormat());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day =  c.get(Calendar.DATE);
        c.set(Calendar.DATE,day + 1);
        String dayAfter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(c.getTime());
        return dayAfter;
    }
}

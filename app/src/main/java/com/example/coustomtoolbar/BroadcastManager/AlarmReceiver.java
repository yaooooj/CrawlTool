package com.example.coustomtoolbar.BroadcastManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.coustomtoolbar.Adapter.BaseTaskAdapter;
import com.example.coustomtoolbar.BaseActivity;
import com.example.coustomtoolbar.MyService;
import com.example.coustomtoolbar.R;

import java.util.ArrayList;
import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private NotificationManager manager;
    private ArrayList<String> taskNameList;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        /*
        taskNameList = new ArrayList<>();
        taskNameList =  intent.getStringArrayListExtra("taskNameList");
        Intent service = new Intent(context, MyService.class);
        if (taskNameList == null){
            //context.startService(service);
            Log.e(TAG, "onReceive: " );
        }else {
            showNotification(context);
            //context.startService(service);
        }
        */
        Intent serviceIntent = new Intent(context, MyService.class);
        context.startService(serviceIntent);


    }
    public void showNotification(Context context){
        String taskName;
        Intent intent = new Intent(context, BaseActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (taskNameList != null){
            Log.e(TAG, "showNotification: " + "hhhhhhhhhhhhhhhh");
            for (int i = 0; i < taskNameList.size();i++) {
                taskName = taskNameList.get(i);
                Log.e(TAG, "showNotification: " + taskName );
                Notification notification = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_done_all_white_24dp)
                        .setTicker( taskName + " been removed")
                        .setContentTitle("this is a notification")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .build();
                manager.notify(0x00113, notification);
            }
        }




    }
}

package com.example.coustomtoolbar;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.coustomtoolbar.Bean.TaskModel;
import com.example.coustomtoolbar.BroadcastManager.AlarmReceiver;
import com.example.coustomtoolbar.DataBaseUtil.DBManager;
import com.example.coustomtoolbar.Util.SystemTime;
import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {
    private static String TAG = "MyService";
    private DBManager db;
    private List<TaskModel> taskModel;
    private SystemTime systemTime;
    private static int count = 0;
    private ArrayList<String> taskNameList;
    private Intent in;
    private PendingIntent pi;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: " );
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        managerTask();


        return START_REDELIVER_INTENT;

    }
    public void managerTask(){
        if (taskModel == null){
            taskNameList = new ArrayList<>();
        }
        if (systemTime == null){
            systemTime =SystemTime.getInstance();
        }
        if (taskModel == null){
            taskModel = new ArrayList<>();
        }
        db = DBManager.Instence(getApplicationContext());
        taskModel = db.queryWithSQL();
        final AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour = 10 * 1000;
        final long triggerAtTime = SystemClock.elapsedRealtime()+ anHour;
        in = new Intent(this, AlarmReceiver.class);
       // in.putStringArrayListExtra("taskNameList", taskNameList);
        //int.putExtra("task_name",taskModel.get(i))
        pi = PendingIntent.getBroadcast(this,0,in,PendingIntent.FLAG_UPDATE_CURRENT);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    String time = systemTime.getTimeWithFormat();
                    Log.e(TAG, "run: " + "mobile time  "+ time );
                    for (int i = 0;i < taskModel.size();i++ ){
                        if (taskModel.get(i).getEnd_time().compareTo(time) < 0){
                            db.delete(taskModel.get(i).getTask_name());
                            //Log.e(TAG, "run: " + taskModel.get(i).getTask_name());
                            taskNameList.add(taskModel.get(i).getTask_name());
                            taskModel.remove(i);

                        }
                        //Log.e(TAG, "run: "+  taskModel.get(i).getEnd_time());
                    }

                }
            }).start();

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        notification();


        Log.e(TAG, "run: " + "database have  " + taskModel.size() +" tasks "+ "Scan this  " + count + " times" );
        count++;
    }
    public void notification(){
        NotificationManager manager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intentToActivity = new Intent(this,BaseActivity.class);
        PendingIntent pendingIntent =  PendingIntent.getActivity(this,0,intentToActivity,0);
        if (taskNameList != null){
            for (int i = 0;i < taskNameList.size();i++ ){
                if (taskNameList.get(i) != null){
                    Notification notifyActivity = new NotificationCompat.Builder(this)
                            .setTicker("notify_activity")
                            .setContentTitle(taskNameList.get(i)+ " removed")
                            .setSmallIcon(R.mipmap.ic_remove_white_24dp)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build();
                    manager.notify(0x1111,notifyActivity);
                }
                taskNameList.remove(i);
            }
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: " );
    }

}

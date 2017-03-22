package com.example.diong.todo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

public class MyService extends Service {
    final String toSend = "com.ianc.Alarmtestintent";
    //联系数据库
    private myDB mydb = new myDB(MyService.this);
    private int altime = 15;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder
    {
        MyService getService() {
            return MyService.this;
        }
    }

    public void addAlarm(String content) {


        TodoItem item = mydb.getEntry(content);


        Calendar c = Calendar.getInstance();
        long now = c.getTimeInMillis();
        //从数据库取数据时间
        c.set(Calendar.YEAR, item.getToDoYear());
        c.set(Calendar.MONTH, item.getToDoMonth() - 1);//也可以填数字，0-11,一月为0
        c.set(Calendar.DAY_OF_MONTH, item.getToDoDay());
        c.set(Calendar.HOUR_OF_DAY, item.getToDoHour());
        c.set(Calendar.MINUTE, item.getToDoMinute());
        c.set(Calendar.SECOND, 0);

        if(c.getTimeInMillis()-item.getBeforeTime()*1000*60 > now && item.getToDoAlarmOP().equals("ON")) {
            //TODO：测试单独是否可行

            Log.i("-----add alarm ", item.getToDoContent());
            Intent intent = new Intent(toSend);
            intent.setClass(this, AlarmReciver.class);
            intent.putExtra("what", content);
            PendingIntent sender = PendingIntent.getBroadcast(
                    MyService.this, mydb.searchID(content)*998 + item.getToDoYear()*3 + item.getToDoHour()*6 + item.getToDoMonth()*2 + item.getToDoDay()*86, intent, 0);



            // Schedule the alarm!
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()-15*1000*60 , sender);
        }
    }

    public void deleteAlarm(String content) {
        // Create the same intent, and thus a matching IntentSender, for
        // the one that was scheduled.
        TodoItem item = mydb.getEntry(content);

        Intent intent = new Intent(toSend);
        intent.setClass(this, AlarmReciver.class);
        intent.putExtra("what", content);
        PendingIntent sender = PendingIntent.getBroadcast(
                MyService.this,  mydb.searchID(content)*998 + item.getToDoYear()*3 + item.getToDoHour()*6 + item.getToDoMonth()*2 + item.getToDoDay()*86, intent, 0);

        Log.i("---delete alarm",content);
        // And cancel the alarm.
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }

}

package com.example.diong.todo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReciver extends BroadcastReceiver {
    public AlarmReciver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String what = intent.getStringExtra("what");
      /*  AlertDialog.Builder builder=new AlertDialog.Builder(context.getApplicationContext());
        builder.setTitle(what);
        builder.setMessage("This is message");
        builder.setNegativeButton("OK", null);
        Dialog dialog=builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
        Log.v("alarm", what);*/
        Log.v("alarm", what);
      //  ApplicationInfo appInfo = context.getApplicationInfo();
       // int resID = context.getResources().getIdentifier("notimap", "mipmap", appInfo.packageName);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("TODO提示")
                .setContentText("离"+what+"完成还有15分钟，快去完成吧")
                .setSmallIcon(R.mipmap.notimap);
              /*  .setContentText(fruit.name[position])
                etLargeIcon(bm)
                .setContentIntent(jump);//一定要设置Icon*/
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = builder.build();
        manager.notify(0, notify);
        /*
        //获取bm图片
            ApplicationInfo appInfo = context.getApplicationInfo();
            int resID = context.getResources().getIdentifier(fruit.img[position], "mipmap", appInfo.packageName);
            Resources r = context.getResources();
            InputStream is = r.openRawResource(resID);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(is);
            Bitmap bm = bitmapDrawable.getBitmap();

            Intent tojump = new Intent(context, MainActivity.class);
            PendingIntent jump = PendingIntent.getActivity(context, 0, tojump, 0);
            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle("静态广播")
                    .setSmallIcon(resID)
                    .setContentText(fruit.name[position])
                    .setLargeIcon(bm)
                    .setContentIntent(jump);//一定要设置Icon
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify = builder.build();
            manager.notify(0, notify);
         */
    }
}

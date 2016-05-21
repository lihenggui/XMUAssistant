package com.merxury.xmuassistant;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

/**
 * Created by Huang on 2016/5/21.
 * 后台定点准时查询电费的方法
 * 未准确测试过！
 */
public class LongRunningService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            ElecQuery elec = new ElecQuery("09", "八号楼", "0436");

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                elec.getMoney();

                String a = "您的电费余额为" + elec.getMoney();

                NotificationManager manager = (NotificationManager)
                        getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder;
                builder = new Notification.Builder(getApplicationContext());
                builder.setAutoCancel(true);
                builder.setContentTitle("您的电费余额");//标题
                builder.setContentText(a);//内容
                builder.setWhen(System.currentTimeMillis());
                builder.setSmallIcon(android.R.drawable.sym_def_app_icon);
                builder.setWhen(System.currentTimeMillis());
                Notification notification = builder.build();
                manager.notify(1, notification);
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 24 * 60 * 60 * 1000; // 这是24小时的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.RTC_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}

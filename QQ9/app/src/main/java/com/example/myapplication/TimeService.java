package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import java.util.Locale;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimeService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "TimeNotificationChannel";
    private static final long INTERVAL_MS =60*1000; // 1分钟间隔

    private NotificationManager notificationManager;
    private Handler handler;
    private Runnable timeUpdater;

    @SuppressLint("ForegroundServiceType")
    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();
        handler = new Handler(Looper.getMainLooper());

        // 创建前台服务通知
        Notification notification = createNotification("服务运行中");
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return START_STICKY;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "时间通知",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("定时时间通知服务");
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification(String content) {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("当前时间")
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
    }

    private void startTimer() {
        timeUpdater = new Runnable() {
            @Override
            public void run() {
                updateTimeNotification();
                handler.postDelayed(this, INTERVAL_MS);
            }
        };
        handler.post(timeUpdater);
    }

    private void updateTimeNotification() {
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());

        Notification notification = createNotification(currentTime);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timeUpdater);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
package com.example.home.taskmanager.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.TaskManager;
import com.example.home.taskmanager.activity.AlarmNotification;
import com.example.home.taskmanager.model.AlarmModel;
import com.example.home.taskmanager.util.CommonUtils;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long alarmId = intent.getLongExtra(CommonUtils.ALARM_MODEL_ID, -1);

        AlarmModel alarmModel = AlarmModel.findById(AlarmModel.class,alarmId);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        PendingIntent activity;

        Intent newIntent = new Intent(context, AlarmNotification.class);
        newIntent.putExtra("ALARM_NAME",alarmModel.getMessage());
        newIntent.putExtra("ALARM_SOUND",alarmModel.isSound());
        newIntent.putExtra("ALARM_VIBRATE", TaskManager.isVibrate());
        context.startActivity(newIntent);

        activity = PendingIntent.getActivity(context, (int) alarmId, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        notification = builder
                .setContentIntent(activity)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setContentTitle("Missed alarm: " + alarmModel.getMessage())
                .build();

        notificationManager.notify((int) alarmId, notification);
    }

}
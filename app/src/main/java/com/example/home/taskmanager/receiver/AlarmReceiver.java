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
import com.example.home.taskmanager.model.Alarm;
import com.example.home.taskmanager.model.AlarmMsg;

public class AlarmReceiver extends BroadcastReceiver {

//	private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        long alarmMsgId = intent.getLongExtra(AlarmMsg.COL_ID, -1);
        long alarmId = intent.getLongExtra(AlarmMsg.COL_ALARMID, -1);

        AlarmMsg alarmMsg = new AlarmMsg(alarmMsgId);
        alarmMsg.setStatus(AlarmMsg.EXPIRED);
        alarmMsg.persist(TaskManager.db);

        Alarm alarm = new Alarm(alarmId);
        alarm.load(TaskManager.db);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        PendingIntent activity;

        Intent newIntent = new Intent(context, AlarmNotification.class);
        newIntent.putExtra("ALARM_NAME",alarm.getName());
        context.startActivity(newIntent);

        activity = PendingIntent.getActivity(context, (int)alarm.getId(), newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        notification = builder
                .setContentIntent(activity)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setAutoCancel(true)
                .setContentTitle("Missed alarm: " + alarm.getName())
                .build();

        notificationManager.notify((int)alarm.getId(), notification);
    }

}
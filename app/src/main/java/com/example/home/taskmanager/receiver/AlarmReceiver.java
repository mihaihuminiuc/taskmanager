package com.example.home.taskmanager.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
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

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent newIntent = new Intent(context, AlarmNotification.class);
        newIntent.putExtra("ALARM_NAME",alarmModel.getMessage());
        newIntent.putExtra("ALARM_SOUND",alarmModel.isSound());
        newIntent.putExtra("ALARM_VIBRATE", TaskManager.isVibrate());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Missed alarm: " + alarmModel.getMessage())
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager.notify((int) alarmId, notificationBuilder.build());

        context.startActivity(newIntent);
    }

}
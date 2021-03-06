package com.example.home.taskmanager.receiver;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.TaskManager;
import com.example.home.taskmanager.activity.AlarmNotification;
import com.example.home.taskmanager.model.AlarmModel;
import com.example.home.taskmanager.util.CommonUtils;
import com.example.home.taskmanager.util.NotificationUtils;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long alarmId = intent.getLongExtra(CommonUtils.ALARM_MODEL_ID, -1);

        AlarmModel alarmModel = AlarmModel.findById(AlarmModel.class,alarmId);

        Intent newIntent = new Intent(context, AlarmNotification.class);
        newIntent.putExtra("ALARM_NAME",alarmModel.getMessage());
        newIntent.putExtra("ALARM_SOUND",alarmModel.isSound());
        newIntent.putExtra("ALARM_VIBRATE", TaskManager.isVibrate());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(newIntent);
    }

}
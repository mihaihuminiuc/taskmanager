package com.example.home.taskmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.home.taskmanager.activity.AlarmNotificationActivity;
import com.example.home.taskmanager.model.Alarm;
import com.example.home.taskmanager.util.CommonUtils;

public class AlarmReceiver extends BroadcastReceiver {
    private final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent newIntent = new Intent(context, AlarmNotificationActivity.class);

        Alarm alarm = intent.getExtras().getParcelable(CommonUtils.bundleKeyAlarm);

        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AlarmNotificationActivity.MODE_NAME,alarm);
        Log.i(TAG, "AlarmReceiver.onReceive('" + alarm.getmTitle() + "')");

        context.startActivity(newIntent);
    }
}

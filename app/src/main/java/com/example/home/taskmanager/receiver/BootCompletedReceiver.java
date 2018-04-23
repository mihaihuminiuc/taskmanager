package com.example.home.taskmanager.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.home.taskmanager.listadapter.AlarmListAdapter;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new AlarmListAdapter(context);
    }
}

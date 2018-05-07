package com.example.home.taskmanager.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.home.taskmanager.model.AlarmModel;
import com.example.home.taskmanager.receiver.AlarmReceiver;
import com.example.home.taskmanager.util.CommonUtils;

public class AlarmService extends IntentService {

    private static final String TAG = "AlarmService";

    public static final String POPULATE = "POPULATE";
    public static final String CREATE = "CREATE";
    public static final String CANCEL = "CANCEL";

    private IntentFilter matcher;

    public AlarmService() {
        super(TAG);
        matcher = new IntentFilter();
        matcher.addAction(POPULATE);
        matcher.addAction(CREATE);
        matcher.addAction(CANCEL);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        long alarmId = intent.getLongExtra(CommonUtils.ALARM_MODEL_ID, 0);

        if (matcher.matchAction(action)) {

            if (POPULATE.equals(action)) {
                execute(CREATE, alarmId);
            }

            if (CREATE.equals(action)) {
                execute(CREATE, alarmId);
            }

            if (CANCEL.equals(action)) {
                execute(CREATE, alarmId);
            }
        }
    }

    private void execute(String action, long alarmId) {
        Intent mIntent;
        PendingIntent mPendingIntent;
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long time;

        mIntent = new Intent(this, AlarmReceiver.class);
        mIntent.putExtra(CommonUtils.ALARM_MODEL_ID, AlarmModel.findById(AlarmModel.class,alarmId).getId());

        mPendingIntent = PendingIntent.getBroadcast(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        time = AlarmModel.findById(AlarmModel.class,alarmId).getTime();
        if (CREATE.equals(action) && mAlarmManager!=null) {
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, time, mPendingIntent);
        }
    }

}

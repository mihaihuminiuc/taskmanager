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

import java.util.Calendar;
import java.util.TimeZone;

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
                execute(CANCEL, alarmId);
            }
        }
    }

    private void execute(String action, long alarmId) {
        Intent mIntent;
        PendingIntent mPendingIntent;
        AlarmModel alarmModel;
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        long now = System.currentTimeMillis();
        long time, diff;

        alarmModel = AlarmModel.findById(AlarmModel.class,alarmId);

        mIntent = new Intent(this, AlarmReceiver.class);
        mIntent.putExtra(CommonUtils.ALARM_MODEL_ID, alarmModel.getId());

        mPendingIntent = PendingIntent.getBroadcast(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        c.setTimeInMillis(AlarmModel.findById(AlarmModel.class,alarmId).getTime());

        time = c.getTimeInMillis();

        diff = time -now + (long)CommonUtils.MIN;
        if (CREATE.equals(action)) {
            if (diff > 0 && diff < CommonUtils.YEAR)
                mAlarmManager.set(AlarmManager.RTC_WAKEUP, time, mPendingIntent);
        } else if (CANCEL.equals(action)) {
            mAlarmManager.cancel(mPendingIntent);
            AlarmModel.delete(alarmModel);
        }
    }

}

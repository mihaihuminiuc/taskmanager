package com.example.home.taskmanager.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;

import com.example.home.taskmanager.TaskManager;
import com.example.home.taskmanager.model.Alarm;
import com.example.home.taskmanager.model.AlarmMsg;
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
        String alarmId = intent.getStringExtra(AlarmMsg.COL_ALARMID);
        String alarmMsgId = intent.getStringExtra(AlarmMsg.COL_ID);
        String startTime = intent.getStringExtra(Alarm.COL_FROMDATE);
        String endTime = intent.getStringExtra(Alarm.COL_TODATE);

        if (matcher.matchAction(action)) {
            if (POPULATE.equals(action)) {
                TaskManager.dbHelper.populate(Long.parseLong(alarmId), TaskManager.db);
                execute(CREATE, alarmId);
            }

            if (CREATE.equals(action)) {
                execute(CREATE, alarmId, alarmMsgId, startTime, endTime);
            }

            if (CANCEL.equals(action)) {
                execute(CANCEL, alarmId, alarmMsgId, startTime, endTime);
                TaskManager.dbHelper.shred(TaskManager.db);
            }
        }
    }

    /**
     * @param action
     * @param args {alarmId, alarmMsgId, startTime, endTime}
     */
    private void execute(String action, String... args) {
        Intent i;
        PendingIntent pi;
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Cursor c;

        String alarmId = (args!=null && args.length>0) ? args[0] : null;
        String alarmMsgId = (args!=null && args.length>1) ? args[1] : null;
        String startTime = (args!=null && args.length>2) ? args[2] : null;
        String endTime = (args!=null && args.length>3) ? args[3] : null;

        String status = CANCEL.equals(action) ? AlarmMsg.CANCELLED : AlarmMsg.ACTIVE;

        if (alarmMsgId != null) {
            c = TaskManager.db.query(AlarmMsg.TABLE_NAME, null, AlarmMsg.COL_ID+" = ?", new String[]{alarmMsgId}, null, null, null);

        } else {
            c = AlarmMsg.list(TaskManager.db, alarmId, startTime, endTime, status);
        }

        if (c.moveToFirst()) {
            long now = System.currentTimeMillis();
            long time, diff;

            do {
                i = new Intent(this, AlarmReceiver.class);
                i.putExtra(AlarmMsg.COL_ID, c.getLong(c.getColumnIndex(AlarmMsg.COL_ID)));
                i.putExtra(AlarmMsg.COL_ALARMID, c.getLong(c.getColumnIndex(AlarmMsg.COL_ALARMID)));

                pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

                time = c.getLong(c.getColumnIndex(AlarmMsg.COL_DATETIME));
                diff = time-now + (long) CommonUtils.MIN;
                if (CREATE.equals(action)) {
                    if (diff > 0 && diff < CommonUtils.YEAR)
                        am.set(AlarmManager.RTC_WAKEUP, time, pi);
                } else if (CANCEL.equals(action)) {
                    am.cancel(pi);
                }
            } while(c.moveToNext());
        }
        c.close();
    }

}
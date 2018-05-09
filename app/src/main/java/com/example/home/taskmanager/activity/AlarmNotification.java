package com.example.home.taskmanager.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.TaskManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AlarmNotification extends Activity implements View.OnClickListener{

    private Ringtone mRingtone;
    private Vibrator mVibrator;
    private final long[] mVibratePattern = { 0, 500, 500 };
    private boolean mVibrate;
    private boolean mSound;
    private Timer mTimer = null;
    private TextView mTextView;
    private Button mDismissButton;
    private Future longRunningTaskFuture;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.notification_activity);

        initUI();
    }

    private void initUI(){
        mTextView = findViewById(R.id.alarm_title_text);
        mDismissButton = findViewById(R.id.dismiss_alarm);

        getData();
    }

    private void getData(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mRingtone = RingtoneManager.getRingtone(getApplicationContext(),  Uri.parse(prefs.getString("ringtone_pref", "DEFAULT_RINGTONE_URI")));

        if (getIntent().hasExtra("ALARM_NAME"))
            mTextView.setText(getIntent().getStringExtra("ALARM_NAME"));

        if (getIntent().hasExtra("ALARM_SOUND"))
            mSound = getIntent().getBooleanExtra("ALARM_SOUND",false);

        if (getIntent().hasExtra("ALARM_VIBRATE"))
            mVibrate = getIntent().getBooleanExtra("ALARM_VIBRATE",false);

        mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        setActions();
    }

    private void setActions(){
        mDismissButton.setOnClickListener(this);
        start();
    }

    private void start(){

        ExecutorService threadPoolExecutor = Executors.newSingleThreadExecutor();
        Runnable longRunningTask = new Runnable() {
            @Override
            public void run() {
                if(mSound)
                    mRingtone.play();

                if (mVibrate)
                    mVibrator.vibrate(mVibratePattern, 1);
            }
        };

        longRunningTaskFuture = threadPoolExecutor.submit(longRunningTask);



        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                longRunningTaskFuture.cancel(true);
                Log.i("TAG_NOTIFICATION","HERE");
                finish();
            }
        }, Long.valueOf(TaskManager.getRingTime()));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dismiss_alarm:
                longRunningTaskFuture.cancel(true);
                Log.i("TAG_NOTIFICATION","HERE");
                finish();
                break;
        }
    }
}

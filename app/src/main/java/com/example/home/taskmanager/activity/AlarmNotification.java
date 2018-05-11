package com.example.home.taskmanager.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
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
import com.example.home.taskmanager.util.CommonUtils;
import com.example.home.taskmanager.util.NotificationUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AlarmNotification extends Activity implements View.OnClickListener{

    private Uri mRingtoneUri;
    private Vibrator mVibrator;
    private final long[] mVibratePattern = { 0, 500, 500 };
    private boolean mVibrate;
    private boolean mSound;
    private Timer mTimer = null;
    private TextView mTextView;
    private Button mDismissButton;
    private boolean run = false;
    private NotificationUtils mNotificationUtils;
    private Context mContext;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        mContext = this;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

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

        mRingtoneUri = Uri.parse(prefs.getString("ringtone_pref", "DEFAULT_RINGTONE_URI"));

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

        long delay = 60000;
        mTimer = new Timer();

        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(run) {
                    if(mSound)
                        CommonUtils.playSound(getApplicationContext(),mRingtoneUri,true);
                    if (mVibrate)
                        mVibrator.vibrate(mVibratePattern, 1);
                } else {
                    CommonUtils.playSound(getApplicationContext(),mRingtoneUri,false);
                    mVibrator.cancel();
                    showNotification();
                    mTimer.cancel();
                    mTimer.purge();
                }
            }
        };

        mTimer.schedule(task, delay);

        // set run to false here to stop the timer.
        run = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dismiss_alarm:
                CommonUtils.playSound(getApplicationContext(),mRingtoneUri,false);
                mVibrator.cancel();
                mTimer.cancel();
                mTimer.purge();
                finish();
                break;
        }
    }

    private void showNotification(){

        Intent intent = new Intent(mContext, AlarmActivity.class);

        PendingIntent pi = PendingIntent.getActivity(mContext,
                0 /* Request code */,
                intent,
                PendingIntent.FLAG_ONE_SHOT);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mNotificationUtils = new NotificationUtils(mContext);
            Notification.Builder nb = mNotificationUtils.getChannelNotification("Missed alarm", getIntent().getStringExtra("ALARM_NAME"), pi);

            mNotificationUtils.getManager().notify(101, nb.build());
        }else{
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, mContext.getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Missed alarm: " + getIntent().getStringExtra("ALARM_NAME"))
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setContentIntent(pi);

            NotificationManager manager =
                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            manager.notify(1, builder.build());
        }
    }
}

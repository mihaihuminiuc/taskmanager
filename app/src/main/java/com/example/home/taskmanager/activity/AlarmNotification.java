package com.example.home.taskmanager.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.home.taskmanager.R;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmNotification extends Activity {

    private Ringtone mRingtone;
    private Vibrator mVibrator;
    private final long[] mVibratePattern = { 0, 500, 500 };
    private boolean mVibrate;
    private Uri mAlarmSound;
    private long mPlayTime;
    private Timer mTimer = null;
    private TextView mTextView;
    private String text;

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.notification_activity);

        mTextView = (TextView)findViewById(R.id.alarm_title_text);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mRingtone = RingtoneManager.getRingtone(getApplicationContext(),  Uri.parse(prefs.getString("ringtone_pref", "DEFAULT_RINGTONE_URI")));
        if (mVibrate)
            mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        if (getIntent().hasExtra("ALARM_NAME")) {
            text = getIntent().getStringExtra("ALARM_NAME");
            mTextView.setText(text);
        }

        start();
    }

    private void start(){
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                finish();
            }
        }, 10000);
        mRingtone.play();
        if (mVibrate)
            mVibrator.vibrate(mVibratePattern, 0);
    }
}

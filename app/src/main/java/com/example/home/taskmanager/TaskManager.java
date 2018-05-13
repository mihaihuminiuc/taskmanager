package com.example.home.taskmanager;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.home.taskmanager.util.CommonUtils;
import com.orm.SugarContext;
import com.orm.SugarDb;

import java.io.File;

/**
 * Created by humin on 4/1/2018.
 */

public class TaskManager extends Application {

    public static SharedPreferences mSharedPreferences;

    public static final String TIME_OPTION = "time_option";
    public static final String DATE_RANGE = "date_range";
    public static final String DATE_FORMAT = "date_format";
    public static final String TIME_FORMAT = "time_format";
    public static final String VIBRATE_PREF = "vibrate_pref";
    public static final String RINGTONE_PREF = "ringtone_pref";
    public static final String RING_TIME = "ring_time";

    public static final String DEFAULT_DATE_FORMAT = "yyyy-M-d";

    @Override
    public void onCreate() {
        super.onCreate();

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SugarContext.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    public static boolean showRemainingTime() {
        return "1".equals(mSharedPreferences.getString(TIME_OPTION, "0"));
    }

    public static int getDateRange() {
        return Integer.parseInt(mSharedPreferences.getString(DATE_RANGE, "0"));
    }

    public static String getDateFormat() {
        return mSharedPreferences.getString(DATE_FORMAT, DEFAULT_DATE_FORMAT);
    }

    public static boolean is24Hours() {
        return mSharedPreferences.getBoolean(TIME_FORMAT, true);
    }

    public static boolean isVibrate() {
        return mSharedPreferences.getBoolean(VIBRATE_PREF, true);
    }

    public static String getRingtone() {
        return mSharedPreferences.getString(RINGTONE_PREF, android.provider.Settings.System.DEFAULT_NOTIFICATION_URI.toString());
    }
    public static String getRingTime(){
        return mSharedPreferences.getString(RING_TIME,String.valueOf(60000));
    }


}

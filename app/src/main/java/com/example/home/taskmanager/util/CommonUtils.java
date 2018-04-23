package com.example.home.taskmanager.util;

import android.content.Context;
import android.os.Build;

import com.example.home.taskmanager.model.Alarm;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by humin on 4/1/2018.
 */

public class CommonUtils {

    public static final int NEVER = 0;
    public static final int EVERY_DAY = 7;

    public static final int ONCE = 0;
    public static final int WEEKLY = 1;

    public static final String bundleKeyAlarm = "ALARM_OBJECT";

    public static int getColor(Context context, int resId) {
        int color;
        if (Build.VERSION.SDK_INT < 23) {
            color = context.getResources().getColor(resId);
        } else {
            color = context.getResources().getColor(resId, null);
        }
        return color;
    }
}

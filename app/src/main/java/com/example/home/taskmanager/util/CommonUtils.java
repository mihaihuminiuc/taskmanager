package com.example.home.taskmanager.util;

import android.content.Context;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import com.example.home.taskmanager.TaskManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by humin on 4/1/2018.
 */

public class CommonUtils {

    public final static String ALARM_MODEL_ID = "ALARM_MODEL_ID";
    public static final double MIN = 60 * 1000.0;
    public static final double HOUR = 60 * MIN;
    public static final double DAY = 24 * HOUR;
    public static final double MONTH = 30 * DAY;
    public static final double YEAR = 365 * DAY;


    public static String concat(Object... objects) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.setLength(0);
        for (Object obj : objects) {
            stringBuilder.append(obj);
        }
        return stringBuilder.toString();
    }

    public static final String getDateStr(int year, int month, int date) {
        return concat(year, "-", month, "-", date);
    }

    public static final String getTimeStr(int hour, int minute) {
        return concat(hour, ":", minute>9 ? "":"0", minute);
    }
}

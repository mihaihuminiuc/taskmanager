package com.example.home.taskmanager.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Date;

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
        month = month+1;
        return concat(year, "-", month>9 ? "":"0", month, "-", date>9 ? "":"0",date);
    }

    public static final String getTimeStr(int hour, int minute) {
        return concat(hour, ":", minute>9 ? "":"0", minute);
    }

    public static Date atEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date atStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static void playSound(Context context, Uri soundUri, boolean play){
        MediaPlayer mp = MediaPlayer.create(context, soundUri);

        if(play){
            try {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.release();

                    mp = MediaPlayer.create(context, soundUri);
                }

                mp.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            try {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean copyFile(String from, String to) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                int end = from.toString().lastIndexOf("/");
                String str1 = from.toString().substring(0, end);
                String str2 = from.toString().substring(end+1, from.length());
                File source = new File(str1, str2);
                File destination= new File(to, str2);
                if (source.exists()) {
                    FileChannel src = new FileInputStream(source).getChannel();
                    FileChannel dst = new FileOutputStream(destination).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

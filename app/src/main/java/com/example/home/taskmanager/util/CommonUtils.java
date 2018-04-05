package com.example.home.taskmanager.util;

import android.content.Context;
import android.os.Build;

/**
 * Created by humin on 4/1/2018.
 */

public class CommonUtils {

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

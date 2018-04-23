package com.example.home.taskmanager;

import android.app.Application;

import com.orm.SugarContext;

/**
 * Created by humin on 4/1/2018.
 */

public class TaskManager extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
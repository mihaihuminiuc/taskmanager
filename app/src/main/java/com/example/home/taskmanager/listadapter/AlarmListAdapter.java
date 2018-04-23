package com.example.home.taskmanager.listadapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.model.Alarm;
import com.example.home.taskmanager.receiver.AlarmReceiver;
import com.example.home.taskmanager.util.CommonUtils;
import com.example.home.taskmanager.util.DateTime;
import com.orm.SugarDb;

import java.util.ArrayList;
import java.util.List;

public class AlarmListAdapter extends RecyclerView.Adapter {
    private final String TAG = "AlarmMe";


    private final int ALARM_LIST_CODE=456;

    private List<Alarm> alarms;

    private Context mContext;
    private LayoutInflater mInflater;
    private DateTime mDateTime;

    private AlarmManager mAlarmManager;


    public AlarmListAdapter(Context context)
    {
        mContext = context;

        if(Alarm.listAll(Alarm.class) != null  && !Alarm.listAll(Alarm.class).isEmpty()){
            alarms = Alarm.listAll(Alarm.class);
        }

        Log.i(TAG, "AlarmListAdapter.create()");

        mInflater = LayoutInflater.from(context);
        mDateTime = new DateTime(context);

        mAlarmManager = (AlarmManager)context.getSystemService(mContext.ALARM_SERVICE);
    }


    private void setAlarm(Alarm alarm)
    {
        PendingIntent sender;
        Intent intent;

        if (alarm.ismEnabled() && !(alarm.getmOccurrence()< System.currentTimeMillis()))
        {
            intent = new Intent(mContext, AlarmReceiver.class);

            Bundle bundle = new Bundle();
            bundle.putParcelable(CommonUtils.bundleKeyAlarm,alarm);

            intent.putExtras(bundle);

            sender = PendingIntent.getBroadcast(mContext, ALARM_LIST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.getmDate(), sender);
            Log.i(TAG, "AlarmListAdapter.setAlarm(" + alarm.getId() + ", '" + alarm.getmTitle() + "', " + alarm.getmDate()+")");
        }
    }

    private void cancelAlarm(Alarm alarm)
    {
        PendingIntent sender;
        Intent intent;

        intent = new Intent(mContext, AlarmReceiver.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonUtils.bundleKeyAlarm,alarm);

        intent.putExtras(bundle);

        sender = PendingIntent.getBroadcast(mContext, ALARM_LIST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.cancel(sender);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new AlarmListHolder(view,mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((AlarmListHolder) holder).bindDatat(alarms.get(position),mDateTime);
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }
}


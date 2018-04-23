package com.example.home.taskmanager.listadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.model.Alarm;
import com.example.home.taskmanager.util.DateTime;

public class AlarmListHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView details;
    private int mColorOutdated;
    private int mColorActive;

    public AlarmListHolder(View itemView, Context mContext) {
        super(itemView);
        title = itemView.findViewById(R.id.item_title);
        details = itemView.findViewById(R.id.item_details);
        mColorOutdated = mContext.getResources().getColor(R.color.alarm_title_outdated);
        mColorActive = mContext.getResources().getColor(R.color.alarm_title_active);
    }

    public void bindDatat(final Alarm alarm, DateTime mDateTime){
        title.setText(alarm.getmTitle());
        details.setText(String.format("%s %s", mDateTime.formatDetails(alarm), alarm.ismEnabled() ? "" : " [disabled]"));

        if (alarm.getmOccurrence()< System.currentTimeMillis())
            title.setTextColor(mColorOutdated);
        else
            title.setTextColor(mColorActive);
    }
}

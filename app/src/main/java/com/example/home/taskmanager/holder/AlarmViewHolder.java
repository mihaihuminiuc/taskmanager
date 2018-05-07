package com.example.home.taskmanager.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.listeners.AlarmClickListener;
import com.example.home.taskmanager.model.AlarmModel;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AlarmViewHolder extends RecyclerView.ViewHolder {

    private TextView mMonth, mYear, mDate, mMesage, mTime;
    private String[] mMonthList;

    public AlarmViewHolder(View itemView) {
        super(itemView);
        mMonth = itemView.findViewById(R.id.month_tv);
        mYear = itemView.findViewById(R.id.year_tv);
        mDate = itemView.findViewById(R.id.date_tv);
        mMesage = itemView.findViewById(R.id.msg_tv);
        mTime = itemView.findViewById(R.id.time_tv);
    }

    public void bindData(Context context, AlarmModel alarmModel, AlarmClickListener AlarmClickListener){

        mMonthList = context.getResources().getStringArray(R.array.spinner3_arr);

        Date date = new Date();
        date.setTime(alarmModel.getTime());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        mMesage.setText(alarmModel.getMessage());
        mDate.setText(String.valueOf(calendar.get(Calendar.DATE)));
        mMonth.setText(mMonthList[calendar.get(Calendar.MONTH) + 1]);
        mYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        mTime.setText(calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE));
    }
}

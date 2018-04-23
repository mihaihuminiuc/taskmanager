package com.example.home.taskmanager.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.listeners.CalendarActivityListener;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by humin on 4/1/2018.
 */

public class CalendarFragment extends Fragment implements CalendarView.OnDateChangeListener {

    private CalendarView calendarView;
    private Context mContext;
    private CalendarActivityListener mCalendarActivityListener;

    public static CalendarFragment newInstance(){
        return new CalendarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        initUI(view);
        setActions();
    }

    private void initUI(View view){
        calendarView = view.findViewById(R.id.calendar_view);
    }

    private void setActions(){
        calendarView.setOnDateChangeListener(this);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        Toast.makeText(mContext, dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();

        Date date = new Date();
        date.setTime(Calendar.getInstance().getTimeInMillis());

        mCalendarActivityListener.onDateClick(date);
    }

    @Override
    public void onAttach(Context  context){
        super.onAttach(context);

        if(context instanceof CalendarActivityListener)
            mCalendarActivityListener = (CalendarActivityListener) context;
        else {
            throw new RuntimeException(context.toString()
                    + " must implement CalendarActivityListener");
        }

    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCalendarActivityListener = null;
    }
}

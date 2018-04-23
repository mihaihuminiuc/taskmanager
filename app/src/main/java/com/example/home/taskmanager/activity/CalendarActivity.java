package com.example.home.taskmanager.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.fragments.CalendarFragment;
import com.example.home.taskmanager.listeners.CalendarActivityListener;

import java.util.Date;

public class CalendarActivity extends AppCompatActivity implements CalendarActivityListener {

    private FragmentTransaction mFragmentTransaction;

    private CalendarFragment mCalendarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

        initFragments();
        initUI();
    }

    private void initFragments(){
        mCalendarFragment = CalendarFragment.newInstance();
    }

    private void initUI(){
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.frame_x, mCalendarFragment);
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        mFragmentTransaction.commit();
    }

    @Override
    public void showCalendar() {
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.frame_x, mCalendarFragment);
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        mFragmentTransaction.commit();
    }

    @Override
    public void onDateClick(Date date) {
        Intent intent = new Intent(getApplicationContext(), AlarmActivity.class);
        startActivity(intent);
    }
}

package com.example.home.taskmanager.listeners;
import java.util.Date;

/**
 * Created by humin on 4/1/2018.
 */

public interface CalendarActivityListener {

    void showCalendar();
    void onDateClick(Date date);

}
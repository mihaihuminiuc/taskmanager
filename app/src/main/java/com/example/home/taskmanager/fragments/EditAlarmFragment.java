package com.example.home.taskmanager.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.listeners.AlarmActivityListener;
import com.example.home.taskmanager.model.Alarm;
import com.example.home.taskmanager.util.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EditAlarmFragment extends Fragment
        implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener{

    private Context mContext;
    private AlarmActivityListener mAlarmActivityListener;

    private Alarm mAlarm;
    private DateTime mDateTime;

    private EditText mTitle;
    private CheckBox mAlarmEnabled;
    private Spinner mOccurence;
    private Button mDateButton;
    private Button mTimeButton;

    public EditAlarmFragment newInstance(Alarm alarm){
        this.mAlarm=alarm;
        return new EditAlarmFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edit_alarm_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        mDateTime = new DateTime(mContext);
        initUI(view);
        setupActions();

    }

    private void initUI(View view){
        mTitle = view.findViewById(R.id.title);
        mAlarmEnabled = view.findViewById(R.id.alarm_checkbox);
        mOccurence = view.findViewById(R.id.occurence_spinner);
        mDateButton = view.findViewById(R.id.date_button);
        mTimeButton = view.findViewById(R.id.time_button);
    }

    private void setupActions() {
        mTimeButton.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context  context){
        super.onAttach(context);

        if(context instanceof AlarmActivityListener)
            mAlarmActivityListener = (AlarmActivityListener) context;
        else {
            throw new RuntimeException(context.toString()
                    + " must implement AlarmActivityListener");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mAlarmActivityListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.time_button:
                showTimePicker();
                break;
        }
    }

    private void showDatePicker(){
        Date date = new Date(mAlarm.getmDate());

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                mContext, this, year, month, day);

        if(!datePickerDialog.isShowing())
            datePickerDialog.show();
    }

    private void showTimePicker(){
        Date date = new Date();

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                mContext,this,Calendar.getInstance().HOUR,Calendar.getInstance().MINUTE,true);

        if(!timePickerDialog.isShowing())
            timePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }
}

package com.example.home.taskmanager.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.TaskManager;
import com.example.home.taskmanager.activity.AddAlarmActivity;
import com.example.home.taskmanager.model.AlarmModel;
import com.example.home.taskmanager.service.AlarmService;
import com.example.home.taskmanager.util.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddAlarmFragment extends Fragment implements View.OnClickListener{

    private EditText mMessageEditText;
    private CheckBox mSoundCheckBox;

    private Button mTimeButton, mDateButton, mAddAlarmButton;

    private TimePickerDialog mTimePicker;
    private DatePickerDialog mDatePicker;

    private static final SimpleDateFormat sdf = new SimpleDateFormat();

    private AlarmModel alarmModel;

    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle extras = getActivity().getIntent().getExtras();
        long longId;

        if (extras != null) {
            longId = extras.getLong(CommonUtils.ALARM_MODEL_ID);
            // and get whatever type user account id is
            alarmModel = AlarmModel.findById(AlarmModel.class,longId);
        }

        mContext=getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_alarm, container, false);
        initUI(view);
        setupActions();

        if(alarmModel!=null)
            setUIFields();

        return view;
    }

    private void initUI(View view) {
        mMessageEditText = view.findViewById(R.id.msg_et);
        mSoundCheckBox = view.findViewById(R.id.sound_cb);
        mTimeButton = view.findViewById(R.id.time_picker_button);
        mDateButton = view.findViewById(R.id.date_picker_button);
        mAddAlarmButton = view.findViewById(R.id.add_alarm);
    }

    private void setUIFields(){
        mMessageEditText.setText(alarmModel.getMessage());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarmModel.getTime());

        mTimeButton.setText(CommonUtils.getTimeStr(calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE)));
        mDateButton.setText(CommonUtils.getDateStr(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)));
    }

    private void setupActions(){
        mTimeButton.setOnClickListener(this);
        mDateButton.setOnClickListener(this);
        mAddAlarmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date_picker_button:
                showDatePicker();
                break;
            case R.id.time_picker_button:
                showTimePicker();
                break;
            case R.id.add_alarm:
                create();
                break;
        }
    }

    private void showTimePicker(){
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                mTimeButton.setText( CommonUtils.getTimeStr(selectedHour,selectedMinute));
            }
        }, hour, minute, TaskManager.is24Hours());//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void showDatePicker(){
        Calendar cal = Calendar.getInstance();

        mDatePicker = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mDateButton.setText(CommonUtils.getDateStr(year,month,dayOfMonth));
                    }
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        mDatePicker.setCancelable(false);
        mDatePicker.setTitle("Select the date");
        mDatePicker.show();
    }

    private boolean validate() {
        if (TextUtils.isEmpty(mMessageEditText.getText())) {
            mMessageEditText.requestFocus();
            Toast.makeText(mContext, "Enter a message", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void create() {
        if (!validate()) return;

        String toParse = mDateButton.getText().toString() + " " + mTimeButton.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.default_date_format));

        Date date = null;
        try {
            date = formatter.parse(toParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long millis = date.getTime();

        AlarmModel alarmModel = new AlarmModel();
        alarmModel.setTime(millis);
        alarmModel.setMessage(mMessageEditText.getText().toString());
        alarmModel.setSound(mSoundCheckBox.isChecked());

        AlarmModel.save(alarmModel);

        Intent service = new Intent(mContext, AlarmService.class);
        service.putExtra(CommonUtils.ALARM_MODEL_ID, alarmModel.getId());
        service.setAction(AlarmService.POPULATE);
        mContext.startService(service);
    }
}

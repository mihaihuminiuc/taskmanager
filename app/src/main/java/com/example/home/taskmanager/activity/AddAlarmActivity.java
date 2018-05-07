package com.example.home.taskmanager.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.TaskManager;
import com.example.home.taskmanager.model.AlarmModel;
import com.example.home.taskmanager.service.AlarmService;
import com.example.home.taskmanager.util.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddAlarmActivity extends Activity implements View.OnClickListener{

    private EditText mMessageEditText;
    private CheckBox mSoundCheckBox;

    private Button mTimeButton, mDateButton, mAddAlarmButton;

    private TimePickerDialog mTimePicker;
    private DatePickerDialog mDatePicker;

    private static final SimpleDateFormat sdf = new SimpleDateFormat();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        initUI();
        setupActions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sdf.applyPattern(TaskManager.getDateFormat());
    }

    private void initUI() {
        mMessageEditText = findViewById(R.id.msg_et);
        mSoundCheckBox = findViewById(R.id.sound_cb);
        mTimeButton = findViewById(R.id.time_picker_button);
        mDateButton = findViewById(R.id.date_picker_button);
        mAddAlarmButton = findViewById(R.id.add_alarm);
    }

    private void setupActions(){
        mTimeButton.setOnClickListener(this);
        mDateButton.setOnClickListener(this);
        mAddAlarmButton.setOnClickListener(this);
    }

    private boolean validate() {
        if (TextUtils.isEmpty(mMessageEditText.getText())) {
            mMessageEditText.requestFocus();
            Toast.makeText(this, "Enter a message", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void create() {
        if (!validate()) return;

        String toParse = mDateButton.getText().toString() + " " + mTimeButton.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat(getString(R.string.default_date_format));

        Date date = null; // You will need try/catch around this
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

        Intent service = new Intent(this, AlarmService.class);
        service.putExtra(CommonUtils.ALARM_MODEL_ID, alarmModel.getId());
        service.setAction(AlarmService.POPULATE);
        startService(service);

        finish();
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
        mTimePicker = new TimePickerDialog(AddAlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

        mDatePicker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mDateButton.setText( CommonUtils.getDateStr(year,month,dayOfMonth));
                    }
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        mDatePicker.setCancelable(false);
        mDatePicker.setTitle("Select the date");
        mDatePicker.show();
    }
}

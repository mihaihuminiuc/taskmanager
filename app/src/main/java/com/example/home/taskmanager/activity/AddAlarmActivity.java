package com.example.home.taskmanager.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.TaskManager;
import com.example.home.taskmanager.model.Alarm;
import com.example.home.taskmanager.model.AlarmMsg;
import com.example.home.taskmanager.model.AlarmTime;
import com.example.home.taskmanager.model.DbHelper;
import com.example.home.taskmanager.service.AlarmService;
import com.example.home.taskmanager.util.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddAlarmActivity extends Activity {

    private EditText mMessageEditText;
    private CheckBox mSoundCheckBox;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private TextView mStartDateTextView, mEndDateTextView, mTimeTextView;

    private ViewSwitcher mViewSwitcher;
    private RadioGroup mRadioGroup;
    private RelativeLayout rl3;
    private RelativeLayout rl4;

    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;

    private EditText minsEdit;
    private EditText hoursEdit;
    private EditText daysEdit;
    private EditText monthsEdit;
    private EditText yearsEdit;

    private SQLiteDatabase db;

    private static final int DIALOG_FROMDATE = 1;
    private static final int DIALOG_TODATE = 2;
    private static final int DIALOG_ATTIME = 3;

    private static final SimpleDateFormat sdf = new SimpleDateFormat();

    private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            if (spinner1.getSelectedItemPosition() > 0 && spinner2.getSelectedItemPosition() > 0)
                spinner1.setSelection(0);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parentView) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("New Reminder");
        setContentView(R.layout.add_activity);
        findViews();
        db = TaskManager.db;

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radio0:
                        rl3.setVisibility(View.VISIBLE);
                        rl4.setVisibility(View.GONE);
                        break;
                    case R.id.radio1:
                        rl4.setVisibility(View.VISIBLE);
                        rl3.setVisibility(View.GONE);
                        break;
                }
            }
        });

        spinner1.setOnItemSelectedListener(spinnerListener);
        spinner2.setOnItemSelectedListener(spinnerListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mViewSwitcher", mViewSwitcher.getDisplayedChild());
        outState.putInt("hour", mTimePicker.getCurrentHour());
        outState.putCharSequence("fromdate", mStartDateTextView.getText());
        outState.putCharSequence("todate", mEndDateTextView.getText());
        outState.putCharSequence("attime", mTimeTextView.getText());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mViewSwitcher.setDisplayedChild(state.getInt("mViewSwitcher"));
        mTimePicker.setCurrentHour(state.getInt("hour"));
        mStartDateTextView.setText(state.getCharSequence("fromdate"));
        mEndDateTextView.setText(state.getCharSequence("todate"));
        mTimeTextView.setText(state.getCharSequence("attime"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sdf.applyPattern(TaskManager.getDateFormat());
    }

    private void findViews() {
        mMessageEditText = findViewById(R.id.msg_et);
        mSoundCheckBox = findViewById(R.id.sound_cb);
        mDatePicker = findViewById(R.id.datePicker);
        mTimePicker = findViewById(R.id.timePicker);
        mStartDateTextView = findViewById(R.id.fromdate_tv);
        mEndDateTextView = findViewById(R.id.todate_tv);
        mTimeTextView = findViewById(R.id.attime_tv);
        mViewSwitcher = findViewById(R.id.view_switcher);
        mRadioGroup = findViewById(R.id.radioGroup);
        rl3 = findViewById(R.id.relativeLayout3);
        rl4 = findViewById(R.id.relativeLayout4);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);

        minsEdit = findViewById(R.id.mins_et);
        hoursEdit = findViewById(R.id.hours_et);
        daysEdit = findViewById(R.id.days_et);
        monthsEdit = findViewById(R.id.months_et);
        yearsEdit = findViewById(R.id.years_et);
    }

    private boolean validate() {
        if (TextUtils.isEmpty(mMessageEditText.getText())) {
            mMessageEditText.requestFocus();
            Toast.makeText(this, "Enter a message", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mViewSwitcher.getDisplayedChild() == 1) {
            if (TextUtils.isEmpty(mStartDateTextView.getText())) {
                Toast.makeText(this, "Specify from date", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (TextUtils.isEmpty(mEndDateTextView.getText())) {
                Toast.makeText(this, "Specify to date", Toast.LENGTH_SHORT).show();
                return false;
            }
            try {
                if (sdf.parse(mStartDateTextView.getText().toString()).after(sdf.parse(mEndDateTextView.getText().toString()))) {
                    Toast.makeText(this, "From date is after To date!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } catch (ParseException e) {}
            if (TextUtils.isEmpty(mTimeTextView.getText())) {
                Toast.makeText(this, "Specify time", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    /* Save */
    public void create(View v) {
        if (!validate()) return;

        Alarm alarm = new Alarm();
        alarm.setName(mMessageEditText.getText().toString());
        alarm.setSound(mSoundCheckBox.isChecked());
        AlarmTime alarmTime = new AlarmTime();
        long alarmId = 0;

        switch(mViewSwitcher.getDisplayedChild()) {
            case 0: //one time
                alarm.setFromDate(DbHelper.getDateStr(mDatePicker.getYear(), mDatePicker.getMonth()+1, mDatePicker.getDayOfMonth()));
                alarmId = alarm.persist(db);

                alarmTime.setAt(DbHelper.getTimeStr(mTimePicker.getCurrentHour(), mTimePicker.getCurrentMinute()));
                alarmTime.setAlarmId(alarmId);
                alarmTime.persist(db);
                break;

            case 1: //repeating
                alarm.setFromDate(CommonUtils.toPersistentDate(mStartDateTextView.getText().toString(), sdf));
                alarm.setToDate(CommonUtils.toPersistentDate(mEndDateTextView.getText().toString(), sdf));
                switch(mRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.radio0: //rule
                        alarm.setRule(CommonUtils.concat(spinner1.getSelectedItemPosition(), " ",
                                spinner2.getSelectedItemPosition(), " ",
                                spinner3.getSelectedItemPosition()));
                        break;
                    case R.id.radio1: //interval
                        alarm.setInterval(CommonUtils.concat(minsEdit.getText(), " ",
                                hoursEdit.getText(), " ",
                                daysEdit.getText(), " ",
                                monthsEdit.getText(), " ",
                                yearsEdit.getText()));
                        break;
                }
                alarmId = alarm.persist(db);

                alarmTime.setAt(CommonUtils.toPersistentTime(mTimeTextView.getText().toString()));
                alarmTime.setAlarmId(alarmId);
                alarmTime.persist(db);
                break;
        }

        Intent service = new Intent(this, AlarmService.class);
        service.putExtra(AlarmMsg.COL_ALARMID, String.valueOf(alarmId));
        service.setAction(AlarmService.POPULATE);
        startService(service);

        finish();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggleButton1:
                mViewSwitcher.showNext();
                break;

            case R.id.fromdate_tv:
            case R.id.fromdate_lb:
                showDialog(DIALOG_FROMDATE);
                break;

            case R.id.todate_tv:
            case R.id.todate_lb:
                showDialog(DIALOG_TODATE);
                break;

            case R.id.attime_tv:
            case R.id.attime_lb:
                showDialog(DIALOG_ATTIME);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        Calendar cal = Calendar.getInstance();
        switch(id) {
            case DIALOG_ATTIME:
                TimePickerDialog.OnTimeSetListener mTimeSetListener =
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                mTimeTextView.setText(CommonUtils.getActualTime(hourOfDay, minute));
                            }
                        };
                return new TimePickerDialog(this, mTimeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), TaskManager.is24Hours());

            case DIALOG_FROMDATE:
            case DIALOG_TODATE:
                DatePickerDialog.OnDateSetListener dateListener =
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String txt = DbHelper.getDateStr(year, monthOfYear+1, dayOfMonth);
                                try {
                                    txt = sdf.format(DbHelper.sdf.parse(txt));
                                } catch (ParseException e) {}

                                if (id == DIALOG_FROMDATE) {
                                    mStartDateTextView.setText(txt);
                                } else if (id == DIALOG_TODATE) {
                                    mEndDateTextView.setText(txt);
                                }
                            }
                        };
                return new DatePickerDialog(this, dateListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        }

        return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);

        switch(id) {
            case DIALOG_ATTIME:
                if (!TextUtils.isEmpty(mTimeTextView.getText())) {
                    String[] arr = CommonUtils.toPersistentTime(mTimeTextView.getText().toString()).split(":");
                    ((TimePickerDialog)dialog).updateTime(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]));
                }
                break;

            case DIALOG_FROMDATE:
                if (!TextUtils.isEmpty(mStartDateTextView.getText())) {
                    String[] arr = CommonUtils.toPersistentDate(mStartDateTextView.getText().toString(), sdf).split("-");
                    ((DatePickerDialog)dialog).updateDate(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])-1, Integer.parseInt(arr[2]));
                }
                break;

            case DIALOG_TODATE:
                if (!TextUtils.isEmpty(mEndDateTextView.getText())) {
                    String[] arr = CommonUtils.toPersistentDate(mEndDateTextView.getText().toString(), sdf).split("-");
                    ((DatePickerDialog)dialog).updateDate(Integer.parseInt(arr[0]), Integer.parseInt(arr[1])-1, Integer.parseInt(arr[2]));
                }
                break;
        }
    }

}

package com.example.home.taskmanager.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.TaskManager;
import com.example.home.taskmanager.listadapter.AlarmViewAdapter;
import com.example.home.taskmanager.listeners.AlarmClickListener;
import com.example.home.taskmanager.model.AlarmModel;
import com.example.home.taskmanager.service.AlarmService;
import com.example.home.taskmanager.util.CommonUtils;
import com.example.home.taskmanager.util.CountDownTimer;
import com.example.home.taskmanager.util.RecyclerItemClickListener;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener, CalendarView.OnDateChangeListener{

    private TextView mRangeText;
    private ImageButton mSettingButton, mAddAlarmButton, mLeftButton, mRightButton;

    private LinearLayout alarmLayout;

    private ProgressBar progressBar;

    private RecyclerView mRecycleView;

    private CalendarView mCalendarView;

    private AlarmViewAdapter alarmViewAdapter;

    private List<AlarmModel> alarms;

    private Context mContext;

    public Calendar mCalendar;
    public Date mDate;
    private String[] mMonthList;

    private CountDownTimer countDownTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alarm_activity);

        mContext = this;

        initVar();

        initUI();
        setupActions();
    }

    private void initVar(){
        mDate = new Date();
        mCalendar = Calendar.getInstance();
        mMonthList = getResources().getStringArray(R.array.spinner3_arr);
    }

    private void initUI() {
        mRangeText = findViewById(R.id.range_tv);
        mSettingButton = findViewById(R.id.settings_ib);
        mAddAlarmButton = findViewById(R.id.add_alarm_ib);
        mLeftButton = findViewById(R.id.left_ib);
        mRightButton = findViewById(R.id.right_ib);
        mRecycleView = findViewById(R.id.recycleview);
        mCalendarView = findViewById(R.id.simpleCalendarView);
        alarmLayout = findViewById(R.id.add_alarm_layout);
        progressBar = findViewById(R.id.progress_dialog);
    }

    private void setupActions(){
        mSettingButton.setOnClickListener(this);
        mAddAlarmButton.setOnClickListener(this);
        mLeftButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
        mCalendarView.setOnDateChangeListener(this);

        mRecycleView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecycleView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(),AddAlarmActivity.class);
                intent.putExtra(CommonUtils.ALARM_MODEL_ID,alarms.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                showDialog(alarms.get(position));
            }
        }));
    }

    private void populateList(long beginTime, long endTime) {

        Select select = Select.from(AlarmModel.class)
                .where(Condition.prop("time").gt(beginTime), Condition
                        .prop("time").lt(endTime));

        alarms = select.list();

        alarmViewAdapter = new AlarmViewAdapter(alarms,mContext);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(llm);

        mRecycleView.setAdapter(alarmViewAdapter);

        alarmViewAdapter.updateList(alarms);
    }

    private String getRangeStr() {
        int date = mCalendar.get(Calendar.DATE);
        int month = mCalendar.get(Calendar.MONTH);
        int year = mCalendar.get(Calendar.YEAR);

        switch (TaskManager.getDateRange()) {
            case 0: // Daily
                if (date == Calendar.DATE && month == Calendar.MONTH && year == Calendar.YEAR + 1900)
                    return "Today";
                else return date + " " + mMonthList[month + 1];

            case 1: // Weekly
                return date + " " + mMonthList[month + 1] + setupCalendarView(+1) + " - " + mCalendar.get(Calendar.DATE) + " " + mMonthList[mCalendar.get(Calendar.MONTH) + 1] + setupCalendarView(-1);

            case 2: // Monthly
                return mMonthList[month + 1] + " " + year;

            case 3: // Yearly
                return year + "";
        }
        return null;
    }

    private String setupCalendarView(int step) {
        switch (TaskManager.getDateRange()) {
            case 0:
                mCalendar.add(Calendar.DATE, step);
                break;
            case 1:
                mCalendar.add(Calendar.DATE, 7 * step);
                break;
            case 2:
                mCalendar.add(Calendar.MONTH, step);
                break;
            case 3:
                mCalendar.add(Calendar.YEAR, step);
                break;
        }

        return "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_ib:
                startActivity(new Intent(this, PreferencesActivity.class));
                break;
            case R.id.add_alarm_ib:
                startActivity(new Intent(this, AddAlarmActivity.class));
                break;
            case R.id.left_ib:
                setupCalendarView(-1);
                mRangeText.setText(getRangeStr());

                long beginTime = CommonUtils.atStartOfDay(mCalendar.getTime()).getTime();
                long endTime = CommonUtils.atEndOfDay(mCalendar.getTime()).getTime();
                populateList(beginTime,endTime);
                break;
            case R.id.right_ib:
                setupCalendarView(+1);
                mRangeText.setText(getRangeStr());

                beginTime = CommonUtils.atStartOfDay(mCalendar.getTime()).getTime();
                endTime = CommonUtils.atEndOfDay(mCalendar.getTime()).getTime();
                populateList(beginTime,endTime);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(getString(R.string.var_bundle_calendar), mCalendar.getTimeInMillis());
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        mCalendar.setTimeInMillis(state.getLong(getString(R.string.var_bundle_calendar)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        long beginTime = CommonUtils.atStartOfDay(mCalendar.getTime()).getTime();
        long endTime = CommonUtils.atEndOfDay(mCalendar.getTime()).getTime();
        populateList(beginTime,endTime);
        mRangeText.setText(getRangeStr());
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        mCalendar.set(year,month,dayOfMonth);
        long beginTime = CommonUtils.atStartOfDay(mCalendar.getTime()).getTime();
        long endTime = CommonUtils.atEndOfDay(mCalendar.getTime()).getTime();

        populateList(beginTime,endTime);
    }

    private void showDialog(final AlarmModel alarmModel){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alarm");
        alertDialog.setMessage("Delete alarm?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {

                        Intent service = new Intent(getApplicationContext(), AlarmService.class);
                        service.putExtra(CommonUtils.ALARM_MODEL_ID, alarmModel.getId());
                        service.setAction(AlarmService.CANCEL);
                        startService(service);

                        countDownTimer = new CountDownTimer(5000,1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                alarmLayout.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onFinish() {
                                long beginTime = CommonUtils.atStartOfDay(mCalendar.getTime()).getTime();
                                long endTime = CommonUtils.atEndOfDay(mCalendar.getTime()).getTime();
                                populateList(beginTime,endTime);

                                alarmLayout.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);

                                dialog.dismiss();
                            }
                        };

                        countDownTimer.start();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}



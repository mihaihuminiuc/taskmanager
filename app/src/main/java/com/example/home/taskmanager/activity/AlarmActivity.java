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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.TaskManager;
import com.example.home.taskmanager.listadapter.AlarmViewAdapter;
import com.example.home.taskmanager.listeners.AlarmClickListener;
import com.example.home.taskmanager.model.AlarmModel;
import com.example.home.taskmanager.service.AlarmService;
import com.example.home.taskmanager.util.CommonUtils;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener, CalendarView.OnDateChangeListener{

    private TextView mRangeText;
    private ImageButton mSettingButton, mAddAlarmButton, mLeftButton, mRightButton;

    private RecyclerView mRecycleView;

    private CalendarView mCalendarView;

    private AlarmViewAdapter alarmViewAdapter;

    private Context mContext;

    private AlarmClickListener mAlarmClickListener;

    public Calendar mCalendar;
    public Date mDate;
    private String[] mMonthList;

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
    }

    private void setupActions(){
        mSettingButton.setOnClickListener(this);
        mAddAlarmButton.setOnClickListener(this);
        mLeftButton.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
        mCalendarView.setOnDateChangeListener(this);
        //mAlarmClickListener.onItemClick();
    }

    private void populateList(long beginTime, long endTime) {

        Select select = Select.from(AlarmModel.class)
                .where(Condition.prop("time").gt(beginTime), Condition
                        .prop("time").lt(endTime));

        List<AlarmModel> alarms = select.list();

        alarmViewAdapter = new AlarmViewAdapter(alarms,mAlarmClickListener,mContext);

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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == android.R.id.list) {
            getMenuInflater().inflate(R.menu.context_menu, menu);
            menu.setHeaderTitle(R.string.alarm_activity_menu_title);
            menu.setHeaderIcon(R.drawable.ic_dialog_menu_generic);

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.menu_edit:

                showEditDialog();
                break;

            case R.id.menu_delete:

                Intent cancelThis = new Intent(this, AlarmService.class);
                //cancelThis.putExtra(AlarmMsg.COL_ID, String.valueOf(info.id));
                cancelThis.setAction(AlarmService.CANCEL);
                startService(cancelThis);
                break;

            case R.id.menu_delete_repeating:

                Intent cancelRepeating = new Intent(this, AlarmService.class);
                cancelRepeating.setAction(AlarmService.CANCEL);
                startService(cancelRepeating);
                break;
        }

        return true;
    }


    private void showEditDialog(){
        AlertDialog alertDialog =  new AlertDialog.Builder(this)
                .setTitle("Edit")
                .setView(getLayoutInflater().inflate(R.layout.edit_activity, null))
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog d = (Dialog) dialog;
                        EditText msgEdit = d.findViewById(R.id.msg_et);
                        CheckBox soundCb = d.findViewById(R.id.sound_cb);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .create();

        alertDialog.show();
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        mCalendar.set(year,month,dayOfMonth);
        long beginTime = CommonUtils.atStartOfDay(mCalendar.getTime()).getTime();
        long endTime = CommonUtils.atEndOfDay(mCalendar.getTime()).getTime();

        populateList(beginTime,endTime);
    }
}



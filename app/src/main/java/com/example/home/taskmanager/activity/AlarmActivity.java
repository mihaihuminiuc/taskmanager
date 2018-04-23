package com.example.home.taskmanager.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.fragments.AlarmListFragment;
import com.example.home.taskmanager.fragments.EditAlarmFragment;
import com.example.home.taskmanager.listeners.AlarmActivityListener;

public class AlarmActivity extends AppCompatActivity implements AlarmActivityListener {

    private Toolbar mToolbar;
    private FrameLayout mFrameLayout;
    private FragmentTransaction mFragmentTransaction;
    private AlarmListFragment alarmListFragment;
    private EditAlarmFragment editAlarmFragment;
    private final int PREFERENCES_ACTIVITY = 2;


    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.alarm_activity);
        initFragments();
        initUI();
    }

    private void initUI(){
        mToolbar = findViewById(R.id.toolbar);
        mFrameLayout = findViewById(R.id.frame_x);
        setSupportActionBar(mToolbar);

        commitFragment(alarmListFragment);
    }

    private void initFragments(){
        alarmListFragment = new AlarmListFragment();
        editAlarmFragment = new EditAlarmFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.menu_settings:
                    Intent intent = new Intent(getBaseContext(), Preferences.class);
                    startActivityForResult(intent, PREFERENCES_ACTIVITY);
                    return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void addAlarm() {
        commitFragment(editAlarmFragment);
    }

    @Override
    public void editAlarm(int id) {
        commitFragment(editAlarmFragment);
    }

    @Override
    public void deleteAlarm(int id) {

    }

    private void commitFragment(Fragment fragment){
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.frame_x, fragment);
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        mFragmentTransaction.commit();
    }
}



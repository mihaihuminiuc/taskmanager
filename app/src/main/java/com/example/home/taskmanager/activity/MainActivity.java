package com.example.home.taskmanager.activity;

import android.content.res.Configuration;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.fragments.AddAlarmFragment;
import com.example.home.taskmanager.fragments.AlarmFragment;
import com.example.home.taskmanager.fragments.SettingsFragment;
import com.example.home.taskmanager.listeners.AddAlarmListener;
import com.example.home.taskmanager.listeners.AlarmClickListener;
import com.example.home.taskmanager.model.AlarmModel;
import com.example.home.taskmanager.util.CommonUtils;

public class MainActivity extends BaseActivity implements AddAlarmListener, AlarmClickListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavDrawer = findViewById(R.id.nav_drawer);
        mDrawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // Setup drawer view
        setupDrawerContent(mNavDrawer);

        // Select TodayFragment on app start by default
        loadAlarmFragment();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeNavigationDrawer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (!closeNavigationDrawer()) {
            super.onBackPressed();
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        closeNavigationDrawer();
        switch(menuItem.getItemId()) {
            case R.id.menu_alarm_list:
                loadFragment(AlarmFragment.class, getResources().getString(R.string.alarm_list_title),null);
                break;
            case R.id.menu_add_alarm:
                loadFragment(AddAlarmFragment.class, getResources().getString(R.string.add_alarm_title),null);
                break;
            case R.id.menu_settings:
                loadFragment(SettingsFragment.class, getResources().getString(R.string.setting_title),null);
                break;
        }
    }

    private boolean closeNavigationDrawer() {
        boolean drawerIsOpen = mDrawerLayout.isDrawerOpen(GravityCompat.START);
        if (drawerIsOpen) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        return drawerIsOpen;
    }

    public void hideNavigationBar() {
        closeNavigationDrawer();
    }

    private void loadFragment(Class fragmentClass, CharSequence toolbarTitle, Bundle bundle) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(bundle!=null)
            fragment.setArguments(bundle);

        insertFragment(fragment);

        // Set action bar title
        setTitle(toolbarTitle);
    }

    private void loadAlarmFragment() {
        loadFragment(AlarmFragment.class, getResources().getString(R.string.alarm_list_title),null);
    }

    @Override
    public void onCreateAlarm() {
        loadFragment(AlarmFragment.class, getResources().getString(R.string.alarm_list_title),null);
    }

    @Override
    public void onItemClick(long alarmId) {

        Bundle bundle = new Bundle();

        bundle.putLong(CommonUtils.ALARM_MODEL_ID,alarmId);

        loadFragment(AddAlarmFragment.class, getResources().getString(R.string.add_alarm_title),bundle);
    }
}

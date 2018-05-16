package com.example.home.taskmanager.fragments;

import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceGroup;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.TaskManager;

public class SettingsFragment extends PreferenceFragmentCompat {
    public static final String KEY_PREF_CURRENCY = "pref_currency";

    private SharedPreferences.OnSharedPreferenceChangeListener mSharedPrefChangeListener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
                    updatePreference(key);
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        return super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.settings, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(mSharedPrefChangeListener);
        updatePreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(mSharedPrefChangeListener);
    }

    private void updatePreferences() {
        updatePreference(TaskManager.TIME_FORMAT);
        updatePreference(TaskManager.VIBRATE_PREF);
        updatePreference(TaskManager.RINGTONE_PREF);
        updatePreference(TaskManager.RING_TIME);
    }

    private void updatePreference(String key){
        Preference pref = findPreference(key);

        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());
            return;
        }

        if (TaskManager.RINGTONE_PREF.equals(key)) {
            Uri ringtoneUri = Uri.parse(TaskManager.getRingtone());
            Ringtone ringtone = RingtoneManager.getRingtone(getContext(), ringtoneUri);
            if (ringtone != null) pref.setSummary(ringtone.getTitle(getContext()));
        }
    }
}

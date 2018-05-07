/**************************************************************************
 *
 * Copyright (C) 2012-2015 Alex Taradov <alex@taradov.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *************************************************************************/

package com.example.home.taskmanager.activity;
 
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.style.TabStopSpan;

import com.example.home.taskmanager.R;
import com.example.home.taskmanager.TaskManager;

public class PreferencesActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.settings);
  }

  @Override
  protected void onResume(){
    super.onResume();
    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    updatePreferences();
  }

  @Override
  protected void onPause() {
    super.onPause();
    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    updatePreference(key);
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
      Ringtone ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
      if (ringtone != null) pref.setSummary(ringtone.getTitle(this));
    }
  }
}

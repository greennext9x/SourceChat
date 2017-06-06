/*
 * Copyright (C) 2007-2008 Esmertec AG. Copyright (C) 2007-2008 The Android Open
 * Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.awesomeapp.messenger.ui.legacy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import org.awesomeapp.messenger.ImApp;
import org.awesomeapp.messenger.Preferences;
import org.awesomeapp.messenger.util.Languages;

import java.util.ArrayList;

import im.zom.messenger.R;
import info.guardianproject.panic.Panic;
import info.guardianproject.panic.PanicResponder;

public class SettingActivity extends PreferenceActivity {
    private static final String TAG = "SettingActivity";

    private static final int CHOOSE_RINGTONE = 5;

    private PackageManager pm;
    private String currentLanguage;
    ListPreference mOtrMode;
    ListPreference mLanguage;
//    CheckBoxPreference mLinkifyOnTor;
    CheckBoxPreference mHideOfflineContacts;
    CheckBoxPreference mEnableNotification;
    CheckBoxPreference mNotificationVibrate;
    CheckBoxPreference mNotificationSound;

    private void setInitialValues() {
        mOtrMode.setValue(Preferences.getOtrMode());

//        mLinkifyOnTor.setChecked(Preferences.getLinkifyOnTor());
        mEnableNotification.setChecked(Preferences.isNotificationEnabled());
        mNotificationVibrate.setChecked(Preferences.getNotificationVibrate());
        mNotificationSound.setChecked(Preferences.getNotificationSound());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        pm = getPackageManager();

        mOtrMode = (ListPreference) findPreference("pref_security_otr_mode");
        mOtrMode.setEntries(Preferences.getOtrModeNames());
        mOtrMode.setEntryValues(Preferences.getOtrModeValues());
        mOtrMode.setDefaultValue(Preferences.DEFAULT_OTR_MODE);
        mLanguage = (ListPreference) findPreference("pref_language");
//        mLinkifyOnTor = (CheckBoxPreference) findPreference("pref_linkify_on_tor");
        mHideOfflineContacts = (CheckBoxPreference) findPreference("pref_hide_offline_contacts");
        mEnableNotification = (CheckBoxPreference) findPreference("pref_enable_notification");
        mNotificationVibrate = (CheckBoxPreference) findPreference("pref_notification_vibrate");
        mNotificationSound = (CheckBoxPreference) findPreference("pref_notification_sound");
    }
    @Override
    protected void onResume() {
        super.onResume();
        setInitialValues();
    }
}

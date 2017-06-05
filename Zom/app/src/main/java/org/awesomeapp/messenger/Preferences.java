package org.awesomeapp.messenger;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.awesomeapp.messenger.util.Languages;

import im.zom.messenger.R;


/**
 * Handles all global preferences that do not need to be stored encrypted,
 * looking after the names of preferences, default values and caching. Needs
 * to be setup in {@link ImApp} using
 * {@link Preferences#setup(android.content.Context)} before it is used.
 */
public class Preferences {

    public static final String TAG = "Preferences";

    /* start encryption modes for OTR */
    public static final String OTR_MODE_FORCE = "force";
    public static final String OTR_MODE_AUTO = "auto";
    public static final String OTR_MODE_REQUESTED = "requested";
    public static final String OTR_MODE_DISABLED = "disabled";
    /**
     * Has the same order as {@link #getOtrModeNames()}
     */
    public static final String[] OTR_MODE_VALUES = {
            OTR_MODE_FORCE,
            OTR_MODE_AUTO,
            OTR_MODE_REQUESTED,
            OTR_MODE_DISABLED
    };
    public static final String DEFAULT_OTR_MODE = OTR_MODE_AUTO;
    public static final String DEFAULT_NOTIFICATION_RINGTONE_URI = "content://settings/system/notification_sound";
    public static final boolean DEFAULT_LINKIFY_ON_TOR = false;
    public static final boolean DEFAULT_NOTIFICATION = true;
    public static final boolean DEFAULT_NOTIFICATION_SOUND = true;
    public static final boolean DEFAULT_NOTIFICATION_VIBRATE = true;

    private static final String LINKIFY_ON_TOR = "pref_linkify_on_tor";
    private static final String NOTIFICATION = "pref_enable_notification";
    private static final String NOTIFICATION_SOUND = "pref_notification_sound";
    private static final String NOTIFICATION_VIBRATE = "pref_notification_vibrate";
    private static final String NOTIFICATION_RINGTONE_URI = "pref_notification_ringtone";
    private static final String OTR_MODE = "pref_security_otr_mode";

    private static Context context;
    private static SharedPreferences preferences;
    private static Preferences instance;

    private Preferences(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setup(Context context) {
        if (instance != null) {
            final String error = "Attempted to reinitialize preferences after it " +
                    "has already been initialized in ImApp";
            throw new RuntimeException(error);
        }
        instance = new Preferences(context);
    }

    public static boolean getLinkifyOnTor() {
        return preferences.getBoolean(LINKIFY_ON_TOR, DEFAULT_LINKIFY_ON_TOR);
    }

    public static void setLinkifyOnTor(boolean linkify) {
        preferences.edit().putBoolean(LINKIFY_ON_TOR, linkify).apply();
    }

    public static boolean isNotificationEnabled() {
        return preferences.getBoolean(NOTIFICATION, DEFAULT_NOTIFICATION);
    }

    public static void setNotification(boolean enable) {
        preferences.edit().putBoolean(NOTIFICATION, enable).apply();
    }

    public static boolean getNotificationSound() {
        return preferences.getBoolean(NOTIFICATION_SOUND, DEFAULT_NOTIFICATION_SOUND);
    }

    public static void setNotificationSound(boolean enable) {
        preferences.edit().putBoolean(NOTIFICATION_SOUND, enable).apply();
    }

    public static boolean getNotificationVibrate() {
        return preferences.getBoolean(NOTIFICATION_VIBRATE, DEFAULT_NOTIFICATION_VIBRATE);
    }

    public static void setNotificationVibrate(boolean enable) {
        preferences.edit().putBoolean(NOTIFICATION_VIBRATE, enable).apply();
    }
    public static Uri getNotificationRingtoneUri() {
        return Uri.parse(preferences.getString(NOTIFICATION_RINGTONE_URI, DEFAULT_NOTIFICATION_RINGTONE_URI));
    }

    public static void setNotificationRingtoneUri(String uriString) {
        if (TextUtils.isEmpty(uriString)) {
            preferences.edit().putString(NOTIFICATION_RINGTONE_URI, DEFAULT_NOTIFICATION_RINGTONE_URI).apply();
        } else {
            preferences.edit().putString(NOTIFICATION_RINGTONE_URI, uriString).apply();
        }
    }

    public static void setNotificationRingtone(Uri uri) {
        preferences.edit().putString(NOTIFICATION_RINGTONE_URI, uri.toString()).apply();
    }

    public static String getOtrMode() {
        return preferences.getString(OTR_MODE, DEFAULT_OTR_MODE);
    }

    public static void setOtrMode(String otrMode) {
        preferences.edit().putString(OTR_MODE, otrMode).commit();
    }

    /**
     * Has the same order as {@link #getOtrModeNames()}
     */
    public static String[] getOtrModeValues() {
        return OTR_MODE_VALUES;
    }

    /**
     * Has the same order as {@link #OTR_MODE_VALUES}
     */
    public static String[] getOtrModeNames() {
        final String names[] = new String[4];
        names[0] = context.getString(R.string.otr_mode_force);
        names[1] = context.getString(R.string.otr_mode_auto);
        names[2] = context.getString(R.string.otr_mode_requested);
        names[3] = context.getString(R.string.otr_mode_disabled);
        return names;
    }
}

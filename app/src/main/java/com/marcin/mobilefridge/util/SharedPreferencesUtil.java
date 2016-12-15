package com.marcin.mobilefridge.util;

import android.content.SharedPreferences;

/**
 * Created by Marcin on 15.12.2016.
 */
public class SharedPreferencesUtil {

    public static String LOGIN_PREFERENCES_PATH = "login";

    public static String O_AUTH_KEY = "OAuth";

    private SharedPreferences preferences;

    public SharedPreferencesUtil(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void saveData(String pathToSave, String valueToSave) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(pathToSave, valueToSave);
        preferencesEditor.apply();
    }

    public void clearAllDataForUser() {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.remove(LOGIN_PREFERENCES_PATH);
        preferencesEditor.remove(O_AUTH_KEY);
        preferencesEditor.apply();
    }

    public String restoreData(String pathToSave) {
        return preferences.getString(pathToSave, "");
    }
}

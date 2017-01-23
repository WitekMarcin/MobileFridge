package com.marcin.mobilefridge.util;

import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marcin on 15.12.2016.
 */
public class SharedPreferencesUtil {

    public static String LOGIN_PREFERENCES_PATH = "login";

    public static String O_AUTH_KEY = "OAuth";

    public static String SHARED_PREFERENCES_FILE_PATH = "com.mobileFridge.SharedPreferencesFile";

    private static Map<String, String> data = new HashMap<>();

    private SharedPreferences preferences;

    public SharedPreferencesUtil(SharedPreferences preferences) {
        this.preferences = preferences;
    }


    public void saveData(String pathToSave, String valueToSave) {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putString(pathToSave, valueToSave);
        preferencesEditor.apply();
        data.put(pathToSave, valueToSave);
    }

    public void clearAllDataForUser() {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.remove(LOGIN_PREFERENCES_PATH);
        preferencesEditor.remove(O_AUTH_KEY);
        preferencesEditor.apply();
        data.clear();
    }

    public String restoreData(String pathToSave) {
        return preferences.getString(pathToSave, "").length() > 2 ?
                preferences.getString(pathToSave, "") : data.get(pathToSave);
    }

}

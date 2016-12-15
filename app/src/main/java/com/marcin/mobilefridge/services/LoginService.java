package com.marcin.mobilefridge.services;

import android.content.SharedPreferences;
import com.marcin.mobilefridge.util.RestConnectionUtil;
import com.marcin.mobilefridge.util.SharedPreferencesService;

import java.io.IOException;

/**
 * Created by Marcin on 15.12.2016.
 */
public class LoginService {

    private RestConnectionUtil restConnectionUtil;
    private SharedPreferencesService sharedPreferencesService;

    public LoginService(SharedPreferences preferences) {
        restConnectionUtil = new RestConnectionUtil();
        sharedPreferencesService = new SharedPreferencesService(preferences);
    }

    public void logIn(String username, String password) throws Exception {
        String oauthValue = restConnectionUtil.tryToLogInAndReturnOauthKey(username, password);
        sharedPreferencesService.saveData(SharedPreferencesService.LOGIN_PREFERENCES_PATH, username);
        sharedPreferencesService.saveData(SharedPreferencesService.O_AUTH_KEY, oauthValue);
    }

    public void logOut() {
        sharedPreferencesService.clearAllDataForUser();
    }
}

package com.marcin.mobilefridge.services;

import android.content.SharedPreferences;
import com.marcin.mobilefridge.util.SharedPreferencesUtil;

/**
 * Created by Marcin on 15.12.2016.
 */
public class LoginService {

    private RestConnectionManagerService restConnectionManagerService;
    private SharedPreferencesUtil sharedPreferencesUtil;

    public LoginService(SharedPreferences preferences) {
        restConnectionManagerService = new RestConnectionManagerService();
        sharedPreferencesUtil = new SharedPreferencesUtil(preferences);
    }

    public void logIn(String username, String password) throws Exception {
        String oauthValue = restConnectionManagerService.tryToLogInAndReturnOauthKey(username, password);
        sharedPreferencesUtil.saveData(SharedPreferencesUtil.LOGIN_PREFERENCES_PATH, username);
        sharedPreferencesUtil.saveData(SharedPreferencesUtil.O_AUTH_KEY, oauthValue);
    }

    public void logOut() {
        sharedPreferencesUtil.clearAllDataForUser();
    }
}

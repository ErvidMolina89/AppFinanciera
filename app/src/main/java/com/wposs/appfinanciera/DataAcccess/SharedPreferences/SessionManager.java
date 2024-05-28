package com.wposs.appfinanciera.DataAcccess.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.wposs.appfinanciera.Models.UserModel;

public class SessionManager {
    private static final String PREF_NAME = "SessionPref";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER_ID = "userId";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    public void createLoginSession(double amount, String name, int userId) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(KEY_NAME, name);
        editor.putFloat(KEY_AMOUNT, (float) amount);
        editor.putInt(KEY_USER_ID, userId);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public UserModel getUserDetails() {
        UserModel user = new UserModel();
        user.setId(pref.getInt(KEY_USER_ID, 0));
        user.setName(pref.getString(KEY_NAME, null));
        user.setAmount(pref.getFloat(KEY_AMOUNT, 0));
        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}

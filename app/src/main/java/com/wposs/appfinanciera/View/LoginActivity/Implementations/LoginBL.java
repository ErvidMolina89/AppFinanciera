package com.wposs.appfinanciera.View.LoginActivity.Implementations;

import android.content.Context;

import com.wposs.appfinanciera.DataAcccess.DBSqlite.DatabaseHelper;
import com.wposs.appfinanciera.DataAcccess.SharedPreferences.SessionManager;
import com.wposs.appfinanciera.Models.UserModel;
import com.wposs.appfinanciera.View.LoginActivity.Interfaces.ILoginBL;
import com.wposs.appfinanciera.View.LoginActivity.Interfaces.LoginListener;

public class LoginBL implements ILoginBL {
    private DatabaseHelper db;
    private LoginListener listener;
    private SessionManager sessionManager;

    public LoginBL(LoginListener listener, Context context){
        this.listener = listener;
        this.db = new DatabaseHelper(context);
        this.sessionManager = new SessionManager(context);
    }

    @Override
    public void login(String phone, String password) {
        UserModel user = db.loginUser(phone, password);
        if (user != null) {
            sessionManager.createLoginSession(user.getAmount(), user.getName(), user.getId());
            listener.showLoginSuccess();
        } else {
            listener.showLoginError("Credenciales incorrectas");
        }
    }
}

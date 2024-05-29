package com.wposs.appfinanciera.View.RegisterActivity.Implementations;

import android.content.Context;

import com.wposs.appfinanciera.DataAcccess.DBSqlite.DatabaseHelper;
import com.wposs.appfinanciera.Models.UserModel;
import com.wposs.appfinanciera.Utils.PhoneNumberExistsException;
import com.wposs.appfinanciera.View.RegisterActivity.Interfaces.IRegisterBL;
import com.wposs.appfinanciera.View.RegisterActivity.Interfaces.RegisterListener;

public class RegisterBL implements IRegisterBL {
    private final DatabaseHelper db;
    private final RegisterListener listener;

    public RegisterBL(RegisterListener listener, Context context){
        this.listener = listener;
        this.db = new DatabaseHelper(context);
    }

    @Override
    public void registerUser(UserModel user) {
        try {
            boolean isRegistered = db.registerUser(user);
            if (isRegistered) {
                listener.showRegisterSuccess();
            } else {
                listener.showRegisterError("Error al registrar usuario");
            }
        } catch (PhoneNumberExistsException e) {
            listener.showRegisterError(e.getMessage());
        }
    }
}

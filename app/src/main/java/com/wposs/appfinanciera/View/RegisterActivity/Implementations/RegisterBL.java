package com.wposs.appfinanciera.View.RegisterActivity.Implementations;

import android.content.Context;

import com.wposs.appfinanciera.DataAcccess.DBSqlite.DatabaseHelper;
import com.wposs.appfinanciera.Models.UserModel;
import com.wposs.appfinanciera.View.RegisterActivity.Interfaces.IRegisterBL;
import com.wposs.appfinanciera.View.RegisterActivity.Interfaces.RegisterListener;

public class RegisterBL implements IRegisterBL {
    private DatabaseHelper db;
    private RegisterListener listener;

    public RegisterBL(RegisterListener listener, Context context){
        this.listener = listener;
        this.db = new DatabaseHelper(context);
    }

    @Override
    public void registerUser(UserModel user) {
        if (db.registerUser(user)) { listener.showRegisterSuccess();
        } else { listener.showRegisterError("Error al registrar usuario");
        }
    }
}

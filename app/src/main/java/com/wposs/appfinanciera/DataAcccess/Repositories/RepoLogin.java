package com.wposs.appfinanciera.DataAcccess.Repositories;

import android.content.Context;

import com.wposs.appfinanciera.Models.UserModel;

public class RepoLogin {

    private Context context;

    public RepoLogin(Context context) {
        this.context = context;
    }

    public <T extends IRepository> void logIn(UserModel user, T responder) {

    }

    public <T extends IRepository> void registerUser(UserModel user, T responder) {

    }

}

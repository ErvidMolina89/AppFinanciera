package com.wposs.appfinanciera.View.RegisterActivity.Interfaces;

import com.wposs.appfinanciera.Models.UserModel;

public interface IRegisterPresenter {
    void registerUser(UserModel user, String confirmPass);
}

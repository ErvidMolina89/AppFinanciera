package com.wposs.appfinanciera.View.RegisterActivity.Implementations;

import android.content.Context;
import com.wposs.appfinanciera.Models.UserModel;
import com.wposs.appfinanciera.View.RegisterActivity.Interfaces.IRegisterPresenter;
import com.wposs.appfinanciera.View.RegisterActivity.Interfaces.RegisterListener;
import com.wposs.appfinanciera.View.RegisterActivity.Interfaces.RegisterView;

public class RegisterPresenter implements IRegisterPresenter {

    private RegisterView view;
    private RegisterBL registerBL;

    public RegisterPresenter(RegisterView view, Context context) {
        this.view = view;
        this.registerBL = new RegisterBL(new listenerRegisterListener(), context);
    }

    @Override
    public void registerUser(UserModel user, String confirmPass) {
        if (!user.getPassword().equals(confirmPass)){
            view.showRegisterError("Las contraseñas no coinciden.");
        }else if (!user.getName().isEmpty() && !user.getEmail().isEmpty()
                && !user.getPassword().isEmpty() && !user.getPhone().isEmpty()){
        registerBL.registerUser(user);
        }else {
            view.showRegisterError("Falta por diligenciar algunos campos: password, name, phone y email");
        }
    }

    private class listenerRegisterListener implements RegisterListener {
        @Override
        public void showRegisterSuccess() {
            view.showRegisterSuccess();
        }

        @Override
        public void showRegisterError(String message) {
            view.showRegisterError(message);
        }
    }
}

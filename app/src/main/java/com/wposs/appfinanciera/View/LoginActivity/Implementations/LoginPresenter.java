package com.wposs.appfinanciera.View.LoginActivity.Implementations;

import android.content.Context;

import com.wposs.appfinanciera.View.LoginActivity.Interfaces.ILoginBL;
import com.wposs.appfinanciera.View.LoginActivity.Interfaces.ILoginPresenter;
import com.wposs.appfinanciera.View.LoginActivity.Interfaces.LoginListener;
import com.wposs.appfinanciera.View.LoginActivity.Interfaces.LoginView;

public class LoginPresenter implements ILoginPresenter {
    private LoginView view;
    private ILoginBL loginBL;

    public LoginPresenter(LoginView view, Context context) {
        this.view = view;
        this.loginBL = new LoginBL(new listenerLoginListener(), context);
    }

    public void login(String phone, String password) {
        // Lógica para validar el login
        if (!phone.isEmpty() && !password.isEmpty()) { // Ejemplo
            loginBL.login(phone, password);
        } else {
            view.showLoginError("Credenciales incorrectas");
        }
    }

    private class listenerLoginListener implements LoginListener {
        @Override
        public void showLoginSuccess() {
            view.showLoginSuccess();
        }

        @Override
        public void showLoginError(String message) {
            view.showLoginError("Credenciales incorrectas");
        }
    }
}

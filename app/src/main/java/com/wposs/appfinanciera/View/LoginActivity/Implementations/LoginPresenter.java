package com.wposs.appfinanciera.View.LoginActivity.Implementations;

import android.content.Context;
import com.wposs.appfinanciera.R;
import com.wposs.appfinanciera.View.LoginActivity.Interfaces.ILoginBL;
import com.wposs.appfinanciera.View.LoginActivity.Interfaces.ILoginPresenter;
import com.wposs.appfinanciera.View.LoginActivity.Interfaces.LoginListener;
import com.wposs.appfinanciera.View.LoginActivity.Interfaces.LoginView;

public class LoginPresenter implements ILoginPresenter {
    private final LoginView view;
    private final ILoginBL loginBL;
    private final Context context;

    public LoginPresenter(LoginView view, Context context) {
        this.view = view;
        this.context = context;
        this.loginBL = new LoginBL(new listenerLoginListener(), context);
    }

    public void login(String phone, String password) {
        if (!phone.isEmpty() && !password.isEmpty()) {
            loginBL.login(phone, password);
        } else {
            view.showLoginError(context.getString(R.string.login_vacio));
        }
    }

    private class listenerLoginListener implements LoginListener {
        @Override
        public void showLoginSuccess() {
            view.showLoginSuccess();
        }

        @Override
        public void showLoginError(String message) {
            view.showLoginError(context.getString(R.string.credenciales_incorrectas));
        }
    }
}

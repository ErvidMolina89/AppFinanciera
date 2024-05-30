package com.wposs.appfinanciera.View.LoginActivity.Implementations;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.wposs.appfinanciera.Base.App;
import com.wposs.appfinanciera.View.LoginActivity.Interfaces.LoginView;
import com.wposs.appfinanciera.View.HomeActivity.Implementations.MainActivity;
import com.wposs.appfinanciera.R;
import com.wposs.appfinanciera.View.RegisterActivity.Implementations.RegisterActivity;

public class LoginActivity extends App {

    private LoginPresenter presenter;
    private EditText phoneEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(new listenerLoginView(), this);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        TextView newAccount = findViewById(R.id.text_new_account);
        Button loginButton = findViewById(R.id.button_Longin);

        loginButton.setOnClickListener(view -> {
            String phone = phoneEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            presenter.login(phone, password);
        });

        newAccount.setOnClickListener(v -> changeActivity(RegisterActivity.class));
    }

    private class listenerLoginView implements LoginView {

        @Override
        public void showLoginSuccess() {
            changeActivity(MainActivity.class);
            finish();
        }

        @Override
        public void showLoginError(String message) {
            showErrorDialog(message);
            System.out.println(message);
        }
    }
}
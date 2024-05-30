package com.wposs.appfinanciera.View.RegisterActivity.Implementations;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.wposs.appfinanciera.Base.App;
import com.wposs.appfinanciera.Models.UserModel;
import com.wposs.appfinanciera.Utils.PasswordValidator;
import com.wposs.appfinanciera.R;
import com.wposs.appfinanciera.View.LoginActivity.Implementations.LoginActivity;
import com.wposs.appfinanciera.View.RegisterActivity.Interfaces.RegisterView;

public class RegisterActivity extends App {
    private RegisterPresenter presenter;
    private ImageView imageViewAccount;
    private TextInputLayout textFieldEmail, textFieldPhone, textFieldPass, textFieldCedula, textFieldConfirmPass;
    private EditText nameEditText, emailEditText, phoneEditText, cedulaEditText, passEditText, confirmPassEditText;
    private static final double INITIAL_BALANCE = 3000000.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        presenter = new RegisterPresenter(new listenerView(), this);
        textFieldEmail = findViewById(R.id.textField_email);
        textFieldPhone = findViewById(R.id.textField_phone);
        textFieldPass = findViewById(R.id.textField_pass);
        textFieldConfirmPass = findViewById(R.id.textField_confirm_pass);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.createPhoneEditText);
        cedulaEditText = findViewById(R.id.cedulaEditText);
        textFieldCedula = findViewById(R.id.textField_id);
        passEditText = findViewById(R.id.createPassEditText);
        confirmPassEditText = findViewById(R.id.confirmPassEditText);
        imageViewAccount = findViewById(R.id.imageViewReturnAccount);

        onClickRegisterButton();
        validateEmailFormat();
        validatePhoneNumberCharacters();
        validateSizePassCharacters();
        validateIdentificationNumberCharacters();
        imageViewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void onClickRegisterButton(){
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(view -> {
            UserModel user = new UserModel();
            user.setName(nameEditText.getText().toString());
            user.setEmail(emailEditText.getText().toString());
            user.setPhone(phoneEditText.getText().toString());
            user.setCedula(cedulaEditText.getText().toString());
            user.setPassword(passEditText.getText().toString());
            user.setAmount(INITIAL_BALANCE);
            presenter.registerUser(user, confirmPassEditText.getText().toString());
        });
    }

    private void validateEmailFormat(){
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String email = s.toString().trim();
                if (isValidEmail(email)) {
                    textFieldEmail.setError(null);
                } else {
                    textFieldEmail.setError(getString(R.string.error_correo_formato));
                }
            }
        });
    }

    private void validateSizePassCharacters(){
        passEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword(textFieldPass);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        confirmPassEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!passEditText.getText().toString().equals(s.toString())){
                    textFieldConfirmPass.setError(getString(R.string.error_pass_coinciden));
                }else {
                    textFieldConfirmPass.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void validatePassword(TextInputLayout textInputLayout) {
        String errorMessage = PasswordValidator.validatePassword(passEditText.getText().toString());
        textInputLayout.setError(errorMessage);
    }

    private void validatePhoneNumberCharacters(){
        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 10) {
                    textFieldPhone.setError(getString(R.string.insuficiente_caracteres));
                } else {
                    textFieldPhone.setError(null);
                }
            }
        });
    }

    private void validateIdentificationNumberCharacters(){
        cedulaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!(s.length()> 7 && s.length() <= 10)) {
                    textFieldCedula.setError(getString(R.string.insuficiente_caracteres));
                } else {
                    textFieldCedula.setError(null); // Elimina el mensaje de error
                }
            }
        });
    }

    private class listenerView implements RegisterView {
        @Override
        public void showRegisterSuccess() {
            Toast.makeText(getApplicationContext(), getString(R.string.realizaste_un_registro_exitoso), Toast.LENGTH_SHORT).show();
            changeActivity(LoginActivity.class);
        }

        @Override
        public void showRegisterError(String message) {
            showErrorDialog(message);
        }
    }
}

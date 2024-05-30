package com.wposs.appfinanciera.View.TransfersActivity.Implementations;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wposs.appfinanciera.Base.App;
import com.wposs.appfinanciera.DataAcccess.SharedPreferences.SessionManager;
import com.wposs.appfinanciera.Models.Transaction;
import com.wposs.appfinanciera.R;
import com.wposs.appfinanciera.View.HomeActivity.Implementations.MainActivity;
import com.wposs.appfinanciera.View.TransfersActivity.Interfaces.ITransferView;
import java.util.List;
import java.util.Objects;

public class TransferActivity extends App {
    private TransferPresenter presenter;
    private Spinner spnReceivers;
    private TextInputEditText etAmountTransfer, etMess;
    private TextInputLayout tfAmountTransfer;
    private TextView amountAvailableTextView;
    private Button btnSend;
    private Double amountDeposit = 0.0;
    Transaction transaction;
    SessionManager sessionManager;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transfers);
        sessionManager = new SessionManager(getApplicationContext());

        spnReceivers = findViewById(R.id.spnPhoneTransfer);
        etAmountTransfer = findViewById(R.id.etAmountTransfer);
        tfAmountTransfer = findViewById(R.id.textField_AmountTransfer);
        etMess = findViewById(R.id.etMess);
        amountAvailableTextView = findViewById(R.id.amountAvailableTextView);
        btnSend = findViewById(R.id.btnSend);
        ImageView imageViewReturn = findViewById(R.id.imageViewReturnTransfer);

        transaction = new Transaction();

        presenter = new TransferPresenter(new listenerTransferPresenter(), getApplicationContext());
        presenter.getAllPhoneNumbers();
        presenter.getAmountForUserId(sessionManager.getUserDetails().getId());

        listenerButtonSent();
        btnSend.setEnabled(false);
        btnSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGrayText)));
        validateAmountIsLessOrEqualToDeposit();
        imageViewReturn.setOnClickListener(v -> onBackPressed());
    }

    private void validateAmountIsLessOrEqualToDeposit(){
        etAmountTransfer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();
                if(input.isEmpty() || input.equals("0") || Double.parseDouble(input) <= 500 ) {
                    tfAmountTransfer.setError(getString(R.string.error_monto_500));
                }else {
                    btnSend.setEnabled((Double.parseDouble(input) <= amountDeposit));
                    btnSend.setBackgroundTintList(ColorStateList.valueOf((Double.parseDouble(input) <= amountDeposit) ? getResources().getColor(R.color.colorAccent):getResources().getColor(R.color.colorGrayText)));
                    tfAmountTransfer.setError(!(Double.parseDouble(input) <= amountDeposit) ? getString(R.string.cantidad_insuficiente) : null);
                }
            }
        });
    }

    private void listenerButtonSent(){
        btnSend.setOnClickListener(v -> {
             String amount = Objects.requireNonNull(etAmountTransfer.getText()).toString();
            transaction.setAmount(Double.parseDouble(amount.startsWith("0")? amount.replace("^0+", ""):amount));
            transaction.setDescription(Objects.requireNonNull(etMess.getText()).toString());
            transaction.setUserId(sessionManager.getUserDetails().getId());
            presenter.sendTransfer(transaction);
        });
    }

    private void setListPhoneUser(List<String> numbers){
        numbers.remove(sessionManager.getUserDetails().getPhone());
        // Crear el adaptador
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnReceivers.setAdapter(adapter);
        // Configurar el listener
        spnReceivers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                transaction.setFromUserPhone(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private class listenerTransferPresenter implements ITransferView {

        @Override
        public void showTransferSuccess() {
            changeActivity(MainActivity.class);
        }

        @Override
        public void showAmountForUserId(double amount) {
            amountDeposit = amount;
            amountAvailableTextView.setText(String.valueOf(amount));
        }

        @Override
        public void showAllPhoneNumbers(List<String> numbers) {
            setListPhoneUser(numbers);
        }

        @Override
        public void showTransferError(String message) {
            showErrorDialog(message);
        }
    }
}
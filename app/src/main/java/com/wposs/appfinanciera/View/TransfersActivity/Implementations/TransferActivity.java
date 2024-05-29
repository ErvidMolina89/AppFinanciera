package com.wposs.appfinanciera.View.TransfersActivity.Implementations;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wposs.appfinanciera.Base.App;
import com.wposs.appfinanciera.DataAcccess.SharedPreferences.SessionManager;
import com.wposs.appfinanciera.Models.Transaction;
import com.wposs.appfinanciera.R;
import com.wposs.appfinanciera.View.HomeActivity.Implementations.MainActivity;
import com.wposs.appfinanciera.View.TransfersActivity.Interfaces.ITransferView;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;

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

        transaction = new Transaction();

        presenter = new TransferPresenter(new listenerTransferPresenter(), getApplicationContext());
        presenter.getAllPhoneNumbers();
        presenter.getAmountForUserId(sessionManager.getUserDetails().getId());

        listenerButtonSent();
        btnSend.setEnabled(false);
        validateAmountIsLessOrEqualToDeposit();
    }

    private void validateAmountIsLessOrEqualToDeposit(){
        etAmountTransfer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                btnSend.setEnabled((Double.parseDouble(s.toString()) <= amountDeposit));
                tfAmountTransfer.setError(!(Double.parseDouble(s.toString()) <= amountDeposit)? "No cuenta con la cantidad suficiente": null);
            }
        });
    }

    private void listenerButtonSent(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaction.setAmount(Double.parseDouble(etAmountTransfer.getText().toString()));
                transaction.setDescription(etMess.getText().toString());
                transaction.setUserId(sessionManager.getUserDetails().getId());
                presenter.sendTransfer(transaction);
            }
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
                // Mostrar el elemento seleccionado
                Toast.makeText(getApplicationContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                transaction.setPhoneTransaction(selectedItem);
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
package com.wposs.appfinanciera.View.MovementDetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.wposs.appfinanciera.Base.App;
import com.wposs.appfinanciera.Models.Transaction;
import com.wposs.appfinanciera.R;

public class MovementDetailActivity extends App {
    private TextView userTextView, sentToTextView, amountTextView, dateTextView, descriptionTextView, phoneToTextView;
    private ImageView imageViewDetail;
    private Transaction transaction;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_detail);

        userTextView = findViewById(R.id.userDetailTextView);
        imageViewDetail = findViewById(R.id.imageViewDetail);
        amountTextView = findViewById(R.id.amountTextView);
        sentToTextView = findViewById(R.id.sentToTextView);
        amountTextView = findViewById(R.id.amountTextView);
        dateTextView = findViewById(R.id.dateDetailTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        phoneToTextView = findViewById(R.id.phoneToTextView);

        // Obtener el Intent que inició la actividad
        Intent intent = getIntent();

        // Recuperar el objeto Transaction
        transaction = intent.getParcelableExtra("transaction");
        completeTransactionData();
    }

    private void completeTransactionData(){
        if (transaction != null) {
            userTextView.setText(transaction.getType() == 1 ? "Traslado Dinero" : "Resección Dinero");
            userTextView.setTextColor(transaction.getType() == 1 ? getResources().getColor(R.color.red) : getResources().getColor(R.color.colorAccent));
            imageViewDetail.setColorFilter(transaction.getType() == 1 ? getResources().getColor(R.color.red) : getResources().getColor(R.color.colorAccent));
            sentToTextView.setText(String.valueOf(transaction.getNameUserTransaction()));
            phoneToTextView.setText(String.valueOf(transaction.getPhoneTransaction()));
            amountTextView.setText(String.valueOf(transaction.getAmount()));
            dateTextView.setText(transaction.getDate());
            descriptionTextView.setText(transaction.getDescription());
        }else showErrorDialog("Error al vizualizar información");
    }
}
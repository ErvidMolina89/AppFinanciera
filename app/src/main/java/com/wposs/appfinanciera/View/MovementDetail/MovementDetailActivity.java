package com.wposs.appfinanciera.View.MovementDetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.wposs.appfinanciera.Base.App;
import com.wposs.appfinanciera.Models.Transaction;
import com.wposs.appfinanciera.R;
import com.wposs.appfinanciera.Utils.DateFormatType;
import com.wposs.appfinanciera.Utils.DateUtils;

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
        ImageView imageViewReturn = findViewById(R.id.imageViewReturnDeatil);
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
        imageViewReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void completeTransactionData(){
        if (transaction != null) {
            userTextView.setText(transaction.getType() == 1 ? getString(R.string.traslado_dinero) : getString(R.string.resecci_n_dinero));
            userTextView.setTextColor(transaction.getType() == 1 ? getResources().getColor(R.color.red) : getResources().getColor(R.color.colorAccent));
            imageViewDetail.setColorFilter(transaction.getType() == 1 ? getResources().getColor(R.color.red) : getResources().getColor(R.color.colorAccent));
            sentToTextView.setText((transaction.getType() == 1)?transaction.getFromUserName():transaction.getToUserName());
            phoneToTextView.setText((transaction.getType() == 1)?transaction.getFromUserPhone(): transaction.getToUserPhone());
            amountTextView.setText(String.valueOf(transaction.getAmount()));
            dateTextView.setText(DateUtils.formatDateString(transaction.getDate(), DateFormatType.FORMAT_2));
            descriptionTextView.setText(transaction.getDescription());
        }else showErrorDialog(getString(R.string.error_al_vizualizar_informacion));
    }
}
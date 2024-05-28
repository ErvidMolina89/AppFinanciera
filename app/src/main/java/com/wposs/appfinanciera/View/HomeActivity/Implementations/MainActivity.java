package com.wposs.appfinanciera.View.HomeActivity.Implementations;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wposs.appfinanciera.Base.App;
import com.wposs.appfinanciera.DataAcccess.SharedPreferences.SessionManager;
import com.wposs.appfinanciera.Models.Transaction;
import com.wposs.appfinanciera.Models.UserModel;
import com.wposs.appfinanciera.R;
import com.wposs.appfinanciera.View.HomeActivity.Interfaces.ITransationAdapterListener;
import com.wposs.appfinanciera.View.LoginActivity.Implementations.LoginActivity;
import com.wposs.appfinanciera.View.MovementDetail.MovementDetailActivity;

import java.util.ArrayList;

public class MainActivity extends App {
    private SessionManager sessionManager;
    private RecyclerView transactionsRecyclerView;
    private TransactionsAdapter transactionsAdapter;
    private ArrayList<Transaction> transactionList;
    private static final double INITIAL_BALANCE = 3000000.0;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView accountBalanceTextView = findViewById(R.id.accountBalanceTextView);
        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // valido la sesión
        sessionManager = new SessionManager(getApplicationContext());
        if (!sessionManager.isLoggedIn()) changeActivity(LoginActivity.class);
        UserModel user = sessionManager.getUserDetails();
        // Establecer el título y el icono
        if (getSupportActionBar() != null) {getSupportActionBar().setTitle("Bienvenido: " + user.getName());}

        // Set initial account balance
        accountBalanceTextView.setText(String.format("Balance: $%,.2f", INITIAL_BALANCE));

        callRecyclerView();
        loadDummyTransactions();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadDummyTransactions() {
        // Add some dummy transactions to the list
        transactionList.add(new Transaction("Compra en supermercado", 50000, "29/05/2024 10:30:00", 1));
        transactionList.add(new Transaction("Pago de servicios", 200000, "29/05/2024 12:30:00",1));
        transactionList.add(new Transaction("Depósito", 100000, "29/05/2024 10:40:00",2));

        transactionsAdapter.updateList(transactionList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú; esto agrega elementos a la barra de acción si está presente.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar clics en los elementos del menú de la barra de acción
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            // Manejar la acción de logout aquí
            Toast.makeText(this, "Logout seleccionado", Toast.LENGTH_SHORT).show();
            sessionManager.logout();
            changeActivity(LoginActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callRecyclerView(){
        transactionList = new ArrayList<>();
        transactionsRecyclerView.setAdapter(null);
        transactionsRecyclerView.setHasFixedSize(true);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        transactionsAdapter = new TransactionsAdapter(transactionList, new listenerAdapter(), getApplicationContext());
        transactionsRecyclerView.setAdapter(transactionsAdapter);
    }

    private class listenerAdapter implements ITransationAdapterListener {

        @Override
        public void onPressTransaction(Transaction transaction) {
            Toast.makeText(getApplicationContext(), "Transacción seleccionada: "+transaction.getAmount(), Toast.LENGTH_SHORT).show();
            changeActivityPutExtra(MovementDetailActivity.class, transaction);
        }
    }
}
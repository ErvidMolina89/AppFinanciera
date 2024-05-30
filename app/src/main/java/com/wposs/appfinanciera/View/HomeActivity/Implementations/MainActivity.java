package com.wposs.appfinanciera.View.HomeActivity.Implementations;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wposs.appfinanciera.Base.App;
import com.wposs.appfinanciera.DataAcccess.SharedPreferences.SessionManager;
import com.wposs.appfinanciera.Models.Transaction;
import com.wposs.appfinanciera.Models.UserModel;
import com.wposs.appfinanciera.R;
import com.wposs.appfinanciera.View.HomeActivity.Interfaces.IMainView;
import com.wposs.appfinanciera.View.HomeActivity.Interfaces.ITransationAdapterListener;
import com.wposs.appfinanciera.View.LoginActivity.Implementations.LoginActivity;
import com.wposs.appfinanciera.View.MovementDetail.MovementDetailActivity;
import com.wposs.appfinanciera.View.TransfersActivity.Implementations.TransferActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends App {
    private SessionManager sessionManager;
    private RecyclerView transactionsRecyclerView;
    private TransactionsAdapter transactionsAdapter;
    private FloatingActionButton fab;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transactionsRecyclerView = findViewById(R.id.transactionsRecyclerView);
        fab = findViewById(R.id.floating_action_button);
        MainPresenter presenter = new MainPresenter(new listenerMainView(), getApplicationContext());

        sessionManager = new SessionManager(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displaySesion();
        callRecyclerView();
        callFloatinActionButton();
        presenter.getAllTransferSuccess(sessionManager.getUserDetails().getId());
    }

    @SuppressLint("DefaultLocale")
    private void displaySesion(){
        TextView accountBalanceTextView = findViewById(R.id.accountBalanceTextView);
        // valido la sesión
        if (!sessionManager.isLoggedIn()) changeActivity(LoginActivity.class);
        UserModel user = sessionManager.getUserDetails();
        // Establecer el título y el icono
        if (getSupportActionBar() != null) {getSupportActionBar().setTitle("Bienvenido: " + user.getName());}

        // Set initial account balance
        accountBalanceTextView.setText(String.format("Balance: $%,.2f", user.getAmount()));
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
        transactionsRecyclerView.setAdapter(null);
        transactionsRecyclerView.setHasFixedSize(true);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        transactionsAdapter = new TransactionsAdapter(new ArrayList<>(), new listenerAdapter(), getApplicationContext());
        transactionsRecyclerView.setAdapter(transactionsAdapter);
    }

    private void callFloatinActionButton(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(TransferActivity.class);
            }
        });
    }

    private class listenerMainView implements IMainView {

        @Override
        public void showGetAllTransferSuccess(List<Transaction> transactions) {
            transactionsAdapter.updateList(transactions);
        }

        @Override
        public void showMainError(String message) {
            showErrorDialog(message);
        }
    }

    private class listenerAdapter implements ITransationAdapterListener {

        @Override
        public void onPressTransaction(Transaction transaction) {
            Toast.makeText(getApplicationContext(), "Transacción seleccionada: "+transaction.getAmount(), Toast.LENGTH_SHORT).show();
            changeActivityPutExtra(MovementDetailActivity.class, transaction);
        }
    }
}
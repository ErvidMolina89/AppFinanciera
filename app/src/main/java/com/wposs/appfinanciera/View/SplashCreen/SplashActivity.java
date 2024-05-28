package com.wposs.appfinanciera.View.SplashCreen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.wposs.appfinanciera.Base.App;
import com.wposs.appfinanciera.DataAcccess.DBSqlite.DatabaseHelper;
import com.wposs.appfinanciera.DataAcccess.SharedPreferences.SessionManager;
import com.wposs.appfinanciera.View.HomeActivity.Implementations.MainActivity;
import com.wposs.appfinanciera.View.LoginActivity.Implementations.LoginActivity;
import com.wposs.appfinanciera.R;
import android.os.Handler;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends App {
    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try (DatabaseHelper db = new DatabaseHelper(getApplicationContext())) {
            db.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            // Manejo de excepciones
        }


        // Cambiar a LoginActivity después de los segundos establecidos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // valido la sesión del usuario
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                if (sessionManager.isLoggedIn()) {
                    changeActivity(MainActivity.class);
                } else {
                    changeActivity(LoginActivity.class);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

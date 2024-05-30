package com.wposs.appfinanciera.View.HomeActivity.Implementations;

import android.content.Context;

import com.wposs.appfinanciera.DataAcccess.DBSqlite.DatabaseHelper;
import com.wposs.appfinanciera.View.HomeActivity.Interfaces.IMainBL;
import com.wposs.appfinanciera.View.HomeActivity.Interfaces.IMainListener;

import java.util.List;

public class MainBL implements IMainBL {
    private final DatabaseHelper db;
    private final IMainListener listener;
    public MainBL(IMainListener listener, Context context) {
        this.listener = listener;
        this.db = new DatabaseHelper(context);
    }

    @Override
    public void getAllTransferSuccess(int userId) {
        listener.showGetAllTransferSuccess(db.getUserTransactions(userId));
    }
}

package com.wposs.appfinanciera.View.TransfersActivity.Implementations;

import android.content.Context;

import com.wposs.appfinanciera.DataAcccess.DBSqlite.DatabaseHelper;
import com.wposs.appfinanciera.Models.Transaction;
import com.wposs.appfinanciera.Models.UserModel;
import com.wposs.appfinanciera.View.TransfersActivity.Interfaces.ITransferBL;
import com.wposs.appfinanciera.View.TransfersActivity.Interfaces.ITransferListener;

public class TransferBL implements ITransferBL {
    private final DatabaseHelper db;
    private final ITransferListener listener;

    public TransferBL(ITransferListener listener, Context context) {
        this.listener = listener;
        this.db = new DatabaseHelper(context);
    }

    @Override
    public void getAllPhoneNumbers() {
        listener.showAllPhoneNumbers(db.getAllPhoneNumbers());
    }
    @Override
    public void getAmountForUserId(int userId) {
        listener.showAmountForUserId(db.getUserAmount(userId));
    }

    @Override
    public void sendTransfer(Transaction transaction) {
        if (db.sendMoney(transaction.getUserId(),
                transaction.getPhoneTransaction(),
                transaction.getAmount(),
                transaction.getDescription())) {
            listener.showTransferSuccess();
        } else {
            listener.showTransferError("No se puedo realizar la Transferencia");
        }
    }
}

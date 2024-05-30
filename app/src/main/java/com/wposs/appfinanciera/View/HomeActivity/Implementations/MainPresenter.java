package com.wposs.appfinanciera.View.HomeActivity.Implementations;

import android.content.Context;

import com.wposs.appfinanciera.Models.Transaction;
import com.wposs.appfinanciera.View.HomeActivity.Interfaces.IMainListener;
import com.wposs.appfinanciera.View.HomeActivity.Interfaces.IMainPresenter;
import com.wposs.appfinanciera.View.HomeActivity.Interfaces.IMainView;

import java.util.List;

public class MainPresenter implements IMainPresenter {
    private final IMainView view;
    private final MainBL bl;

    public MainPresenter(IMainView view, Context context) {
        this.view = view;
        this.bl = new MainBL(new listenerMainBL(), context);
    }

    @Override
    public void getAllTransferSuccess(int userId) {
        bl.getAllTransferSuccess(userId);
    }

    private class listenerMainBL implements IMainListener {

        @Override
        public void showGetAllTransferSuccess(List<Transaction> transactions) {
            view.showGetAllTransferSuccess(transactions);
        }

        @Override
        public void showMainError(String message) {
            view.showMainError(message);
        }
    }
}

package com.wposs.appfinanciera.View.TransfersActivity.Implementations;

import android.content.Context;

import com.wposs.appfinanciera.Models.Transaction;
import com.wposs.appfinanciera.View.TransfersActivity.Interfaces.ITransferListener;
import com.wposs.appfinanciera.View.TransfersActivity.Interfaces.ITransferPresenter;
import com.wposs.appfinanciera.View.TransfersActivity.Interfaces.ITransferView;

import java.util.List;

public class TransferPresenter implements ITransferPresenter {
    private final ITransferView view;
    private final TransferBL bl;

    public TransferPresenter(ITransferView view, Context context) {
        this.view = view;
        this.bl = new TransferBL(new listenerTransferBL(), context);
    }

    @Override
    public void getAllPhoneNumbers() {
        bl.getAllPhoneNumbers();
    }

    @Override
    public void getAmountForUserId(int userId) {
        bl.getAmountForUserId(userId);
    }

    @Override
    public void sendTransfer(Transaction transaction) {
        bl.sendTransfer(transaction);
    }
    private class listenerTransferBL implements ITransferListener {

        @Override
        public void showTransferSuccess() {
            view.showTransferSuccess();
        }

        @Override
        public void showAmountForUserId(double amount) {
            view.showAmountForUserId(amount);
        }

        @Override
        public void showAllPhoneNumbers(List<String> numbers) {
            view.showAllPhoneNumbers(numbers);
        }

        @Override
        public void showTransferError(String message) {
            view.showTransferError(message);
        }
    }
}

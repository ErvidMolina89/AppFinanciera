package com.wposs.appfinanciera.View.HomeActivity.Interfaces;

import com.wposs.appfinanciera.Models.Transaction;

import java.util.List;

public interface IMainView {
    void showGetAllTransferSuccess(List<Transaction> transactions);
    void showMainError(String message);
}

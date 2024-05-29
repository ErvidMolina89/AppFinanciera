package com.wposs.appfinanciera.View.TransfersActivity.Interfaces;

import com.wposs.appfinanciera.Models.Transaction;

public interface ITransferBL {
    void getAllPhoneNumbers();
    void getAmountForUserId(int userId);
    void sendTransfer(Transaction transaction);
}

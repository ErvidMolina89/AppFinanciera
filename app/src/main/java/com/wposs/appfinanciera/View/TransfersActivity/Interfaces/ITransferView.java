package com.wposs.appfinanciera.View.TransfersActivity.Interfaces;

import java.util.List;

public interface ITransferView {
    void showTransferSuccess();
    void showAmountForUserId(double amount);
    void showAllPhoneNumbers(List<String> numbers);
    void showTransferError(String message);
}

package com.wposs.appfinanciera.DataAcccess.Repositories;

public interface IRepository {
    void onSuccessResponse(Object objectResponse);
    void onFailedResponse(String message);
}

package com.wposs.appfinanciera.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Transaction implements Parcelable {
    private int id;
    private int type;
    private String description;
    private double amount;
    private String date;
    private int userId;
    private int fromUserId;
    private String fromUserName;
    private String fromUserPhone;
    private String toUserName;
    private String toUserPhone;

    public Transaction() {}

    // Métodos Parcelable
    protected Transaction(Parcel in) {
        id = in.readInt();
        type = in.readInt();
        description = in.readString();
        amount = in.readDouble();
        date = in.readString();
        userId = in.readInt();
        fromUserId = in.readInt();
        fromUserName = in.readString();
        fromUserPhone = in.readString();
        toUserName = in.readString();
        toUserPhone = in.readString();
    }
    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(type);
        dest.writeString(description);
        dest.writeDouble(amount);
        dest.writeString(date);
        dest.writeInt(userId);
        dest.writeInt(fromUserId);
        dest.writeString(fromUserName);
        dest.writeString(fromUserPhone);
        dest.writeString(toUserName);
        dest.writeString(toUserPhone);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getFromUserPhone() {
        return fromUserPhone;
    }

    public void setFromUserPhone(String fromUserPhone) {
        this.fromUserPhone = fromUserPhone;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getToUserPhone() {
        return toUserPhone;
    }

    public void setToUserPhone(String toUserPhone) {
        this.toUserPhone = toUserPhone;
    }
}

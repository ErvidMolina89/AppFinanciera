package com.wposs.appfinanciera.DataAcccess.DBSqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wposs.appfinanciera.Models.Transaction;
import com.wposs.appfinanciera.Models.UserModel;

public class DatabaseHelper extends SQLiteOpenHelper implements AutoCloseable {
    private static final String DATABASE_NAME = "dbFinance.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_CEDULA = "cedula";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_AMOUNT = "amount";


    // Transactions table
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COLUMN_TRANSACTION_ID = "id";
    private static final String COLUMN_TRANSACTION_TYPE = "type";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_VALUE = "value";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_USER_ID_FK = "user_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        // Create Users table
        createTableUsers(db);
        // Create Transactions table
        createTableTransactions(db);
    }

    private void createTableUsers(SQLiteDatabase db){
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_CEDULA + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_AMOUNT + " REAL)";
        db.execSQL(CREATE_USERS_TABLE);

        // Insert default users with balance
        insertDefaultUsers(db);
    }

    private void createTableTransactions(SQLiteDatabase db){
        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TRANSACTION_TYPE + " INTEGER, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_VALUE + " REAL, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_USER_ID_FK + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    private void insertDefaultUsers(SQLiteDatabase db) {
        UserModel user = new UserModel();
        user.setName("Thiago Molina");
        user.setEmail("Thiago@gmail.com");
        user.setPhone("3000000001");
        user.setCedula("1234567890");
        user.setPassword("pass123");
        user.setAmount(3000000.0);
        insertUser(db, user);
        user = new UserModel();
        user.setName("Maira Molina");
        user.setEmail("Maira@gmail.com");
        user.setPhone("3000000002");
        user.setCedula("1234567891");
        user.setPassword("pass123");
        user.setAmount(3000000.0);
        insertUser(db, user);
        user = new UserModel();
        user.setName("Jaime Cuba");
        user.setEmail("jaime_cuba@gmail.com");
        user.setPhone("3000000003");
        user.setCedula("1234567892");
        user.setPassword("pass123");
        user.setAmount(3000000.0);
        insertUser(db, user);
    }

    private void insertUser(SQLiteDatabase db, UserModel user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, user.getName());
        values.put(COLUMN_EMAIL,user.getEmail());
        values.put(COLUMN_PHONE, user.getPhone());
        values.put(COLUMN_CEDULA, user.getCedula());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_AMOUNT, user.getAmount());  // Insert the double value
        db.insert(TABLE_USERS, null, values);
    }

    //registrar Usuario
    public boolean registerUser(UserModel user) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, user.getName());
            values.put(COLUMN_EMAIL, user.getEmail());
            values.put(COLUMN_PHONE, user.getPhone());
            values.put(COLUMN_CEDULA, user.getCedula());
            values.put(COLUMN_PASSWORD, user.getPassword());
            values.put(COLUMN_AMOUNT, user.getAmount());

            long result = db.insert(TABLE_USERS, null, values);
            return result != -1;
        }
    }

    @SuppressLint("Range")
    public UserModel loginUser(String phone, String password) {
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = db.query(TABLE_USERS, null, COLUMN_PHONE + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{phone, password}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                UserModel user = new UserModel();
                user.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                user.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
                user.setCedula(cursor.getString(cursor.getColumnIndex(COLUMN_CEDULA)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                user.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)));
                cursor.close();
                return user;
            } else {
                return null;
            }
        }
    }

    // Insert a transaction
    public boolean insertTransaction(Transaction transaction) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TRANSACTION_TYPE, transaction.getType());
            values.put(COLUMN_DESCRIPTION, transaction.getDescription());
            values.put(COLUMN_VALUE, transaction.getAmount());
            values.put(COLUMN_DATE, transaction.getDate());
            values.put(COLUMN_USER_ID_FK, transaction.getUserId());

            long result = db.insert(TABLE_TRANSACTIONS, null, values);
            return result != -1;
        }
    }

    @Override
    public synchronized void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}

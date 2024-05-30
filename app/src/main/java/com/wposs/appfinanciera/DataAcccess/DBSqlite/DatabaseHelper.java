package com.wposs.appfinanciera.DataAcccess.DBSqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wposs.appfinanciera.DataAcccess.SharedPreferences.SessionManager;
import com.wposs.appfinanciera.Models.Transaction;
import com.wposs.appfinanciera.Models.UserModel;
import com.wposs.appfinanciera.Utils.PhoneNumberExistsException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_VALUE = "value";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_USER_ID_FK = "user_id";
    private static final String COLUMN_FROM_USER_ID = "from_user_id";
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_VALUE + " REAL, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_FROM_USER_ID + " INTEGER, " +
                COLUMN_USER_ID_FK + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_FROM_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")," +
                "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                ")";
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
    public boolean registerUser(UserModel user) throws PhoneNumberExistsException {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            if (isPhoneNumberExists(db, user.getPhone())) {
                throw new PhoneNumberExistsException("El número de teléfono ya está registrado.");
            }

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
    private boolean isPhoneNumberExists(SQLiteDatabase db, String phoneNumber) {
        String query = "SELECT 1 FROM " + TABLE_USERS + " WHERE " + COLUMN_PHONE + " = ?";
        try (Cursor cursor = db.rawQuery(query, new String[]{phoneNumber})) {
            return cursor.moveToFirst(); // Si se mueve al primer resultado, el teléfono existe
        }
    }
    public List<String> getAllPhoneNumbers() {
        List<String> phoneNumbers = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PHONE + " FROM " + TABLE_USERS, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));
                phoneNumbers.add(phoneNumber);
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return phoneNumbers;
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
    @SuppressLint("Range")
    public double getUserAmount(int loggedInUserId) {
        double amount = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_AMOUNT + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID + " = ?", new String[]{String.valueOf(loggedInUserId)});

        if (cursor != null && cursor.moveToFirst()) {
            amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
            cursor.close();
        }

        db.close();
        return amount;
    }
    public boolean sendMoney(int userId, String toPhone, double amount, String mess) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Obtener datos del remitente
            Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_AMOUNT}, COLUMN_USER_ID + "=?",
                    new String[]{String.valueOf(userId)}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                @SuppressLint("Range") double fromBalance = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                cursor.close();

                // Obtener datos del receptor
                cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID, COLUMN_AMOUNT, COLUMN_NAME, COLUMN_PHONE}, COLUMN_PHONE + "=?",
                        new String[]{toPhone}, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    @SuppressLint("Range") int toUserId = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                    @SuppressLint("Range") double toBalance = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                    cursor.close();

                    // Realizar la transferencia
                    fromBalance -= amount;
                    toBalance += amount;

                    sesionAmount(fromBalance);

                    updateAmountUsers(db,fromBalance, userId);
                    updateAmountUsers(db,toBalance, toUserId);
                    boolean result = insertTransaction(db, mess, amount, dateTransaction(), userId, toUserId);

                    // Commit transaction
                    db.setTransactionSuccessful();
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
        return false;
    }
    private void updateAmountUsers(SQLiteDatabase db, Double amountUpdate, int userId){
        ContentValues toValues = new ContentValues();
        toValues.put(COLUMN_AMOUNT, amountUpdate);
        db.update(TABLE_USERS, toValues, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
    }
    private void sesionAmount(Double amount){
        SessionManager sessionManager = new SessionManager(context);
        sessionManager.saveUserAmount(amount);
    }
    private String dateTransaction(){
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(currentTime));
    }
    public boolean insertTransaction(SQLiteDatabase db, String description, double amount, String date, int userId, int fromUserId) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_VALUE, amount);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_USER_ID_FK, userId);
        values.put(COLUMN_FROM_USER_ID, fromUserId);

        long result = db.insert(TABLE_TRANSACTIONS, null, values);
        return result != -1;
    }
    public List<Transaction> getUserTransactions(int userId) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = querySQLTransactions();

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId), String.valueOf(userId), String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                transactionList.add(fillTransactionModel(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return transactionList;
    }
    private String querySQLTransactions(){
        return "SELECT " +
                "t.id, " +
                "t.description, " +
                "t.value, " +
                "t.date, " +
                "t.from_user_id, " +
                "t.user_id, " +
                "u_from.name AS from_user_name, " +
                "u_from.phone AS from_user_phone, " +
                "u_to.name AS to_user_name, " +
                "u_to.phone AS to_user_phone, " +
                "CASE " +
                "WHEN t.from_user_id = ? THEN 2 " +
                "ELSE 1 " +
                "END AS type " +
                "FROM transactions t " +
                "LEFT JOIN users u_from ON t.from_user_id = u_from.id " +
                "LEFT JOIN users u_to ON t.user_id = u_to.id " +
                "WHERE t.from_user_id = ? OR t.user_id = ? " +
                "ORDER BY t.date DESC";
    }
    @SuppressLint("Range")
    private Transaction fillTransactionModel(Cursor cursor){
        Transaction transaction = new Transaction();
        transaction.setId(cursor.getInt(cursor.getColumnIndex("id")));
        transaction.setDescription(cursor.getString(cursor.getColumnIndex("description")));
        transaction.setAmount(cursor.getDouble(cursor.getColumnIndex("value")));
        transaction.setDate(cursor.getString(cursor.getColumnIndex("date")));
        transaction.setFromUserId(cursor.getInt(cursor.getColumnIndex("from_user_id")));
        transaction.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
        transaction.setFromUserName(cursor.getString(cursor.getColumnIndex("from_user_name")));
        transaction.setFromUserPhone(cursor.getString(cursor.getColumnIndex("from_user_phone")));
        transaction.setToUserName(cursor.getString(cursor.getColumnIndex("to_user_name")));
        transaction.setToUserPhone(cursor.getString(cursor.getColumnIndex("to_user_phone")));
        transaction.setType(cursor.getInt(cursor.getColumnIndex("type")));
        return transaction;
    }
    @Override
    public synchronized void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}

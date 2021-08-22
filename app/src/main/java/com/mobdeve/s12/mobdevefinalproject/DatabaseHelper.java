package com.mobdeve.s12.mobdevefinalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "PlanX.db";
    private static final int DB_VERSION = 1;

    // EXPENSE TABLE //
    private static final String EXPENSE_TABLE = "expense_table";
    private static final String EXPENSE_COLUMN_ID = "id";
    private static final String EXPENSE_COLUMN_NAME = "expense_name";
    private static final String EXPENSE_COLUMN_YEAR = "expense_year";
    private static final String EXPENSE_COLUMN_MONTH = "expense_month";
    private static final String EXPENSE_COLUMN_DAY = "expense_day";
    private static final String EXPENSE_COLUMN_AMOUNT = "expense_amount";
    private static final String EXPENSE_COLUMN_CATEGORY = "expense_category";
    private static final String EXPENSE_COLUMN_USERNAME = "username";

    // USER TABLE //
    private static final String USER_TABLE = "user_table";
    private static final String USER_COLUMN_ID = "id";
    private static final String USER_COLUMN_USERNAME = "username";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_EMAIL = "email";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create expense_table //
        String queryCreateExpenseTable =
                "CREATE TABLE " + EXPENSE_TABLE +
                        " (" + EXPENSE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        EXPENSE_COLUMN_NAME + " TEXT, " +
                        EXPENSE_COLUMN_YEAR + " TEXT, " +
                        EXPENSE_COLUMN_MONTH + " TEXT, " +
                        EXPENSE_COLUMN_DAY + " TEXT, " +
                        EXPENSE_COLUMN_AMOUNT + " DOUBLE, " +
                        EXPENSE_COLUMN_CATEGORY + " TEXT, " +
                        EXPENSE_COLUMN_USERNAME + " TEXT" +
                        ");";

        // Create user_table //
        String queryCreateUserTable =
                "CREATE TABLE " + USER_TABLE +
                        " (" + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        USER_COLUMN_USERNAME + " TEXT, " +
                        USER_COLUMN_PASSWORD + " TEXT, " +
                        USER_COLUMN_EMAIL + " TEXT" +
                        ");";

        // Execute queries //
        db.execSQL(queryCreateUserTable);
        db.execSQL(queryCreateExpenseTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);

        onCreate(db);

    }

    /* DATABASE FUNCTIONS */

    /* Add expense to expense_table */
    public void addExpense(String name, String year, String month, String day,
                           double amount, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(EXPENSE_COLUMN_NAME, name);
        cv.put(EXPENSE_COLUMN_YEAR, year);
        cv.put(EXPENSE_COLUMN_MONTH, month);
        cv.put(EXPENSE_COLUMN_DAY, day);
        cv.put(EXPENSE_COLUMN_AMOUNT, amount);
        cv.put(EXPENSE_COLUMN_CATEGORY, category);

        long result = db.insert(EXPENSE_TABLE, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed Upload :(", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Expense successfully added :)", Toast.LENGTH_SHORT).show();
        }
    }

    /* Add expense to expense_table of a user */
    public void addUserExpense(String name, String year, String month, String day,
                           double amount, String category, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(EXPENSE_COLUMN_NAME, name);
        cv.put(EXPENSE_COLUMN_YEAR, year);
        cv.put(EXPENSE_COLUMN_MONTH, month);
        cv.put(EXPENSE_COLUMN_DAY, day);
        cv.put(EXPENSE_COLUMN_AMOUNT, amount);
        cv.put(EXPENSE_COLUMN_CATEGORY, category);
        cv.put(EXPENSE_COLUMN_USERNAME, username);

        long result = db.insert(EXPENSE_TABLE, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed Upload :(", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Expense successfully added :)", Toast.LENGTH_SHORT).show();
        }
    }

    /* Get all contents of expense_table */
    public Cursor readAllExpenseTable() {
        String query = "SELECT * FROM " + EXPENSE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    /* Get all contents of expense_table of the selected user */
    public Cursor readAllUserExpenseTable(String username) {
        String query = "SELECT * FROM " + EXPENSE_TABLE + " WHERE " +
                EXPENSE_COLUMN_USERNAME + "=?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[] {username});
        }
        return cursor;
    }

    /* Get the sum of the amount column in expense_table */
    public Cursor getTotalExpenses() {
        String query = "SELECT SUM(" + EXPENSE_COLUMN_AMOUNT + ") " +
                "FROM " + EXPENSE_TABLE + " WHERE " + EXPENSE_COLUMN_AMOUNT +
                " < 0";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    /* Get the sum of the amount column in expense_table of selected user */
    public Cursor getUserTotalExpenses(String username) {
        String query = "SELECT SUM(" + EXPENSE_COLUMN_AMOUNT + ") " +
                "FROM " + EXPENSE_TABLE + " WHERE " + EXPENSE_COLUMN_AMOUNT +
                " < 0 AND " + EXPENSE_COLUMN_USERNAME + "=?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[] {username});
        }
        return cursor;
    }

    /* Add user to user_table */
    public void addUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USER_COLUMN_USERNAME, username);
        cv.put(USER_COLUMN_PASSWORD, password);
        cv.put(USER_COLUMN_EMAIL, email);

        long result = db.insert(USER_TABLE, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed register :(", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Register success!", Toast.LENGTH_SHORT).show();
        }
    }

    /* Function for login. Return true if username password match exists */
    public boolean loginUser(String username, String password) {
        String query = "SELECT * FROM " + USER_TABLE + " WHERE username=?" +
                " AND password=?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[] {username, password});
        }

        // Match Found //
        if (cursor.getCount() > 0) {
            return true;
        }

        // No Matches //
        Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show();
        return false;
    }

}

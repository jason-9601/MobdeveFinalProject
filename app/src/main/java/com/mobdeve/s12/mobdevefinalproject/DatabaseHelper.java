package com.mobdeve.s12.mobdevefinalproject;

import android.content.ContentValues;
import android.content.Context;
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

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Expense Table //
        String queryCreateExpenseTable =
                "CREATE TABLE " + EXPENSE_TABLE +
                        " (" + EXPENSE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        EXPENSE_COLUMN_NAME + " TEXT, " +
                        EXPENSE_COLUMN_YEAR + " TEXT, " +
                        EXPENSE_COLUMN_MONTH + " TEXT, " +
                        EXPENSE_COLUMN_DAY + " TEXT, " +
                        EXPENSE_COLUMN_AMOUNT + " DOUBLE, " +
                        EXPENSE_COLUMN_CATEGORY + " TEXT" +
                        ");";

        db.execSQL(queryCreateExpenseTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE);
        onCreate(db);

    }

    /* DATABASE FUNCTIONS */

    /* Add expense to EXPENSE_TABLE */
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

}

package com.mobdeve.s12.mobdevefinalproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "PlanX.db";
    private static final int DB_VERSION = 1;

    // USER TABLE //
    private static final String USER_TABLE = "user_table";
    private static final String USER_COLUMN_ID = "id";
    private static final String USER_COLUMN_USERNAME = "username";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_EMAIL = "email";

    // EXPENSE TABLE //
    private static final String EXPENSE_TABLE = "expense_table";
    private static final String EXPENSE_COLUMN_ID = "id";
    private static final String EXPENSE_COLUMN_USERNAME = "username";
    private static final String EXPENSE_COLUMN_NAME = "expense_name";
    private static final String EXPENSE_COLUMN_YEAR = "expense_year";
    private static final String EXPENSE_COLUMN_MONTH = "expense_month";
    private static final String EXPENSE_COLUMN_DAY = "expense_day";
    private static final String EXPENSE_COLUMN_AMOUNT = "expense_amount";
    private static final String EXPENSE_COLUMN_CATEGORY = "expense_category";
    private static final String EXPENSE_COLUMN_FULLDATE = "expense_fulldate";

    // NOTES TABLE //
    private static final String NOTES_TABLE = "notes_table";
    private static final String NOTES_ID = "id";
    private static final String NOTES_USERNAME = "username";
    private static final String NOTES_TEXT = "notes_text";
    private static final String NOTES_BACKGROUND_COLOR = "notes_background_color";
    private static final String NOTES_FONT_COLOR = "notes_font_color";

    // TO DO TABLE //
    private static final String TODO_TABLE = "todo_table";
    private static final String TODO_ID = "todo_id";
    private static final String TODO_USERNAME = "todo_username";
    private static final String TODO_TITLE = "todo_title";
    private static final String TODO_YEAR = "todo_year";
    private static final String TODO_MONTH = "todo_month";
    private static final String TODO_DAY = "todo_day";
    private static final String TODO_ADD_SPECIFIC_TIME = "todo_add_specific_time";
    private static final String TODO_SET_REMINDER = "todo_set_reminder";
    private static final String TODO_ACTIVITY_TIME = "todo_activity_time";
    private static final String TODO_REMINDER_INTERVALS = "todo_reminder_intervals";
    private static final String TODO_REMINDER_STARTING_TIME = "todo_reminder_starting_time";
    private static final String TODO_PRIORITY = "todo_priority";
    private static final String TODO_HOUR = "todo_hour";
    private static final String TODO_MINUTE = "todo_minute";
    private static final String TODO_BACKGROUND_COLOR = "todo_background_color";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create user_table //
        String queryCreateUserTable =
                "CREATE TABLE " + USER_TABLE +
                        " (" + USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        USER_COLUMN_USERNAME + " TEXT, " +
                        USER_COLUMN_PASSWORD + " TEXT, " +
                        USER_COLUMN_EMAIL + " TEXT" +
                        ");";

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
                        EXPENSE_COLUMN_USERNAME + " TEXT, " +
                        EXPENSE_COLUMN_FULLDATE + " TEXT" +
                        ");";

        // Create notes_table //
        String queryCreateNotesTable =
                "CREATE TABLE " + NOTES_TABLE +
                        " (" + NOTES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        NOTES_USERNAME + " TEXT, " +
                        NOTES_TEXT + " TEXT, " +
                        NOTES_BACKGROUND_COLOR + " INT, " +
                        NOTES_FONT_COLOR + " INT" +
                        ");";

        // Create todo_table //
        String queryCreateToDoTable =
                "CREATE TABLE " + TODO_TABLE +
                        " (" + TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TODO_USERNAME + " TEXT, " +
                        TODO_TITLE + " TEXT, " +
                        TODO_YEAR + " TEXT, " +
                        TODO_MONTH + " TEXT, " +
                        TODO_DAY + " TEXT, " +
                        TODO_ADD_SPECIFIC_TIME + " BOOLEAN, " +
                        TODO_SET_REMINDER + " BOOLEAN, " +
                        TODO_ACTIVITY_TIME + " TEXT, " +
                        TODO_REMINDER_INTERVALS + " TEXT, " +
                        TODO_REMINDER_STARTING_TIME + " TEXT, " +
                        TODO_PRIORITY + " INT, " +
                        TODO_HOUR + " TEXT, " +
                        TODO_MINUTE + " TEXT, " +
                        TODO_BACKGROUND_COLOR + " TEXT" +
                        ");";

        // Execute queries //
        db.execSQL(queryCreateUserTable);
        db.execSQL(queryCreateExpenseTable);
        db.execSQL(queryCreateNotesTable);
        db.execSQL(queryCreateToDoTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + EXPENSE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);

    }

    /* DATABASE FUNCTIONS */

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
        cv.put(EXPENSE_COLUMN_FULLDATE, convertToDate(year, month, day));

        long result = db.insert(EXPENSE_TABLE, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed Upload :(", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Expense successfully added :)", Toast.LENGTH_SHORT).show();
        }
    }

    /* Convert year, month, day to YYYY-MM-DD string */
    public String convertToDate(String year, String month, String day) {
        String newMonth = month;
        String newDay = day;

        if (month.length() == 1) {
            newMonth = "0" + month;
        }

        if (day.length() == 1) {
            newDay = "0" + day;
        }

        String newDate = year + "-" + newMonth + "-" + newDay;

        return newDate;
    }

    public void addUserNote(String noteText, int noteBackgroundColor, int noteFontColor, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String text = noteText;
        int backgroundColor = noteBackgroundColor;
        int fontColor = noteFontColor;

        cv.put(NOTES_USERNAME, username);
        cv.put(NOTES_TEXT, text);
        cv.put(NOTES_BACKGROUND_COLOR, backgroundColor);
        cv.put(NOTES_FONT_COLOR, fontColor);

        long result = db.insert(NOTES_TABLE, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed Upload :(", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Note successfully added :)", Toast.LENGTH_SHORT).show();
        }
    }

    public void addUserTodo(String username, String title, String year, String month, String day,
                            int add_specific_time, int set_reminder, String time,
                            String intervals, String starting_time, int priority, String hour, String minute,
                            String backgroundColor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TODO_USERNAME, username);
        cv.put(TODO_TITLE, title);
        cv.put(TODO_YEAR, year);
        cv.put(TODO_MONTH, month);
        cv.put(TODO_DAY, day);
        cv.put(TODO_ADD_SPECIFIC_TIME, add_specific_time);
        cv.put(TODO_SET_REMINDER, set_reminder);
        cv.put(TODO_ACTIVITY_TIME, time);
        cv.put(TODO_REMINDER_INTERVALS, intervals);
        cv.put(TODO_REMINDER_STARTING_TIME, starting_time);
        cv.put(TODO_PRIORITY, priority);
        cv.put(TODO_HOUR, hour);
        cv.put(TODO_MINUTE, minute);
        cv.put(TODO_BACKGROUND_COLOR, backgroundColor);

        long result = db.insert(TODO_TABLE, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed Upload :(", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Todo successfully added :)", Toast.LENGTH_SHORT).show();
        }
    }

    /* Set boolean value of To Do reminder of given id */
    public void setToDoReminder(String id, int isReminderNotified) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TODO_SET_REMINDER, isReminderNotified);

        long result = db.update(TODO_TABLE, cv, TODO_ID + "=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed Update", Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(context, "Updated Todo Reminder to: " + Integer.toString(isReminderNotified), Toast.LENGTH_SHORT).show();
            if (isReminderNotified == 0) {
                Toast.makeText(context, "Turned off reminder", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Turned on reminder", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setToDoBackgroundColor(String id, String color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TODO_BACKGROUND_COLOR, color);

        long result = db.update(TODO_TABLE, cv, TODO_ID + "=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed Update", Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(context, "Updated Todo Reminder to: " + Integer.toString(isReminderNotified), Toast.LENGTH_SHORT).show();
            if (color == null) {
                Toast.makeText(context, "Turned off reminder", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Turned on reminder", Toast.LENGTH_SHORT).show();
            }
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
                EXPENSE_COLUMN_USERNAME + "=?" +
                " ORDER BY date(" + EXPENSE_COLUMN_FULLDATE +
                ") DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username});
        }
        return cursor;
    }

    public Cursor readAllUserNotesTable(String username) {
        String query = "SELECT * FROM " + NOTES_TABLE + " WHERE " +
                NOTES_USERNAME + "=?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username});
        }
        return cursor;
    }

    public Cursor readAllUserTodoTable(String username) {
        String query = "SELECT * FROM " + TODO_TABLE + " WHERE " +
                TODO_USERNAME + "=?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username});
        }
        return cursor;
    }

    public Cursor readAllUserTodoTableSortedByPriority(String username){
        String query = "SELECT * FROM " + TODO_TABLE + " WHERE " +
                TODO_USERNAME + "=?" + " ORDER BY " + TODO_PRIORITY + " ASC ";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username});
        }
        return cursor;
    }

    public Cursor readAllUserTodoTableSortedByDateTime(String username){

        String cast_year = " cast( " + TODO_YEAR + " as int) ";
        String cast_day = " cast( " + TODO_DAY + " as int) ";
        String cast_month = " cast( " + TODO_MONTH + " as int) ";
        String cast_hour = " cast( " + TODO_HOUR + " as int) ";
        String cast_minute = " cast( " + TODO_MINUTE + " as int) ";

        String query = "SELECT * FROM " + TODO_TABLE + " WHERE " +
                TODO_USERNAME + "=?" + " ORDER BY " + cast_year + " , " +
                cast_month + " , " + cast_day + " , " + cast_hour + " , " +
                cast_minute;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username});
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
            cursor = db.rawQuery(query, new String[]{username});
        }
        return cursor;
    }

    /* Get the sum of the amount column in expense_table of selected user */
    public Cursor getUserTotalProfits(String username) {
        String query = "SELECT SUM(" + EXPENSE_COLUMN_AMOUNT + ") " +
                "FROM " + EXPENSE_TABLE + " WHERE " + EXPENSE_COLUMN_AMOUNT +
                " >= 0 AND " + EXPENSE_COLUMN_USERNAME + "=?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username});
        }
        return cursor;
    }

    /* Get the total expenses amount of a user given a selected month and year */
    public Cursor getTotalExpensesOfYearMonth(String username, String year, String month) {
        String query = "SELECT SUM(" + EXPENSE_COLUMN_AMOUNT + ") " +
                "FROM " + EXPENSE_TABLE +
                " WHERE " + EXPENSE_COLUMN_AMOUNT + " < 0 AND " +
                EXPENSE_COLUMN_USERNAME + "=? AND " +
                EXPENSE_COLUMN_YEAR + "=? AND " +
                EXPENSE_COLUMN_MONTH + "=?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username, year, getIntegerMonth(month)});
        }

        return cursor;
    }

    /* Get the total profits amount of a user given a selected month and year */
    public Cursor getTotalProfitsOfYearMonth(String username, String year, String month) {
        String query = "SELECT SUM(" + EXPENSE_COLUMN_AMOUNT + ") " +
                "FROM " + EXPENSE_TABLE +
                " WHERE " + EXPENSE_COLUMN_AMOUNT + " >= 0 AND " +
                EXPENSE_COLUMN_USERNAME + "=? AND " +
                EXPENSE_COLUMN_YEAR + "=? AND " +
                EXPENSE_COLUMN_MONTH + "=?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username, year, getIntegerMonth(month)});
        }

        return cursor;
    }

    /* Return a month as a number string, for example: "January" returns "1" */
    public String getIntegerMonth(String month) {
        String integerMonth = "";

        switch (month) {
            case "January":
                integerMonth = "1";
                break;
            case "February":
                integerMonth = "2";
                break;
            case "March":
                integerMonth = "3";
                break;
            case "April":
                integerMonth = "4";
                break;
            case "May":
                integerMonth = "5";
                break;
            case "June":
                integerMonth = "6";
                break;
            case "July":
                integerMonth = "7";
                break;
            case "August":
                integerMonth = "8";
                break;
            case "September":
                integerMonth = "9";
                break;
            case "October":
                integerMonth = "10";
                break;
            case "November":
                integerMonth = "11";
                break;
            default:
                integerMonth = "12";
                break;
        }

        return integerMonth;
    }

    /* Return the sums of expenses grouped by category in expense_table of selected user */
    public Cursor getAllCategoryExpenses(String username) {
        String query = "SELECT " + EXPENSE_COLUMN_CATEGORY + " ," +
                "SUM(" + EXPENSE_COLUMN_AMOUNT + ") " +
                "FROM " + EXPENSE_TABLE + " WHERE " + EXPENSE_COLUMN_AMOUNT +
                " < 0 AND " + EXPENSE_COLUMN_USERNAME + "=? GROUP BY " +
                EXPENSE_COLUMN_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username});
        }
        return cursor;
    }

    /* Return the sums of expenses grouped by category in expense_table of selected user, year and month */
    public Cursor getYearMonthCategoryExpenses(String username, String year, String month) {
        String query = "SELECT " + EXPENSE_COLUMN_CATEGORY + " ," +
                "SUM(" + EXPENSE_COLUMN_AMOUNT + ") " +
                "FROM " + EXPENSE_TABLE + " WHERE " + EXPENSE_COLUMN_AMOUNT + " < 0 " +
                "AND " + EXPENSE_COLUMN_USERNAME + "=? " +
                "AND " + EXPENSE_COLUMN_YEAR + "=? " +
                "AND " + EXPENSE_COLUMN_MONTH + "=? " +
                "GROUP BY " + EXPENSE_COLUMN_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username, year, getIntegerMonth(month)});
        }

        return cursor;
    }

    /* Return the sums of profits grouped by category in expense_table of selected user, year and month */
    public Cursor getYearMonthCategoryProfits(String username, String year, String month) {
        String query = "SELECT " + EXPENSE_COLUMN_CATEGORY + " ," +
                "SUM(" + EXPENSE_COLUMN_AMOUNT + ") " +
                "FROM " + EXPENSE_TABLE + " WHERE " + EXPENSE_COLUMN_AMOUNT + " >= 0 " +
                "AND " + EXPENSE_COLUMN_USERNAME + "=? " +
                "AND " + EXPENSE_COLUMN_YEAR + "=? " +
                "AND " + EXPENSE_COLUMN_MONTH + "=? " +
                "GROUP BY " + EXPENSE_COLUMN_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username, year, getIntegerMonth(month)});
        }

        return cursor;
    }

    /* Return the sums of profits grouped by category in expense_table of selected user */
    public Cursor getAllCategoryProfits(String username) {
        String query = "SELECT " + EXPENSE_COLUMN_CATEGORY + " ," +
                "SUM(" + EXPENSE_COLUMN_AMOUNT + ") " +
                "FROM " + EXPENSE_TABLE + " WHERE " + EXPENSE_COLUMN_AMOUNT +
                " >= 0 AND " + EXPENSE_COLUMN_USERNAME + "=? GROUP BY " +
                EXPENSE_COLUMN_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username});
        }
        return cursor;
    }

    /* Returns a list of the years of a user's expenses */
    public List<String> getYearsOfExpenses(String username) {
        List<String> yearsList = new ArrayList<String>();

        String query = "SELECT DISTINCT " + EXPENSE_COLUMN_YEAR + " FROM " + EXPENSE_TABLE +
                " WHERE " + EXPENSE_COLUMN_USERNAME + "=?" +
                " ORDER BY " + EXPENSE_COLUMN_YEAR + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username});
        }

        if (cursor.getCount() == 0) {
            // Toast.makeText(context, "No Data Available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                yearsList.add(cursor.getString(0));
            }
        }

        for (int i = 0; i < yearsList.size(); i++) {
            String tempYear = yearsList.get(i);
            Log.d("yearslist", tempYear);
        }

        return yearsList;
    }

    /* Delete an expense given a selected id */
    public void deleteExpense(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(EXPENSE_TABLE, "id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed Delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted Expense", Toast.LENGTH_SHORT).show();
        }
    }

    /* Delete a note given a selected id */
    public void deleteNote(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(NOTES_TABLE, "id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed Delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted Note", Toast.LENGTH_SHORT).show();
        }
    }

    /* Delete a to do given a selected id */
    public void deleteTodo(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TODO_TABLE, "todo_id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Failed Delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted Todo", Toast.LENGTH_SHORT).show();
        }
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

    /* Return true if user exists in the database, false otherwise */
    public boolean userExists(String username) {
        String query = "SELECT * FROM " + USER_TABLE + " WHERE username=?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username});
        }

        // Match Found //
        if (cursor.getCount() > 0) {
            return true;
        }

        return false;
    }

    /* Function for login. Return true if username password match exists */
    public boolean loginUser(String username, String password) {
        String query = "SELECT * FROM " + USER_TABLE + " WHERE username=?" +
                " AND password=?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, new String[]{username, password});
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

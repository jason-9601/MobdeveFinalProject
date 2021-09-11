package com.mobdeve.s12.mobdevefinalproject.addexpenses;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobdeve.s12.mobdevefinalproject.database.DatabaseHelper;
import com.mobdeve.s12.mobdevefinalproject.R;

import java.util.Calendar;

public class AddExpensesInput extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String loggedInUser;

    // Shared preferences //
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private EditText etExpenseTitle;
    private EditText etExpenseAmount;
    private EditText etExpenseDate;
    private Spinner spExpenseCategory;
    private Button btnAddExpensesInput;

    private String yearSelected;
    private String monthSelected;
    private String daySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses_input);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        spEditor = this.sp.edit();

        // Print logged in user to Logcat for checking //
        loggedInUser = sp.getString("KEY_USERNAME_LOGGED_IN", "N/A");
        Log.d("Logged in user: ", loggedInUser);

        etExpenseTitle = findViewById(R.id.et_expense_title);
        etExpenseAmount = findViewById(R.id.et_expense_amount);

        // Date Input //
        etExpenseDate = findViewById(R.id.et_expense_date);

        etExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // Categories Dropdown //
        spExpenseCategory = findViewById(R.id.sp_expense_category);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(this,
                R.array.expenses_categories_array, android.R.layout.simple_spinner_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExpenseCategory.setAdapter(spAdapter);

        // Add Expense Button //
        btnAddExpensesInput = findViewById(R.id.btn_add_expenses_input);
        btnAddExpensesInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(AddExpensesInput.this);

                if (inputIsNotComplete()) {
                    Toast.makeText(getApplicationContext(), "Please complete fields", Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.addUserExpense(etExpenseTitle.getText().toString().trim(),
                            yearSelected, monthSelected, daySelected,
                            Float.valueOf(etExpenseAmount.getText().toString()),
                            spExpenseCategory.getSelectedItem().toString(), loggedInUser);
                    finish();
                }
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(this, this, year, month, day);
        datePicker.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        etExpenseDate.setHint(Integer.toString(year) + "/" + Integer.toString(month + 1) + "/" + Integer.toString(day));
        this.yearSelected = Integer.toString(year);
        this.monthSelected = Integer.toString(month + 1);
        this.daySelected = Integer.toString(day);
    }

    // Return true if at least one of the input fields is empty //
    public boolean inputIsNotComplete() {
        if (etExpenseTitle.getText().toString().trim().length() == 0 ||
                yearSelected == null ||
                monthSelected == null ||
                daySelected == null ||
                etExpenseAmount.getText().toString().length() == 0 ||
                spExpenseCategory.getSelectedItem().toString().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

}
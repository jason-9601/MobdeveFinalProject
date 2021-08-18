package com.mobdeve.s12.mobdevefinalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class AddExpensesInput extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText etExpenseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expenses_input);

        etExpenseDate = findViewById(R.id.et_expense_date);

        etExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
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

        etExpenseDate.setHint(Integer.toString(year) + "/" + Integer.toString(month) + "/" + Integer.toString(day));

    }

}
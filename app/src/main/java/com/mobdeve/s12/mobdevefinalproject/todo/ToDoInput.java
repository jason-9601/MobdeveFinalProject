package com.mobdeve.s12.mobdevefinalproject.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.timepicker.TimeFormat;
import com.mobdeve.s12.mobdevefinalproject.DateTimeHelper;
import com.mobdeve.s12.mobdevefinalproject.R;
import com.mobdeve.s12.mobdevefinalproject.database.DatabaseHelper;
import com.mobdeve.s12.mobdevefinalproject.notes.NotesInput;

import java.time.Clock;
import java.util.Calendar;

public class ToDoInput extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String loggedInUser;

    // Shared preferences //
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private EditText etToDoTitle;
    private EditText etToDoDate;
    private EditText etToDoTime;
    private EditText etToDoPriority;

    private CheckBox addSpecificTime;
    private CheckBox setReminders;

    private Spinner reminderIntervals;
    private Spinner reminderStartingTime;

    private Button buttonAddToDo;

    private String yearSelected;
    private String monthSelected;
    private String daySelected;

    private String hourSelected;
    private String minuteSelected;
    private String timeCombined;

    private boolean isAddSpecificTime;
    private boolean isSetReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo_input);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        spEditor = this.sp.edit();

        // Print logged in user to Logcat for checking //
        loggedInUser = sp.getString("KEY_USERNAME_LOGGED_IN", "N/A");
        Log.d("Logged in user: ", loggedInUser);

        this.etToDoTitle = findViewById(R.id.et_todo_title);
        this.etToDoDate = findViewById(R.id.et_todo_date);
        this.etToDoTime = findViewById(R.id.et_todo_time);
        this.etToDoPriority = findViewById(R.id.et_todo_priority);

        this.addSpecificTime = findViewById(R.id.cbs_add_specific_time);
        this.setReminders = findViewById(R.id.cbs_set_reminder);

        this.reminderIntervals = findViewById(R.id.sp_todo_reminder_intervals);
        this.reminderStartingTime = findViewById(R.id.sp_todo_reminder_starting_time);

        this.buttonAddToDo = findViewById(R.id.btn_add_todo);

        this.isAddSpecificTime = true;
        this.isSetReminders = true;

        ArrayAdapter<CharSequence> spIntervals = ArrayAdapter.createFromResource(this,
                R.array.reminders_intervals_array, android.R.layout.simple_spinner_item);
        spIntervals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderIntervals.setAdapter(spIntervals);

        ArrayAdapter<CharSequence> spStartingTime = ArrayAdapter.createFromResource(this,
                R.array.reminders_intervals_starting_time_array, android.R.layout.simple_spinner_item);
        spStartingTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderStartingTime.setAdapter(spStartingTime);

        etToDoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        etToDoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        this.addSpecificTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    etToDoTime.setEnabled(true);
                    isAddSpecificTime = true;
                } else {
                    etToDoTime.setEnabled(false);
                    isAddSpecificTime = false;
                }
            }
        });

        this.setReminders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    reminderIntervals.setEnabled(true);
                    reminderStartingTime.setEnabled(true);

                    reminderIntervals.setSelection(0);
                    reminderStartingTime.setSelection(0);

                    isSetReminders = true;
                } else {
                    reminderIntervals.setEnabled(false);
                    reminderStartingTime.setEnabled(false);

                    reminderIntervals.setSelection(0);
                    reminderStartingTime.setSelection(0);

                    isSetReminders = false;
                }
            }
        });

        this.buttonAddToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputIsNotComplete()) {
                    Toast.makeText(getApplicationContext(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                } else {

                    if (priorityNotBetweenRange()) {
                        Toast.makeText(getApplicationContext(), "Priority must be between 1 and 100", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent();
                        addToDatabase();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }

                }
            }
        });
    }

    private void addToDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(ToDoInput.this);

        String title = etToDoTitle.getText().toString();
        String intervals = reminderIntervals.getSelectedItem().toString();
        String starting_time = reminderStartingTime.getSelectedItem().toString();
        int priority = Integer.parseInt(etToDoPriority.getText().toString());

        // Convert these two variables into integers as sqlite treats boolean as 0 or 1 only //
        int isAddSpecificTimeInteger;
        int isSetRemindersInteger;

        if (isAddSpecificTime == true) {
            isAddSpecificTimeInteger = 1;
        } else {
            isAddSpecificTimeInteger = 0;
            hourSelected = "0";
            minuteSelected = "0";
            timeCombined = "0";
        }

        if (isSetReminders == true) {
            // Temporarily set this to 0 by default
            // isSetRemindersInteger = 1;
            isSetRemindersInteger = 0;
        } else {
            isSetRemindersInteger = 0;
        }

        String fullDateTime = DateTimeHelper.convertToDate(yearSelected, monthSelected, daySelected) +
                " " + DateTimeHelper.reformatTimeString(hourSelected, minuteSelected);

        dbHelper.addUserTodo(loggedInUser, title, yearSelected, monthSelected, daySelected,
                isAddSpecificTimeInteger, isSetRemindersInteger, timeCombined, intervals, starting_time,
                priority, hourSelected, minuteSelected, fullDateTime, "#FFFFFF");
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(this, this, year, month, day);
        datePicker.show();
    }

    private void showTimePicker() {
        DialogFragment timeSelector = new TimeSelectorFragment();
        timeSelector.show(getSupportFragmentManager(), "time picker");
    }

    // Return true if at least one of the input fields is empty //
    public boolean inputIsNotComplete() {
        // Check if title and date have input first as they are required fields //
        if (etToDoTitle.getText().toString().trim().length() == 0 ||
                yearSelected == null ||
                monthSelected == null ||
                daySelected == null) {
            return true;
        }

        // When addSpecificTime checkbox is checked, check if time has input //
        if (addSpecificTime.isSelected()) {
            if (daySelected == null || hourSelected == null || minuteSelected == null) {
                return true;
            }
        }

        // Otherwise return false is input is complete //
        return false;
    }

    // Return true if to do priority is not between range (0-100) //
    public boolean priorityNotBetweenRange() {
        if (Integer.parseInt(etToDoPriority.getText().toString().trim()) < 0 || Integer.parseInt(etToDoPriority.getText().toString().trim()) > 100) {
            return true;
        }

        return false;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        etToDoDate.setHint(Integer.toString(year) + "/" + Integer.toString(month + 1) + "/" + Integer.toString(day));
        this.yearSelected = Integer.toString(year);
        this.monthSelected = Integer.toString(month + 1);
        this.daySelected = Integer.toString(day);
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        etToDoTime.setHint(Integer.toString(hour) + ":" + Integer.toString(minute));
        this.hourSelected = Integer.toString(hour);
        this.minuteSelected = Integer.toString(minute);
        this.timeCombined = Integer.toString(hour) + ":" + Integer.toString(minute);
    }

}

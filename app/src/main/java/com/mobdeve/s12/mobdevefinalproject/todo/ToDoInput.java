package com.mobdeve.s12.mobdevefinalproject.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import com.mobdeve.s12.mobdevefinalproject.R;
import com.mobdeve.s12.mobdevefinalproject.database.DatabaseHelper;
import com.mobdeve.s12.mobdevefinalproject.notes.NotesInput;

import java.time.Clock;
import java.util.Calendar;

public class ToDoInput extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

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

    private boolean isAddSpecificTime;
    private boolean isSetReminders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo_input);

        this.etToDoTitle          = findViewById(R.id.et_todo_title);
        this.etToDoDate           = findViewById(R.id.et_todo_date);
        this.etToDoTime           = findViewById(R.id.et_todo_time);
        this.etToDoPriority       = findViewById(R.id.et_todo_priority);

        this.addSpecificTime      = findViewById(R.id.cbs_add_specific_time);
        this.setReminders         = findViewById(R.id.cbs_set_reminder);

        this.reminderIntervals    = findViewById(R.id.sp_todo_reminder_intervals);
        this.reminderStartingTime = findViewById(R.id.sp_todo_reminder_starting_time);

        this.buttonAddToDo        = findViewById(R.id.btn_add_todo);

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

        /*
        etToDoTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });
        */

        this.addSpecificTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    etToDoTime.setEnabled(true);
                    isAddSpecificTime = true;
                }
                else{
                    etToDoTime.setEnabled(false);
                    isAddSpecificTime = false;
                }
            }
        });

        this.setReminders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    reminderIntervals.setEnabled(true);
                    reminderStartingTime.setEnabled(true);

                    reminderIntervals.setSelection(0);
                    reminderStartingTime.setSelection(0);

                    isSetReminders = true;
                }
                else{
                    reminderIntervals.setEnabled(false);
                    reminderStartingTime.setEnabled(false);

                    reminderIntervals.setSelection(5);
                    reminderStartingTime.setSelection(2);

                    isSetReminders = false;
                }
            }
        });

        this.buttonAddToDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isAllFilled = true;
                //boolean isAllFilled = isAllFilled();
                if(isAllFilled)
                {
                    Intent intent = new Intent();
                    addToDatabase();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addToDatabase(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor spEditor = sp.edit();

        // Print logged in user to Logcat for checking //
        String loggedInUser = sp.getString("KEY_USERNAME_LOGGED_IN", "N/A");
        Log.d("Logged in user: ", loggedInUser);

        DatabaseHelper dbHelper = new DatabaseHelper(ToDoInput.this);

        String title = etToDoTitle.getText().toString();
        String time = etToDoTime.getText().toString();
        String intervals = reminderIntervals.getSelectedItem().toString();
        String starting_time = reminderStartingTime.getSelectedItem().toString();
        int priority = Integer.parseInt(etToDoPriority.getText().toString());

        dbHelper.addUserTodo(loggedInUser,title,yearSelected, monthSelected, daySelected,
                isAddSpecificTime,isSetReminders, time, intervals, starting_time,
                priority);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(this, this, year, month, day);
        datePicker.show();
    }

    private void showTimePicker(){


        TimePicker timePicker = new TimePicker(this);
        String ampm;


        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        if(hour > 12){
            ampm = "PM";
            hour -= 12;
        }
        else{
            ampm = "AM";
        }

        etToDoTime.setText(hour + ":" + minute + " " + ampm);
    }

    private boolean isAllFilled(){

        if(etToDoTitle.getText().toString().length() != 0
        && etToDoDate.getText().toString().length()  != 0
        && etToDoTime.getText().toString().length()  != 0
        && etToDoPriority.getText().toString().length() != 0)
            return true;

        return false;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        etToDoDate.setHint(Integer.toString(year) + "/" + Integer.toString(month) + "/" + Integer.toString(day));
        this.yearSelected = Integer.toString(year);
        this.monthSelected = Integer.toString(month);
        this.daySelected = Integer.toString(day);
    }

}
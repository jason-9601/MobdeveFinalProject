package com.mobdeve.s12.mobdevefinalproject.todo;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s12.mobdevefinalproject.DateTimeHelper;
import com.mobdeve.s12.mobdevefinalproject.NotificationReceiver;
import com.mobdeve.s12.mobdevefinalproject.R;
import com.mobdeve.s12.mobdevefinalproject.database.DatabaseHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoViewHolder> {

    private ViewGroup parent;
    private ArrayList<ToDo> list;
    private DatabaseHelper dbHelper;

    // Shared preferences //
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    public ToDoAdapter(ArrayList<ToDo> list){
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        dbHelper = new DatabaseHelper(parent.getContext());
        sp = PreferenceManager.getDefaultSharedPreferences(parent.getContext());
        spEditor = this.sp.edit();
        this.parent = parent;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.rv_template_add_todo, parent, false);
        ToDoViewHolder vh = new ToDoViewHolder(itemView);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ToDoViewHolder holder, int position) {
        holder.setTitle(list.get(position).getTodo_Title());
        holder.setDate(list.get(position).getTodo_date());
        holder.setTime(DateTimeHelper.reformatTimeString(list.get(position).getTodo_hour(), list.get(position).getTodo_minutes()));
        holder.setPriority(list.get(position).getPriority());
        holder.setNotifications(list.get(position).getIsNotified());

        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerNotification(holder, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    // Function for creating notification channel //
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "To Do Channel";
            String channelDescription = "Description here";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("channelTodo", channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = parent.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Onclick listener for to do loudspeaker button
    // Triggers the notification at the time and date of the respective to do
    private void triggerNotification(@NonNull @NotNull ToDoViewHolder holder, int position) {

        createNotificationChannel();

        ToDo selectedTodo = list.get(holder.getBindingAdapterPosition());

        int selectedTodoId = Integer.parseInt(selectedTodo.getTodo_id());
        String selectedTodoTitle = selectedTodo.getTodo_Title();

        int isOn;
        // Set isOn to opposite value
        // Call the turnOffReminder and turnOnReminder functions accordingly
        if (selectedTodo.getIsNotified() == 1) {
            isOn = 0;
            turnOffReminder(holder, position);
        } else {
            isOn = 1;
            turnOnReminder(holder, position);
        }

        // Changes color of button //
        holder.setNotifications(isOn);

        // To Do model class //
        selectedTodo.setNotified(isOn);

        // Set isOn in database for the clicked to do (todo_set_reminder column of todo_table) //
        dbHelper.setToDoReminder(selectedTodo.getTodo_id(), isOn);
    }

    // Function to turn on reminder. Starts up the repeating alarm of respective to do //
    private void turnOnReminder(@NonNull @NotNull ToDoViewHolder holder, int position) {
        ToDo selectedTodo = list.get(holder.getBindingAdapterPosition());

        int selectedTodoId = Integer.parseInt(selectedTodo.getTodo_id());
        String selectedTodoTitle = selectedTodo.getTodo_Title();

        String whenStart = selectedTodo.getRemindStartingTime();
        String alarmIntervals = selectedTodo.getRemindInterval();

        int goalCount = getGoalCount(alarmIntervals);

        // Count how many times alarm has repeated for selected to do //
        spEditor.putInt("CURRENT_COUNT_OF_ID_" + selectedTodoId, 0);
        spEditor.putInt("GOAL_COUNT_OF_" + selectedTodoId, goalCount);
        spEditor.apply();

        // Create intent for NotificationReceiver class //
        Intent intent = new Intent(parent.getContext(), NotificationReceiver.class);
        intent.putExtra("todoRequestCode", selectedTodoId);
        intent.putExtra("todoTitle", selectedTodoTitle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(parent.getContext(), selectedTodoId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)parent.getContext().getSystemService(Context.ALARM_SERVICE);

        String date[]= selectedTodo.getTodo_date().split("/");
        int year = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int day = Integer.parseInt(date[2]);
        int hour = Integer.parseInt(selectedTodo.getTodo_hour());
        int minutes = Integer.parseInt(selectedTodo.getTodo_minutes());

        // Create Calendar based from to do date //
        // Subtract 1 from month parameter as it is zero indexed in the database //
        Calendar alarmDate = getCalendarWithValues(year, month - 1, day,
                hour, minutes, 0);

        long alarmStartingMillis = getAlarmStarting(alarmDate, whenStart, alarmIntervals);
        long alarmIntervalsMillis = getAlarmIntervals(alarmDate, whenStart, alarmIntervals);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartingMillis, alarmIntervalsMillis, pendingIntent);
    }

    // Function to turn off reminder. Cancels the repeating alarm of respective to do //
    private void turnOffReminder(@NonNull @NotNull ToDoViewHolder holder, int position) {
        ToDo selectedTodo = list.get(holder.getBindingAdapterPosition());
        int selectedTodoId = Integer.parseInt(selectedTodo.getTodo_id());

        AlarmManager alarmManager = (AlarmManager)parent.getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(parent.getContext(), NotificationReceiver.class);
        intent.putExtra("todoRequestCode", selectedTodoId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(parent.getContext(), selectedTodoId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }

    // Return a Calendar with set values //
    private Calendar getCalendarWithValues(int year, int month, int day,
                                     int hour, int minutes, int seconds) {
        Calendar alarmDate = Calendar.getInstance();

        alarmDate.set(Calendar.MONTH, month);
        alarmDate.set(Calendar.YEAR,year);
        alarmDate.set(Calendar.DAY_OF_MONTH, day);
        alarmDate.set(Calendar.HOUR_OF_DAY, hour);
        alarmDate.set(Calendar.MINUTE, minutes);
        alarmDate.set(Calendar.SECOND,0);

        return alarmDate;
    }

    // Get the starting time of the alarm in milliseconds //
    private long getAlarmStarting(Calendar scheduledDate, String whenStart, String alarmIntervals) {
        // Default value //
        long alarmStarting = scheduledDate.getTimeInMillis();

        if (whenStart.equals("On The Day Itself")) {

            if (alarmIntervals.equals("Once")) {
                // Alarm on the time itself
                alarmStarting = scheduledDate.getTimeInMillis();
            } else if (alarmIntervals.equals("Twice")) {
                // Subtract an hour or 3600000 milliseconds //
                alarmStarting = scheduledDate.getTimeInMillis() - 3600000;
            } else if (alarmIntervals.equals("Thrice")) {
                // Subtract two hours or 7200000 milliseconds //
                alarmStarting = scheduledDate.getTimeInMillis() - 7200000;
            }

        } else if (whenStart.equals("1 Day Before")) {

            // Subtract a day in milliseconds //
            alarmStarting = scheduledDate.getTimeInMillis() -  86400000;
            Log.d("equal", "equal2st");

        } else if (whenStart.equals("2 Days Before")) {

            // Subtract 2 days in milliseconds //
            alarmStarting = scheduledDate.getTimeInMillis() - 172800000;
            Log.d("equal", "equal3st");

        }

        Calendar currentDateTime = Calendar.getInstance();
        boolean isLater = goalTimeIsLater(scheduledDate.getTimeInMillis(), currentDateTime.getTimeInMillis());
        Log.d("time-islater", Boolean.toString(isLater));

        // If the value of starting alarm date time is not later than current date time, set alarm starting to the current datetime //
        if (!isLater) {
            alarmStarting = scheduledDate.getTimeInMillis();
        }

        return alarmStarting;
    }

    // Get alarm intervals in milliseconds //
    private long getAlarmIntervals(Calendar scheduledDate, String whenStart, String alarmIntervals) {
        long alarmIntervalsInMillis;

        if (whenStart.equals("On The Day Itself")) {

            if (alarmIntervals.equals("Once")) {
                alarmIntervalsInMillis = scheduledDate.getTimeInMillis();
                Log.d("equalintervals", "Once");
            } else if (alarmIntervals.equals("Twice")) {
                alarmIntervalsInMillis = (scheduledDate.getTimeInMillis() - 3600000) / 2;
                Log.d("equalintervals", "Twice");
            } else {
                alarmIntervalsInMillis = (scheduledDate.getTimeInMillis() - 3600000) / 3;
                Log.d("equalintervals", "Thrice else");
            }

        } else if (whenStart.equals("1 Day Before")) {

            if (alarmIntervals.equals("Once")) {
                alarmIntervalsInMillis = scheduledDate.getTimeInMillis();
                Log.d("equalintervals", "1 day before Once");
            } else if (alarmIntervals.equals("Twice")) {
                alarmIntervalsInMillis = (scheduledDate.getTimeInMillis() -  86400000) / 2;
                Log.d("equalintervals", "1 day before Twice");
            } else {
                alarmIntervalsInMillis = (scheduledDate.getTimeInMillis() -  86400000) / 3;
                Log.d("equalintervals", "1 day before Thrice");
            }

        } else if (whenStart.equals("2 Days Before")) {

            if (alarmIntervals.equals("Once")) {
                alarmIntervalsInMillis = scheduledDate.getTimeInMillis();
                Log.d("equalintervals", "2 day before Once");
            } else if (alarmIntervals.equals("Twice")) {
                alarmIntervalsInMillis = (scheduledDate.getTimeInMillis() - 172800000) / 2;
                Log.d("equalintervals", "2 day before Twice");
            } else {
                alarmIntervalsInMillis = (scheduledDate.getTimeInMillis() - 172800000) / 3;
                Log.d("equalintervals", "1 day before Thrice");
            }

        } else {
            // Default value //
            alarmIntervalsInMillis = 120000;
        }

        alarmIntervalsInMillis = 30000;
        Log.d("alarmIntervals", Long.toString(alarmIntervalsInMillis));
        return alarmIntervalsInMillis;
    }

    // Return goal count based on interval in the database (Once, Twice, Thrice) //
    private int getGoalCount(String alarmIntervals) {
        int goalCount;

        switch(alarmIntervals) {
            case "Once":
                goalCount = 1;
                break;
            case "Twice":
                goalCount = 2;
                break;
            case "Thrice":
                goalCount = 3;
                break;
            default:
                goalCount = 1;
                break;
        }

        return goalCount;
    }

    // Returns true if the goal time is a later time than the start time
    // Parameters are datetime in milliseconds
    private boolean goalTimeIsLater(long startTime, long goalTime) {
        if (goalTime - startTime > 0) {
            return true;
        } else if (goalTime - startTime == 0) {
            return true;
        } else {
            // goalTime - startTime is < 0 //
            return false;
        }
    }

}

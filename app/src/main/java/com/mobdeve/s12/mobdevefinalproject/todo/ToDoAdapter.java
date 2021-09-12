package com.mobdeve.s12.mobdevefinalproject.todo;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    public ToDoAdapter(ArrayList<ToDo> list){
        this.list = list;
    }

    @NonNull
    @NotNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        dbHelper = new DatabaseHelper(parent.getContext());
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
        holder.setTime(list.get(position).getTodo_time());
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

    private void turnOnReminder(@NonNull @NotNull ToDoViewHolder holder, int position) {
        ToDo selectedTodo = list.get(holder.getBindingAdapterPosition());

        int selectedTodoId = Integer.parseInt(selectedTodo.getTodo_id());
        String selectedTodoTitle = selectedTodo.getTodo_Title();

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

        String hour = selectedTodo.getTodo_hour();
        String minutes = selectedTodo.getTodo_minutes();

        Calendar alarmDate = Calendar.getInstance();

        // Subtract 1 from month as Java calendar treats months zero indexed //
        alarmDate.set(Calendar.MONTH, month - 1);
        alarmDate.set(Calendar.YEAR,year);
        alarmDate.set(Calendar.DAY_OF_MONTH, day);
        alarmDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        alarmDate.set(Calendar.MINUTE, Integer.parseInt(minutes));
        alarmDate.set(Calendar.SECOND,0);

        // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSecondsInMillis, 8000, pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmDate.getTimeInMillis(), pendingIntent);
    }

    private void turnOffReminder(@NonNull @NotNull ToDoViewHolder holder, int position) {
        ToDo selectedTodo = list.get(holder.getBindingAdapterPosition());
        int selectedTodoId = Integer.parseInt(selectedTodo.getTodo_id());

        AlarmManager alarmManager = (AlarmManager)parent.getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(parent.getContext(), NotificationReceiver.class);
        intent.putExtra("todoRequestCode", selectedTodoId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(parent.getContext(), selectedTodoId, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

}

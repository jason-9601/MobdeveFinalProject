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
                createNotificationChannel();

                ToDo selectedTodo = list.get(holder.getBindingAdapterPosition());
                int isOn;

                // Set isOn to opposite value //
                if (selectedTodo.getIsNotified() == 1) {
                    isOn = 0;
                } else {
                    isOn = 1;
                }

                // Changes color of button //
                holder.setNotifications(isOn);

                // To Do model class //
                selectedTodo.setNotified(isOn);

                // Set isOn in database for the clicked to do (todo_set_reminder column of todo_table) //
                dbHelper.setToDoReminder(selectedTodo.getTodo_id(), isOn);

                // Notify user in 10 seconds - FOR TESTING - NEED TO CHANGE TO PROPER TIME SELECTED BY USER //
                Intent intent = new Intent(parent.getContext(), NotificationReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(parent.getContext(), 0, intent, 0);

                AlarmManager alarmManager = (AlarmManager)parent.getContext().getSystemService(Context.ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();
                long tenSecondsInMillis = 1000 * 10;

                // Sample alarm. First alarm starts after 10 seconds. Repeats every 8 seconds.
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenSecondsInMillis, 8000, pendingIntent);
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
}

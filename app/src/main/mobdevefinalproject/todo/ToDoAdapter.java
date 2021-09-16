package com.mobdeve.s12.mobdevefinalproject.todo;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s12.mobdevefinalproject.NotificationReceiver;
import com.mobdeve.s12.mobdevefinalproject.R;
import com.mobdeve.s12.mobdevefinalproject.database.DatabaseHelper;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
        holder.setBackgroundColor(list.get(position).getBackgroundColor());

        holder.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerNotification(holder, position);
            }
        });

        ConstraintLayout cl = holder.getLayout();
        ToDo selectedTodo = list.get(holder.getBindingAdapterPosition());

        try {
            boolean isPastDueDate = checkifPastDueDate(holder, list.get(position));
            if(isPastDueDate){
                Log.d("PASTDUEDATE", "Past Due Date");
                cl.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                dbHelper.setToDoBackgroundColor(selectedTodo.getTodo_id(),"#FF0000");
            }
        }catch(ParseException e){
            Log.d("PARSEEXCEPTION", "Error in Parsing");
        }


        GestureDetector gestureDetector = new GestureDetector(cl.getContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                try {
                    setBackgroundColorOfLayout(holder, cl, selectedTodo);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
                return true;
            }
        });

        cl.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    private boolean checkifPastDueDate(@NonNull @NotNull ToDoViewHolder holder, ToDo todo) throws ParseException {

        Calendar calendar1 = Calendar.getInstance();
        Date currDate = calendar1.getTime();

        Calendar calendar2 = Calendar.getInstance();
        String[] dateFactors = todo.getTodo_date().split("/");

        int year = Integer.parseInt(dateFactors[0]);
        int month = Integer.parseInt(dateFactors[1]);
        int day = Integer.parseInt(dateFactors[2]);
        int hour = Integer.parseInt(todo.getTodo_hour());
        int minute = Integer.parseInt(todo.getTodo_minutes());

        calendar2.set(year, month, day, hour, minute);
        Date todoDate = calendar2.getTime();

        //Log.d("CALENDAR1TIME", String.valueOf(calendar1));
        //Log.d("CALENDAR2TIME", String.valueOf(calendar2));

        if(currDate.compareTo(todoDate) > 0 )
        {
            return true;
        }

        return false;
    }

    private void setBackgroundColorOfLayout(@NonNull @NotNull ToDoViewHolder holder, ConstraintLayout cl, ToDo selectedTodo) throws ParseException {
        if(cl.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
        || cl.getBackgroundTintList() == ColorStateList.valueOf(Color.parseColor("#FF0000")))
        {
            cl.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#3DED97")));
            dbHelper.setToDoBackgroundColor(selectedTodo.getTodo_id(),"#3DED97");
        }
        else
        {
            if(checkifPastDueDate(holder,selectedTodo)){
                cl.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                dbHelper.setToDoBackgroundColor(selectedTodo.getTodo_id(),"#FF0000");
            }else{
                cl.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                dbHelper.setToDoBackgroundColor(selectedTodo.getTodo_id(),"#FFFFFF");
            }
        }
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
        //Log.d("IDofLayout", String.valueOf(holder.getButton().getId()));
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

        PendingIntent pendingIntent = PendingIntent.getBroadcast(parent.getContext(), selectedTodoId, intent, 0);

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

    public void setData(ArrayList<ToDo> newList){
        this.list.clear();
        this.list.addAll(newList);
        notifyDataSetChanged();
    }
}

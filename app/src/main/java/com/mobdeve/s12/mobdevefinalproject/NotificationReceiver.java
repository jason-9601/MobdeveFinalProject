package com.mobdeve.s12.mobdevefinalproject;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.mobdeve.s12.mobdevefinalproject.todo.ToDo;

public class NotificationReceiver extends BroadcastReceiver {

    // Shared preferences //
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        spEditor = this.sp.edit();

        NotificationManager notificationManager = (NotificationManager)context.
                getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeatingIntent = new Intent(context, MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Get id of selected to do and set it as the request code for the Pending Intent //
        int todoRequestCode = intent.getIntExtra("todoRequestCode", 0);
        String todoTitle = intent.getStringExtra("todoTitle");

        // Get current count of how many times the alarm has occured
        // This will be compared to the goal count/intervals that the alarm needs to reach
        int currentCount = sp.getInt("CURRENT_COUNT_OF_ID_" + todoRequestCode, 0);
        int goalCount = sp.getInt("GOAL_COUNT_OF_" + todoRequestCode, 0);

        // If current count has not yet reached the goal //
        if (currentCount < goalCount) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, todoRequestCode,
                    repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channelTodo")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.ic_dollar_sign)
                    .setContentTitle(todoTitle)
                    .setContentText("Current: " + Integer.toString(currentCount)+
                            "\nGoal: " + Integer.toString(goalCount))
                    //.setContentText("You have an event coming up! :D")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            notificationManager.notify(todoRequestCode, builder.build());

            Log.d("I AM CALLED", "Current: " + Integer.toString(currentCount));

            // Increment current count of alarm //
            spEditor.putInt("CURRENT_COUNT_OF_ID_" + todoRequestCode, currentCount + 1);
            spEditor.apply();
        } else {
            Log.d("REACHEDGOAL", "REACHEDGOAL!!!!");

            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, todoRequestCode,
                    repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            try {
                alarmManager.cancel(pendingIntent);
                Log.e("cancel", "Cancelling success");
            } catch(Exception e) {
                Log.e("cancel", "AlarmManager update was not canceled. " + e.toString());
            }

        }
    }

}

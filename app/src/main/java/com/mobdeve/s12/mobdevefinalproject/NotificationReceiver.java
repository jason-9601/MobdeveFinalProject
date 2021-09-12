package com.mobdeve.s12.mobdevefinalproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager)context.
                getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeatingIntent = new Intent(context, MainActivity.class);
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Get id of selected to do and set it as the request code //
        int todoRequestCode = intent.getIntExtra("todoRequestCode", 0);
        String todoTitle = intent.getStringExtra("todoTitle");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, todoRequestCode,
                repeatingIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channelTodo")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_dollar_sign)
                .setContentTitle(todoTitle)
                .setContentText("You have an event coming up! :D")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(todoRequestCode, builder.build());
    }

}

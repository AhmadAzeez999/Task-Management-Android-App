package com.example.mytaskmanagementapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the alarm goes off

        // Get the date and time information from the intent (you should put these values when setting the alarm)
        long dateTimeInMillis = intent.getLongExtra("dateTimeInMillis", 0);

        // Create an intent to broadcast the date and time information
        Intent broadcastIntent = new Intent("com.example.mytaskmanagementapp.REMINDER_RECEIVED");
        broadcastIntent.putExtra("dateTimeInMillis", dateTimeInMillis);

        // Send the broadcast
        context.sendBroadcast(broadcastIntent);
    }
}


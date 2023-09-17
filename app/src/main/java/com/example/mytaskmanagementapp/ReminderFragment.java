package com.example.mytaskmanagementapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Calendar;


public class ReminderFragment extends Fragment {

    private DatePicker setDatePicker;
    private TimePicker setTimePicker;
    private Button saveReminderButton;
    private Button btnOpenSetReminder;
    private RecyclerView recyclerView;
    private ReminderAdapter adapter;
    private List<String> reminders;

    // BroadcastReceiver to handle the reminder broadcast
    private BroadcastReceiver reminderBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get the date and time information from the broadcast
            long dateTimeInMillis = intent.getLongExtra("dateTimeInMillis", 0);

            // Convert the millis to a readable date and time format (you can customize this)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            String formattedDateTime = sdf.format(new Date(dateTimeInMillis));

            // Add the formatted date and time to the RecyclerView
            reminders.add(formattedDateTime);
            adapter.notifyDataSetChanged();

            // Show a toast or perform any other actions you want
            Toast.makeText(context, "Reminder received for " + formattedDateTime, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register a BroadcastReceiver to listen for the broadcast
        IntentFilter intentFilter = new IntentFilter("com.example.mytaskmanagementapp.REMINDER_RECEIVED");
        requireContext().registerReceiver(reminderBroadcastReceiver, intentFilter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        setDatePicker = view.findViewById(R.id.setDatePicker);
        setTimePicker = view.findViewById(R.id.setTimePicker);
        saveReminderButton = view.findViewById(R.id.saveReminderButton);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnOpenSetReminder = view.findViewById(R.id.btnOpenSetReminder);

        reminders = new ArrayList<>();
        adapter = new ReminderAdapter(reminders);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        btnOpenSetReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the Date Picker, Time Picker, and Save Button
                setDatePicker.setVisibility(View.VISIBLE);
                setTimePicker.setVisibility(View.VISIBLE);
                saveReminderButton.setVisibility(View.VISIBLE);

                btnOpenSetReminder.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
        });

        saveReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminder();

                btnOpenSetReminder.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unregister the BroadcastReceiver when the fragment is destroyed
        requireContext().unregisterReceiver(reminderBroadcastReceiver);
    }

    private void saveReminder() {
        int year = setDatePicker.getYear();
        int month = setDatePicker.getMonth();
        int dayOfMonth = setDatePicker.getDayOfMonth();
        int hour = setTimePicker.getCurrentHour();
        int minute = setTimePicker.getCurrentMinute();

        // Create a Calendar instance with the selected date and time
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, hour, minute);

        // Format the date and time as needed
        String reminderDateTime = String.format("%04d-%02d-%02d %02d:%02d", year, month + 1, dayOfMonth, hour, minute);

        // Add the formatted date and time to the list
        reminders.add(reminderDateTime);
        adapter.notifyDataSetChanged();

        // Hide the date picker, time picker, and save button
        setDatePicker.setVisibility(View.GONE);
        setTimePicker.setVisibility(View.GONE);
        saveReminderButton.setVisibility(View.GONE);

        // Show a toast message
        Toast.makeText(requireContext(), "Reminder saved for " + reminderDateTime, Toast.LENGTH_SHORT).show();
    }
}



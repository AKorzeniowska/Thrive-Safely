package com.example.thrive.thrivesafely;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.thrive.thrivesafely.notifications.NotificationUtils;
import com.example.thrive.thrivesafely.schedulers.ReminderUtilities;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ReminderUtilities.scheduleDailyReminder(this);
    }

    public void openPlants(View view){
        Intent plantIntent = new Intent(this, PlantsActivity.class);
        startActivity(plantIntent);
    }

    public void openCalendar (View view){
        Intent calendarIntent = new Intent(this, CalendarActivity.class);
        startActivity(calendarIntent);
    }

    public void createNotification (View view){
        NotificationUtils.remindUserToWater(this);
    }

}

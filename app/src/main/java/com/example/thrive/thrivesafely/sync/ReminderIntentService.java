package com.example.thrive.thrivesafely.sync;

import android.app.IntentService;
import android.content.Intent;

import com.example.thrive.thrivesafely.notifications.NotificationTasks;

public class ReminderIntentService extends IntentService {
    public ReminderIntentService() {
        super("ReminderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        NotificationTasks.executeTask(this, action);
    }
}

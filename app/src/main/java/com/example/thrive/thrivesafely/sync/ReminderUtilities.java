package com.example.thrive.thrivesafely.sync;

import android.content.Context;

import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class ReminderUtilities {

    private static final int DAILY_SCHEDULE_IN_HOURS = 12;
    private static final int POSTPONED_SCHEDULE_IN_MINUTES = 60;

    private static final int DAILY_IN_SECONDS = (int) TimeUnit.HOURS.toSeconds(DAILY_SCHEDULE_IN_HOURS);
    private static final int POSTPONED_IN_SECONDS = (int) TimeUnit.MINUTES.toSeconds(POSTPONED_SCHEDULE_IN_MINUTES);
    private static final int SYNC_FLEXTIME_IN_SECONDS = 60;

    private static final String DAILY_JOB_TAG = "daily-reminder";
    private static final String POSTPONED_JOB_TAG = "postponed-reminder";

    public static boolean isDailyInitialized = false;
    private static boolean isPostponedInitialized = false;


    synchronized public static void scheduleDailyReminder(final Context context) {
        if (isDailyInitialized) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job constraintJob = dispatcher.newJobBuilder()
                .setService(PostponedReminderFirebaseJobService.class)
                .setTag(DAILY_JOB_TAG)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(DAILY_IN_SECONDS, DAILY_IN_SECONDS + SYNC_FLEXTIME_IN_SECONDS))
                .setReplaceCurrent(true)
                .build();

        //todo: personalize intervals of notifications

        dispatcher.schedule(constraintJob);
        isDailyInitialized = true;
    }

    //todo: handle reminding notifications
}

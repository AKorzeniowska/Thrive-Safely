package com.example.thrive.thrivesafely.notifications;

import android.content.Context;

import com.example.thrive.thrivesafely.schedulers.ReminderUtilities;

public class NotificationTasks {
    public static final String ACTION_CONFIRM = "already-watered";
    public static final String ACTION_DISMISS = "remind-me-later";
    public static final String ACTION_REMINDER = "reminder";

    public static void executeTask (Context context, String action){
        if (action.equals(ACTION_CONFIRM)){
            holdLaterReminders(context);
        }
        else if (action.equals(ACTION_DISMISS)){
            issueLaterReminder(context);
        }
        else if (action.equals(ACTION_REMINDER)){
            issueDailyReminder(context);
        }
    }

    private static void issueDailyReminder(Context context){
        NotificationUtils.remindUserToWater(context);
    }

    private static void issueLaterReminder (Context context){
        if (!ReminderUtilities.isPostponedEnabled) {
            ReminderUtilities.isPostponedEnabled = true;
//            ReminderUtilities.schedulePostponedReminder(context);
        }
        NotificationUtils.clearAllNotifications(context);
    }

    private static void holdLaterReminders (Context context){
        ReminderUtilities.isPostponedEnabled = false;
        ReminderUtilities.cancelPostponedReminder(context);
        NotificationUtils.clearAllNotifications(context);
        ReminderUtilities.isPostponedInitialized = false;
    }

}

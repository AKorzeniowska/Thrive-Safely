package com.example.thrive.thrivesafely.notifications;

import android.content.Context;

public class NotificationTasks {
    public static final String ACTION_CONFIRM = "already-watered";
    public static final String ACTION_DISMISS = "remind-me-later";
    public static final String ACTION_LATER_REMINDER = "postponed-reminder";

    public static void executeTask (Context context, String action){
        if (action.equals(ACTION_CONFIRM)){

        }
        else if (action.equals(ACTION_DISMISS)){

        }
        else if (action.equals(ACTION_LATER_REMINDER)){
            issueLaterReminder(context);
        }
    }

    private static void issueLaterReminder (Context context){
        NotificationUtils.remindUserToWater(context);
    }

}

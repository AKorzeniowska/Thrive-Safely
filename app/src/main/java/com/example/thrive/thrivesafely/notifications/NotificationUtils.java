package com.example.thrive.thrivesafely.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.thrive.thrivesafely.CalendarActivity;
import com.example.thrive.thrivesafely.MainActivity;
import com.example.thrive.thrivesafely.R;

public class NotificationUtils {

    private static final int WATER_NOTIFICATION_ID = 1138;
    private static final int WATER_PENDING_INTENT_ID = 3417;
    private static final String WATER_NOTIFICATION_CHANNEL_ID = "watering_notification_channel";

    private static final int ACTION_DISMISS_PENDING_INTENT_ID = 7;
    private static final int ACTION_CONFIRM_PENDING_INTENT_ID = 8;

    private static PendingIntent contentIntent (Context context){
        Intent startActivityIntent = new Intent(context, CalendarActivity.class);
        return PendingIntent.getActivity(
                context,
                WATER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon (Context context){
        Resources resources = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(resources, R.drawable.icon_flowers);
        return largeIcon;
    }

    public static void remindUserToWater (Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = new NotificationChannel(
                WATER_NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.main_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH);


        notificationManager.createNotificationChannel(mChannel);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, WATER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.main_title))
                .setSmallIcon(R.drawable.small_notification_icon)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_body))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.notification_body)))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(remindMeLaterAction(context))
                .addAction(confirmAction((context)))
                .setAutoCancel(true);

        notificationManager.notify(WATER_NOTIFICATION_ID, notificationBuilder.build());
    }

    //todo: list plants that need watering in expanded notification

    public static void clearAllNotifications (Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private static NotificationCompat.Action remindMeLaterAction(Context context){
        Intent remindMeLaterIntent = new Intent(context, NotificationIntentService.class);
        remindMeLaterIntent.setAction(NotificationTasks.ACTION_DISMISS);
        PendingIntent remindLaterPendingIntent = PendingIntent.getService(
                context,
                ACTION_DISMISS_PENDING_INTENT_ID,
                remindMeLaterIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action remindLaterAction = new NotificationCompat.Action(
                R.drawable.icon_flowers,
                "Remind me later",
                remindLaterPendingIntent);

        return remindLaterAction;
    }

    private static NotificationCompat.Action confirmAction(Context context){
        Intent confirmIntent = new Intent(context, NotificationIntentService.class);
        confirmIntent.setAction(NotificationTasks.ACTION_CONFIRM);
        PendingIntent confirmPendingIntent = PendingIntent.getService(
                context,
                ACTION_CONFIRM_PENDING_INTENT_ID,
                confirmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action confirmAction = new NotificationCompat.Action(
                R.drawable.icon_flowers,
                "Already did it!",
                confirmPendingIntent);

        return confirmAction;
    }

//    private static NotificationCompat.Action quitReminding (Context context){
//        Intent quitRemindingIntent = new Intent(context, NotificationIntentService.class);
//        quitRemindingIntent.setAction(NotificationTasks.ACTION_CONFIRM);
//        PendingIntent quitRemindingPendingIntent
//    }

}

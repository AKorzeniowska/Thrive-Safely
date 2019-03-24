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
import com.example.thrive.thrivesafely.R;

public class NotificationUtils {

    private static final int WATER_NOTIFICATION_ID = 1138;
    private static final int WATER_PENDING_INTENT_ID = 3417;
    private static final String WATER_NOTIFICATION_CHANNEL_ID = "watering_notification_channel";

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
                .setAutoCancel(true);

        notificationManager.notify(WATER_NOTIFICATION_ID, notificationBuilder.build());
    }

    public static void clearAllNotifications (Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}

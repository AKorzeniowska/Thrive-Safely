package com.example.thrive.thrivesafely.notifications;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

import com.example.thrive.thrivesafely.CalendarActivity;
import com.example.thrive.thrivesafely.notifications.NotificationTasks;
import com.example.thrive.thrivesafely.data.PlantContract.PlantEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationIntentService extends IntentService {
    private List<Integer> idsToUpdate = new ArrayList<>();

    public NotificationIntentService() {
        super("DatabaseUpdateIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (action.equals(NotificationTasks.ACTION_CONFIRM)){
            updateData();
        }

        NotificationTasks.executeTask(this, action);
    }




    private void getDataToUpdate() {
        String[] projection = {PlantEntry._ID, PlantEntry.COLUMN_WATERING, PlantEntry.COLUMN_LAST_WATERING};
        Cursor cursor = getContentResolver().query(PlantEntry.CONTENT_URI, projection, null, null, null);

        int idColumnIndex = cursor.getColumnIndex(PlantEntry._ID);
        int wateringColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_WATERING);
        int lastWateringColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_LAST_WATERING);

        Integer id;
        Integer nextWatering;
        Integer wateringFrequency;
        String lastWatering;

        while (cursor.moveToNext()) {
            id = cursor.getInt(idColumnIndex);
            wateringFrequency = cursor.getInt(wateringColumnIndex);
            lastWatering = cursor.getString(lastWateringColumnIndex);

            nextWatering = CalendarActivity.nextWateringDay(lastWatering, wateringFrequency);
            if (nextWatering <= 0) {
                idsToUpdate.add(id);
            }
        }
        cursor.close();
    }

    private void updateData(){
        getDataToUpdate();
        for (Integer id : idsToUpdate){
            updateWateringDate(id);
        }
    }


    public void updateWateringDate(int id) {
        Date todayDate = Calendar.getInstance().getTime();
        String today = CalendarActivity.dateFormat.format(todayDate);

        ContentValues values = new ContentValues();
        values.put(PlantEntry.COLUMN_LAST_WATERING, today);

        getContentResolver().update(PlantEntry.CONTENT_URI_ID(id), values, null, null);
    }
}

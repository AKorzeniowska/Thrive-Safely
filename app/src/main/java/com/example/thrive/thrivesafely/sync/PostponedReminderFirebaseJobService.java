package com.example.thrive.thrivesafely.sync;

import com.example.thrive.thrivesafely.CalendarActivity;
import com.example.thrive.thrivesafely.notifications.NotificationTasks;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Pair;
import com.example.thrive.thrivesafely.data.PlantContract.PlantEntry;

import java.util.ArrayList;

public class PostponedReminderFirebaseJobService extends JobService {

    private AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters params) {
        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = PostponedReminderFirebaseJobService.this;
                if (checkIfAnyToWater()) {
                    NotificationTasks.executeTask(context, NotificationTasks.ACTION_LATER_REMINDER);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(params, false);
            }
        };

        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (mBackgroundTask != null)
            mBackgroundTask.cancel(true);
        return true;
    }


    private boolean checkIfAnyToWater(){
        String [] projection = {PlantEntry.COLUMN_WATERING, PlantEntry.COLUMN_LAST_WATERING};
        Cursor cursor = getContentResolver().query(PlantEntry.CONTENT_URI, projection, null, null, null);

        int wateringColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_WATERING);
        int lastWateringColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_LAST_WATERING);

        Integer nextWatering;
        Integer wateringFrequency;
        String lastWatering;

        while (cursor.moveToNext()){
            wateringFrequency = cursor.getInt(wateringColumnIndex);
            lastWatering = cursor.getString(lastWateringColumnIndex);

            nextWatering = CalendarActivity.nextWateringDay(lastWatering, wateringFrequency);
            if (nextWatering <= 0) { return true; }
        }
        cursor.close();
        return false;
    }


}

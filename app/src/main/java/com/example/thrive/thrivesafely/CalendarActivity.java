package com.example.thrive.thrivesafely;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.thrive.thrivesafely.data.PlantDBHelper;
import com.example.thrive.thrivesafely.data.PlantProvider;
import com.example.thrive.thrivesafely.data.PlantContract.PlantEntry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CalendarActivity extends AppCompatActivity {
    private TextView textView;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
    private Date currentDate;
    private Calendar calendarCurrentDate;

    private PlantDBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mDbHelper = new PlantDBHelper(this);

        currentDate = new Date();
        calendarCurrentDate = Calendar.getInstance();
        calendarCurrentDate.setTime(currentDate);
        textView = findViewById(R.id.textView);
        addDateIfWatering();
    }

    private void addDateIfWatering(){
        calendarCurrentDate.roll(Calendar.DAY_OF_MONTH, 3);
        int day = calendarCurrentDate.get(Calendar.DAY_OF_MONTH);

        Date dateOfInput = null;        //date of input from database
        try {
            dateOfInput = dateFormat.parse("2019/01/02");
        } catch (ParseException e) {
            e.printStackTrace();
            textView.setText("unparseable");
        }

        long diffBetweenDatesInMillies = Math.abs(currentDate.getTime() - dateOfInput.getTime());
        int diffBetweenDatesInDays = (int) TimeUnit.DAYS.convert(diffBetweenDatesInMillies, TimeUnit.MILLISECONDS);

        int frequencyOfWatering = 2;       // frequency of watering from database
        int diffBetweenTodayAndWateringDay = diffBetweenDatesInDays % frequencyOfWatering;
        if (diffBetweenTodayAndWateringDay == 0){
            //today is the watering day
        }
        else{
            int daysUntilWatering = frequencyOfWatering - diffBetweenTodayAndWateringDay;
            calendarCurrentDate.roll(Calendar.DAY_OF_MONTH, daysUntilWatering);    // next day that will be watering day
        }

        Calendar firstDayOfCurrentMonth = Calendar.getInstance();
        firstDayOfCurrentMonth.set(calendarCurrentDate.get(Calendar.YEAR), calendarCurrentDate.get(Calendar.MONTH), 1);

        /*
        ...
         */
    }
}

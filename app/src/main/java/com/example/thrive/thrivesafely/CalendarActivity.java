package com.example.thrive.thrivesafely;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.thrive.thrivesafely.data.PlantDBHelper;
import com.example.thrive.thrivesafely.data.PlantProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarActivity extends AppCompatActivity {
    private TextView textView;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private Date currentDate;
    private Calendar calendar;

    private PlantDBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mDbHelper = new PlantDBHelper(this);

        calendar = Calendar.getInstance();
        currentDate = new Date();
        calendar.setTime(currentDate);
        textView = findViewById(R.id.textView);
        selectIfWatering();
    }

    private void selectIfWatering(){
        calendar.roll(Calendar.DAY_OF_MONTH, 3);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        textView.setText(String.valueOf(day));
    }
}

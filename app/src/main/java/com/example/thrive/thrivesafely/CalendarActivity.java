package com.example.thrive.thrivesafely;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.thrive.thrivesafely.data.ListViewAdapter;
import com.example.thrive.thrivesafely.data.PlantDBHelper;
import com.example.thrive.thrivesafely.data.PlantProvider;
import com.example.thrive.thrivesafely.data.PlantContract.PlantEntry;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CalendarActivity extends AppCompatActivity {
    private ArrayList<HashMap<String, String>> dataForListView = new ArrayList<>();
    private ListView listView;
    private View header;

    private static final DateFormat dateFormat = new SimpleDateFormat(PlantEntry.DATE_FORMAT_PATTERN, Locale.ENGLISH);
    private PlantDBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mDbHelper = new PlantDBHelper(this);

        dataSetter();
        
        listView = (ListView) findViewById(R.id.listview_calendar);
        header = getLayoutInflater().inflate(R.layout.listview_header, listView, false);
        ListViewAdapter adapter = new ListViewAdapter(this, dataForListView);
        TextView textView = header.findViewById(R.id.textView);
        textView.setText(getString(R.string.water_us_later));
        listView.addHeaderView(header, null, false);
        listView.setAdapter(adapter);
    }

    private List<Pair<String, Integer>> dataGetter(){
        List<Pair<String, Integer>> nextWateringForEachPlant = new ArrayList<>();

        String [] projection = {PlantEntry.COLUMN_NAME, PlantEntry.COLUMN_SPECIES, PlantEntry.COLUMN_WATERING, PlantEntry.COLUMN_LAST_WATERING};
        Cursor cursor = getContentResolver().query(PlantEntry.CONTENT_URI, projection, null, null, null);

        int nameColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_NAME);
        int speciesColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_SPECIES);
        int wateringColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_WATERING);
        int lastWateringColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_LAST_WATERING);

        Integer nextWatering;
        String nameOrSpecies;
        Integer wateringFrequency;
        String lastWatering;

        while (cursor.moveToNext()){
            nameOrSpecies = cursor.getString(nameColumnIndex);
            if (nameOrSpecies.equals("")){
                nameOrSpecies = cursor.getString(speciesColumnIndex);
            }
            wateringFrequency = cursor.getInt(wateringColumnIndex);
            lastWatering = cursor.getString(lastWateringColumnIndex);

            nextWatering = nextWateringDay(lastWatering, wateringFrequency);
            nextWateringForEachPlant.add(new Pair<>(nameOrSpecies, nextWatering));
        }

        Collections.sort(nextWateringForEachPlant, new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                if (o1.second > o2.second) { return 1; }
                else if (o1.second.equals(o2.second)) { return 0; }
                else { return -1; }
            }
        });

        return nextWateringForEachPlant;
    }

    private void dataSetter (){
        List<Pair<String, Integer>> nextWateringForEachPlant = dataGetter();
        HashMap<String,String> mapForPlant;
        for (Pair<String, Integer> x : nextWateringForEachPlant){
            mapForPlant = new HashMap<>();
            mapForPlant.put(ListViewAdapter.FIRST_COLUMN, x.first);
            switch (x.second) {
                case 0:
                    mapForPlant.put(ListViewAdapter.SECOND_COLUMN, getString(R.string.water_today));
                    break;
                case 1:
                    mapForPlant.put(ListViewAdapter.SECOND_COLUMN, getString(R.string.water_tomorrow));
                    break;
                default:
                    mapForPlant.put(ListViewAdapter.SECOND_COLUMN, getString(R.string.water_in_X_days, x.second));
            }
            dataForListView.add(mapForPlant);
        }
    }

    private int nextWateringDay (String lastWateringText, int wateringFrequency){
        Date todayDate = Calendar.getInstance().getTime();
        Date lastWatering = null;
        try {
            lastWatering = dateFormat.parse(lastWateringText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diffBetweenDatesInMillies = Math.abs(todayDate.getTime() - lastWatering.getTime());
        int diffBetweenDatesInDays = (int) TimeUnit.DAYS.convert(diffBetweenDatesInMillies, TimeUnit.MILLISECONDS);

        return wateringFrequency - diffBetweenDatesInDays;
    }

}

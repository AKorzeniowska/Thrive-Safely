package com.example.thrive.thrivesafely;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
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
    private ArrayList<HashMap<String, String>> dataForLaterListView = new ArrayList<>();
    private ArrayList<String> dataForTodayListView = new ArrayList<>();
    private ArrayList<HashMap<String, String>> dataForForgottenListView = new ArrayList<>();

    private ArrayList<Integer> todayIdList = new ArrayList<>();
    private ArrayList<Integer> forgottenIdList = new ArrayList<>();

    private ListView laterListView;
    private ListView todayListView;
    private ListView forgottenListView;

    private static final DateFormat dateFormat = new SimpleDateFormat(PlantEntry.DATE_FORMAT_PATTERN, Locale.ENGLISH);
    private PlantDBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mDbHelper = new PlantDBHelper(this);

        dataSetter();
        listViewFiller();
        setListeners();
    }

    private List<Pair<Integer, Pair<String, Integer>>> dataGetter(){
        List<Pair<Integer, Pair<String, Integer>>> nextWateringForEachPlant = new ArrayList<>();

        String [] projection = {PlantEntry._ID, PlantEntry.COLUMN_NAME, PlantEntry.COLUMN_SPECIES, PlantEntry.COLUMN_WATERING, PlantEntry.COLUMN_LAST_WATERING};
        Cursor cursor = getContentResolver().query(PlantEntry.CONTENT_URI, projection, null, null, null);

        int idColumnIndex = cursor.getColumnIndex(PlantEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_NAME);
        int speciesColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_SPECIES);
        int wateringColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_WATERING);
        int lastWateringColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_LAST_WATERING);

        Integer id;
        Integer nextWatering;
        String nameOrSpecies;
        Integer wateringFrequency;
        String lastWatering;

        while (cursor.moveToNext()){
            id = cursor.getInt(idColumnIndex);
            nameOrSpecies = cursor.getString(nameColumnIndex);

            if (nameOrSpecies.equals("")){
                nameOrSpecies = cursor.getString(speciesColumnIndex);
            }
            wateringFrequency = cursor.getInt(wateringColumnIndex);
            lastWatering = cursor.getString(lastWateringColumnIndex);

            nextWatering = nextWateringDay(lastWatering, wateringFrequency);
            nextWateringForEachPlant.add(new Pair<>(id, new Pair<>(nameOrSpecies, nextWatering)));
        }

        cursor.close();

        Collections.sort(nextWateringForEachPlant, new Comparator<Pair<Integer, Pair<String, Integer>>>() {
            @Override
            public int compare(Pair<Integer, Pair<String, Integer>> o1, Pair<Integer, Pair<String, Integer>> o2) {
                if (o1.second.second > o2.second.second)
                    return 1;
                else if (o1.second.second.equals(o2.second.second))
                    return  0;
                else
                    return -1;
            }
        });
        return nextWateringForEachPlant;
    }


    private void dataSetter (){
        dataForForgottenListView.clear();
        dataForTodayListView.clear();
        dataForLaterListView.clear();
        todayIdList.clear();
        forgottenIdList.clear();

        List<Pair<Integer, Pair<String, Integer>>> nextWateringForEachPlant = dataGetter();
        HashMap<String,String> mapForPlant;

        for (Pair<Integer, Pair<String, Integer>> currentPlant : nextWateringForEachPlant){
            if (currentPlant.second.second < 0){
                mapForPlant = new HashMap<>();
                mapForPlant.put(ListViewAdapter.FIRST_COLUMN, currentPlant.second.first);
                switch (Math.abs(currentPlant.second.second)) {
                    case 1:
                        mapForPlant.put(ListViewAdapter.SECOND_COLUMN, getString(R.string.water_yesterday));
                        break;
                    default:
                        mapForPlant.put(ListViewAdapter.SECOND_COLUMN, getString(R.string.water_X_days_ago, Math.abs(currentPlant.second.second)));
                }
                dataForForgottenListView.add(mapForPlant);
                forgottenIdList.add(currentPlant.first);

            }
            else if (currentPlant.second.second.equals(0)){
                dataForTodayListView.add(currentPlant.second.first);
                todayIdList.add(currentPlant.first);
            }
            else {
                mapForPlant = new HashMap<>();
                mapForPlant.put(ListViewAdapter.FIRST_COLUMN, currentPlant.second.first);

                switch (currentPlant.second.second) {
                    case 1:
                        mapForPlant.put(ListViewAdapter.SECOND_COLUMN, getString(R.string.water_tomorrow));
                        break;
                    default:
                        mapForPlant.put(ListViewAdapter.SECOND_COLUMN, getString(R.string.water_in_X_days, currentPlant.second.second));
                }
                dataForLaterListView.add(mapForPlant);
            }
        }
    }


    private void listViewFiller (){
        laterListView = (ListView) findViewById(R.id.listview_later);
        ListViewAdapter adapterLater = new ListViewAdapter(this, dataForLaterListView);
        laterListView.setAdapter(adapterLater);

        todayListView = (ListView) findViewById(R.id.listview_today);
        ArrayAdapter<String> adapterToday = new ArrayAdapter<>(this, R.layout.listview_row_layout, dataForTodayListView);
        todayListView.setAdapter(adapterToday);

        forgottenListView = (ListView) findViewById(R.id.listview_forgotten);
        TextView forgottenTextView = (TextView) findViewById(R.id.textview_forgotten);
        if (dataForForgottenListView.size() == 0){
            forgottenListView.setVisibility(View.GONE);
            forgottenTextView.setVisibility(View.GONE);

            LinearLayout.LayoutParams laterParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0,
                    9.0f
            );
            laterListView.setLayoutParams(laterParam);
            return;
        }

        ListViewAdapter adapterForgotten = new ListViewAdapter(this, dataForForgottenListView);
        forgottenListView.setAdapter(adapterForgotten);
    }

    private void setListeners(){
        forgottenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String plantName = dataForForgottenListView.get(position).get(ListViewAdapter.FIRST_COLUMN);
                showConfirmWateringDialog(forgottenIdList.get(position), plantName);
            }
        });

        todayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showConfirmWateringDialog(todayIdList.get(position), dataForTodayListView.get(position));
            }
        });
    }

    private int nextWateringDay (String lastWateringText, int wateringFrequency){
        Date todayDate = Calendar.getInstance().getTime();
        Date lastWatering = null;
        try {
            lastWatering = dateFormat.parse(lastWateringText);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diffBetweenDatesInMillies = todayDate.getTime() - lastWatering.getTime();
        int diffBetweenDatesInDays = (int) TimeUnit.DAYS.convert(diffBetweenDatesInMillies, TimeUnit.MILLISECONDS);

        return wateringFrequency - diffBetweenDatesInDays;
    }


    private void updateWateringDate(int id){
        Date todayDate = Calendar.getInstance().getTime();
        String today = dateFormat.format(todayDate);

        ContentValues values = new ContentValues();
        values.put(PlantEntry.COLUMN_LAST_WATERING, today);

        getContentResolver().update(PlantEntry.CONTENT_URI_ID(id), values, null, null);
    }

    private void showConfirmWateringDialog(final int plantId, final String plantName){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
        TextView dialogMessage = view.findViewById(R.id.dialog_message);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogMessage.setVisibility(View.GONE);
        dialogTitle.setText(getString(R.string.confirm_watering_dialog, plantName));
        builder.setView(view);


        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                updateWateringDate(plantId);
                dataSetter();
                listViewFiller();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

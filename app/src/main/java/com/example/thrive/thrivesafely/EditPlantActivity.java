package com.example.thrive.thrivesafely;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thrive.thrivesafely.data.PlantDBHelper;
import com.example.thrive.thrivesafely.data.PlantContract.PlantEntry;
import com.example.thrive.thrivesafely.exceptions.IllegalInputData;

import java.util.Calendar;
import java.util.Date;

public class EditPlantActivity extends AppCompatActivity {
    private final static String FINAL_PLANT_ID = "final_plant_id";
    private PlantDBHelper mDbHelper;

    private CalendarView mCalendarView;

    private Integer id;
    private String name;
    private String species;
    private Integer watering;
    private Integer fertilizing;
    private Integer minTemperature;
    private String lastWatering;

    private EditText mNameEditText;
    private EditText mSpeciesEditText;
    private EditText mWateringEditText;
    private EditText mFertilizingEditText;
    private EditText mMinTempEditText;
    private EditText mLastWateringEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plant);
        mDbHelper = new PlantDBHelper(this);
        id = getIntent().getIntExtra(FINAL_PLANT_ID, 0);

        mNameEditText = findViewById(R.id.name_edit_text);
        mSpeciesEditText = findViewById(R.id.species_edit_text);
        mWateringEditText = findViewById(R.id.watering_edit_text);
        mFertilizingEditText = findViewById(R.id.fertilizing_edit_text);
        mMinTempEditText = findViewById(R.id.min_temp_edit_text);
        mLastWateringEditText = findViewById(R.id.last_watering_edit_text);
        mCalendarView = (CalendarView) findViewById(R.id.edit_plant_calendar);
        mCalendarView.setVisibility(View.GONE);

        dataGetter();
        dataSetter();
        setPickDateListener();
    }

    private void dataGetter (){
        String [] projection = {PlantEntry.COLUMN_NAME,
                PlantEntry.COLUMN_SPECIES,
                PlantEntry.COLUMN_WATERING,
                PlantEntry.COLUMN_FERTILIZING,
                PlantEntry.COLUMN_MIN_TEMP,
                PlantEntry.COLUMN_LAST_WATERING
        };
        String selection = PlantEntry._ID + "=?";
        String [] selectionArgs = {String.valueOf(id)};

        Cursor cursor = getContentResolver().query(PlantEntry.CONTENT_URI_ID(id), projection, selection, selectionArgs, null);

        int nameColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_NAME);
        int speciesColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_SPECIES);
        int wateringColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_WATERING);
        int fertilizingColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_FERTILIZING);
        int minTempColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_MIN_TEMP);
        int lastWateringColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_LAST_WATERING);

        while (cursor.moveToNext()){
            name = cursor.getString(nameColumnIndex);
            species = cursor.getString(speciesColumnIndex);
            watering = cursor.getInt(wateringColumnIndex);
            fertilizing = cursor.getInt(fertilizingColumnIndex);
            minTemperature = cursor.getInt(minTempColumnIndex);
            lastWatering = cursor.getString(lastWateringColumnIndex);
        }
        cursor.close();
    }

    private void dataSetter(){
        mNameEditText.setText(name);
        mSpeciesEditText.setText(species);
        mWateringEditText.setText(String.valueOf(watering));
        mLastWateringEditText.setText(lastWatering);
        if (fertilizing != 0)
            mFertilizingEditText.setText(String.valueOf(fertilizing));
        else
            mFertilizingEditText.setText("");
        if (minTemperature != 0)
            mMinTempEditText.setText(String.valueOf(minTemperature));
        else
            mMinTempEditText.setText("");
    }

    public void submitUpdatedPlant(View view){
        if (updatePlant()) {
            Intent chosenPlantIntent = new Intent(EditPlantActivity.this, ChosenPlantActivity.class);
            chosenPlantIntent.putExtra(FINAL_PLANT_ID, id);
            startActivity(chosenPlantIntent);
            finish();
        }
    }

    private boolean updatePlant (){
        String nameString = mNameEditText.getText().toString().trim();
        String speciesString = mSpeciesEditText.getText().toString().trim();
        String wateringString = mWateringEditText.getText().toString().trim();
        String fertilizingString = mFertilizingEditText.getText().toString().trim();
        String minTempString = mMinTempEditText.getText().toString().trim();
        String lastWateringString = mLastWateringEditText.getText().toString().trim();

        try{
            if (nameString.equals("") && speciesString.equals("")) {
                throw new IllegalInputData(this, IllegalInputData.NO_GIVEN_NAME_OR_SPECIES);
            }
            if (wateringString.equals("")){
                throw new IllegalInputData(this, IllegalInputData.NO_GIVEN_WATERING);
            }
            if (wateringString.matches("0[0-9]*") || !wateringString.matches("[0-9]+")){
                throw new IllegalInputData(this, IllegalInputData.INVALID_WATERING_INPUT);
            }
            if (lastWateringString.equals("")){
                throw new IllegalInputData(this, IllegalInputData.NO_GIVEN_LAST_WATERING);
            }
        } catch (IllegalInputData ex) { return false; }

        int wateringInt = Integer.parseInt(wateringString);
        Integer fertilizingInt = null;
        Integer minTempInt = null;

        if (!fertilizingString.equals("")){
            fertilizingInt = Integer.parseInt(fertilizingString);
        }
        if (!minTempString.equals("")){
            minTempInt = Integer.parseInt(minTempString);
        }

        ContentValues values = new ContentValues();
        values.put(PlantEntry.COLUMN_NAME, nameString);
        values.put(PlantEntry.COLUMN_SPECIES, speciesString);
        values.put(PlantEntry.COLUMN_WATERING, wateringInt);
        values.put(PlantEntry.COLUMN_FERTILIZING, fertilizingInt);
        values.put(PlantEntry.COLUMN_MIN_TEMP, minTempInt);
        values.put(PlantEntry.COLUMN_LAST_WATERING, lastWateringString);

        int rows = getContentResolver().update(PlantEntry.CONTENT_URI_ID(id), values, null, null);
        if (rows == 0){
            Toast.makeText(this, "Editing plant failed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Plant has been edited successfully", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void datePicker(){
        mCalendarView.setVisibility(View.VISIBLE);
        Date today = Calendar.getInstance().getTime();
        mCalendarView.setDate(today.getTime());

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String dayFixed = String.valueOf(dayOfMonth);
                String monthFixed = String.valueOf(month+1);
                if (month < 10){
                    monthFixed = "0" + monthFixed;
                }

                if (dayOfMonth < 10){
                    dayFixed = "0" + dayFixed;
                }
                String chosenDate = dayFixed + "-" + monthFixed + "-" + year;
                mLastWateringEditText.setText(chosenDate);
                mCalendarView.setVisibility(View.GONE);
            }
        });
    }

    private void setPickDateListener(){
        mLastWateringEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });
    }
}

package com.example.thrive.thrivesafely;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.thrive.thrivesafely.data.PlantContract.PlantEntry;
import com.example.thrive.thrivesafely.data.PlantDBHelper;


public class ChosenPlantActivity extends AppCompatActivity {
    private static final String FINAL_PLANT_NAME = "final_plant_name";
    PlantDBHelper mDbHelper;

    private String name;
    private String species;
    private Integer watering;
    private Integer fertilizing;
    private Integer minTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen_plant);
        mDbHelper = new PlantDBHelper(this);
        String name = getIntent().getStringExtra(FINAL_PLANT_NAME);
        dataGetter(name);
        dataSetter();
    }

    protected void dataGetter (String plantName){
        String [] projection = {PlantEntry.COLUMN_NAME,
                PlantEntry.COLUMN_SPECIES,
                PlantEntry.COLUMN_WATERING,
                PlantEntry.COLUMN_FERTILIZING,
                PlantEntry.COLUMN_MIN_TEMP
        };
        String selection = PlantEntry.COLUMN_NAME + "=?";
        String [] selectionArgs = {plantName};

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(PlantEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        int nameColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_NAME);
        int speciesColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_SPECIES);
        int wateringColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_WATERING);
        int fertilizingColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_FERTILIZING);
        int minTempColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_MIN_TEMP);

        while (cursor.moveToNext()){
            name = cursor.getString(nameColumnIndex);
            species = cursor.getString(speciesColumnIndex);
            watering = cursor.getInt(wateringColumnIndex);
            fertilizing = cursor.getInt(fertilizingColumnIndex);
            minTemperature = cursor.getInt(minTempColumnIndex);
        }
        cursor.close();
    }

    private void dataSetter (){
        TextView nameText = (TextView) findViewById(R.id.plant_name_textview);
        TextView speciesText = (TextView) findViewById(R.id.plant_species_textview);
        TextView wateringText = (TextView) findViewById(R.id.watering_frequency_text);
        TextView fertilizingText = (TextView) findViewById(R.id.fertilizing_frequency_text);
        TextView minTempText = (TextView) findViewById(R.id.acceptable_temperature_text);

        nameText.setText(name);
        speciesText.setText(species);
        wateringText.setText(getString(R.string.how_often_text, watering));
        fertilizingText.setText(getString(R.string.how_often_text, fertilizing));
        minTempText.setText(getString(R.string.higher_than_temp_text, minTemperature));
    }
}

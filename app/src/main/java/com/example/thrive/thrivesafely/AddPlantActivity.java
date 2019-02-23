package com.example.thrive.thrivesafely;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thrive.thrivesafely.data.PlantContract;
import com.example.thrive.thrivesafely.data.PlantContract.PlantEntry;
import com.example.thrive.thrivesafely.data.PlantDBHelper;
import com.example.thrive.thrivesafely.exceptions.IllegalInputData;

public class AddPlantActivity extends AppCompatActivity {
    private PlantDBHelper mDbHelper;
    private EditText mNameEditText;
    private EditText mSpeciesEditText;
    private EditText mWateringEditText;
    private EditText mFertilizingEditText;
    private EditText mMinTempEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
        mDbHelper = new PlantDBHelper(this);
        mNameEditText = findViewById(R.id.name_edit_text);
        mSpeciesEditText = findViewById(R.id.species_edit_text);
        mWateringEditText = findViewById(R.id.watering_edit_text);
        mFertilizingEditText = findViewById(R.id.fertilizing_edit_text);
        mMinTempEditText = findViewById(R.id.min_temp_edit_text);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public void submitInsertedPlant(View view){
        if (insertPlant()) {
            Intent plantsIntent = new Intent(AddPlantActivity.this, PlantsActivity.class);
            startActivity(plantsIntent);
            finish();
        }
    }

    private boolean insertPlant (){
        String nameString = mNameEditText.getText().toString().trim();
        String speciesString = mSpeciesEditText.getText().toString().trim();
        String wateringString = mWateringEditText.getText().toString().trim();
        String fertilizingString = mFertilizingEditText.getText().toString().trim();
        String minTempString = mMinTempEditText.getText().toString().trim();

        try{
            if (nameString.equals("") && speciesString.equals("")) {
                throw new IllegalInputData(this, IllegalInputData.NO_GIVEN_NAME_OR_SPECIES);
            }
            if (wateringString.equals("")){
                throw new IllegalInputData(this, IllegalInputData.NO_GIVEN_WATERING);
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

        Uri newUri = getContentResolver().insert(PlantEntry.CONTENT_URI, values);
        if (newUri == null){
            Toast.makeText(this, "Adding plant failed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Plant has been added successfully", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}

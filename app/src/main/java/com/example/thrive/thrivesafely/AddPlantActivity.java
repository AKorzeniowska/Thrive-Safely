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
//        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
//    private void displayDatabaseInfo() {
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//
//        String[] projection = {
//                PlantEntry._ID,
//                PlantEntry.COLUMN_NAME,
//                PlantEntry.COLUMN_SPECIES,
//                PlantEntry.COLUMN_WATERING,
//                PlantEntry.COLUMN_FERTILIZING,
//                PlantEntry.COLUMN_MIN_TEMP };
//
//        Cursor cursor = db.query(
//                PlantEntry.TABLE_NAME,   // The table to query
//                projection,            // The columns to return
//                null,                  // The columns for the WHERE clause
//                null,                  // The values for the WHERE clause
//                null,                  // Don't group the rows
//                null,                  // Don't filter by row groups
//                null);                   // The sort order
//
//        TextView displayView = (TextView) findViewById(R.id.addPlantTextView);
//
//        displayView.setText("The pets table contains " + cursor.getCount() + " pets.\n\n");
//        displayView.append(PlantEntry._ID + " - " +
//                PlantEntry.COLUMN_NAME + " - " +
//                PlantEntry.COLUMN_SPECIES + " - " +
//                PlantEntry.COLUMN_WATERING + " - " +
//                PlantEntry.COLUMN_FERTILIZING + "-" +
//                PlantEntry.COLUMN_MIN_TEMP + "\n" );
//
//            int idColumnIndex = cursor.getColumnIndex(PlantEntry._ID);
//            int nameColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_NAME);
//            int speciesColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_SPECIES);
//            int wateringColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_WATERING);
//            int fertilizingColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_FERTILIZING);
//            int minTempColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_MIN_TEMP);
//
//            while (cursor.moveToNext()) {
//                int currentID = cursor.getInt(idColumnIndex);
//                String currentName = cursor.getString(nameColumnIndex);
//                String currentSpecies = cursor.getString(speciesColumnIndex);
//                int currentWatering = cursor.getInt(wateringColumnIndex);
//                int currentFertilizing = cursor.getInt(fertilizingColumnIndex);
//                int currentMinTemp = cursor.getInt(minTempColumnIndex);
//                displayView.append(("\n" + currentID + " - " +
//                        currentName + " - " +
//                        currentSpecies + " - " +
//                        currentWatering + " - " +
//                        currentFertilizing + " - " +
//                        currentMinTemp));
//            }
//            cursor.close();
//    }


//    public void insertingPlant(View view){
//        insertPlant();
//        displayDatabaseInfo();
//    }

//    private void insertPlant() {
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(PlantEntry.COLUMN_NAME, "Toto");
//        values.put(PlantEntry.COLUMN_SPECIES, "Terrier");
//        values.put(PlantEntry.COLUMN_WATERING, 5);
//        values.put(PlantEntry.COLUMN_FERTILIZING, 7);
//        values.put(PlantEntry.COLUMN_MIN_TEMP, 4);
//
//        long newRowId = db.insert(PlantEntry.TABLE_NAME, null, values);
//    }

    public void submitInsertedPlant(View view){
        insertPlant();
        Intent plantsIntent = new Intent(AddPlantActivity.this, PlantsActivity.class);
        startActivity(plantsIntent);
    }

    private void insertPlant (){
        String nameString = mNameEditText.getText().toString().trim();
        String speciesString = mSpeciesEditText.getText().toString().trim();
        String wateringString = mWateringEditText.getText().toString().trim();
        String fertilizingString = mFertilizingEditText.getText().toString().trim();
        String minTempString = mMinTempEditText.getText().toString().trim();

        int wateringInt = Integer.parseInt(wateringString);
        int fertilizingInt = Integer.parseInt(fertilizingString);
        int minTempInt = Integer.parseInt(minTempString);

        ContentValues values = new ContentValues();
        values.put(PlantEntry.COLUMN_NAME, nameString);
        values.put(PlantEntry.COLUMN_SPECIES, speciesString);
        values.put(PlantEntry.COLUMN_WATERING, wateringInt);
        values.put(PlantEntry.COLUMN_FERTILIZING, fertilizingInt);
        values.put(PlantEntry.COLUMN_MIN_TEMP, minTempInt);

        Uri newUri = getContentResolver().insert(PlantEntry.CONTENT_URI, values);
        if (newUri == null){
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }
    }

}

package com.example.thrive.thrivesafely;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thrive.thrivesafely.data.PlantContract.PlantEntry;
import com.example.thrive.thrivesafely.data.PlantDBHelper;


public class ChosenPlantActivity extends AppCompatActivity {
    private static final String FINAL_PLANT_ID = "final_plant_id";
    PlantDBHelper mDbHelper;

    private Integer id;
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
        id = getIntent().getIntExtra(FINAL_PLANT_ID, 0);
        dataGetter();
        dataSetter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataGetter();
        dataSetter();
    }

    protected void dataGetter (){
        String [] projection = {PlantEntry.COLUMN_NAME,
                PlantEntry.COLUMN_SPECIES,
                PlantEntry.COLUMN_WATERING,
                PlantEntry.COLUMN_FERTILIZING,
                PlantEntry.COLUMN_MIN_TEMP
        };
        String selection = PlantEntry._ID + "=?";
        String [] selectionArgs = {String.valueOf(id)};

        Cursor cursor = getContentResolver().query(PlantEntry.CONTENT_URI_ID(id), projection, selection, selectionArgs, null);

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

        TextView fertilizingIntroText = (TextView) findViewById(R.id.fertilizing_intro);
        TextView minTempIntroText = (TextView) findViewById(R.id.min_temp_intro);

        if (!name.equals("")) {
            nameText.setText(name);
            speciesText.setText(species);
        }
        else{ nameText.setText(species); }

        wateringText.setText(getString(R.string.how_often_text, watering));
        fertilizingText.setText(getString(R.string.how_often_text, fertilizing));
        minTempText.setText(getString(R.string.higher_than_temp_text, minTemperature));

        if (fertilizing == 0) {
            fertilizingIntroText.setText("");
            fertilizingText.setText("");
        }

        if (minTemperature == 0) {
            minTempIntroText.setText("");
            minTempText.setText("");
        }
    }

    public void editPlant (View view) {
        Intent editPlantIntent = new Intent(ChosenPlantActivity.this, EditPlantActivity.class);
        editPlantIntent.putExtra(FINAL_PLANT_ID, id);
        startActivity(editPlantIntent);
    }

    public void deletePlantButton (View view) {
        showDeleteConfirmationDialog();
    }

    private void deletePlant (){
        int rows = getContentResolver().delete(PlantEntry.CONTENT_URI_ID(id), null, null);
        if (rows == 0){
            Toast.makeText(this, "Deleting plant failed", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Plant has been deleted successfully", Toast.LENGTH_SHORT).show();
        }
        Intent plantListIntent = new Intent(ChosenPlantActivity.this, PlantsActivity.class);
        startActivity(plantListIntent);
        finish();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_layout, null));

        builder.setPositiveButton(R.string.confirm_deletion, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deletePlant();
            }
        });
        builder.setNegativeButton(R.string.cancel_deletion, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        final Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams buttonLayoutParams = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
//        buttonLayoutParams.leftMargin = 5;
        positiveButton.setLayoutParams(buttonLayoutParams);
        negativeButton.setLayoutParams(buttonLayoutParams);
    }
}

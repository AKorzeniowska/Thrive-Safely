package com.example.thrive.thrivesafely;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thrive.thrivesafely.data.PlantDBHelper;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.thrive.thrivesafely.data.PlantContract.PlantEntry;

public class PlantsActivity extends AppCompatActivity {
    private PlantDBHelper mDbHelper;
    ArrayList<String> listViewData = new ArrayList<>();
    private final static String FINAL_PLANT_ID = "final_plant_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);
        mDbHelper = new PlantDBHelper(this);
        listGetter();
        chosenPlantIntentCreator();
    }

    @Override
    protected void onStart() {
        super.onStart();
        listGetter();
        chosenPlantIntentCreator();
    }

    protected void listGetter(){
        listViewData.clear();
        String [] projection = {PlantEntry.COLUMN_NAME};
        Cursor cursor = getContentResolver().query(PlantEntry.CONTENT_URI, projection, null, null, null);

        int nameColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_NAME);
        while (cursor.moveToNext()){
            String currentName = cursor.getString(nameColumnIndex);
            listViewData.add(currentName);
        }
        cursor.close();
    }

    protected void chosenPlantIntentCreator(){
        final ListView listView = (ListView) findViewById(R.id.plants_listView_dynamic);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_row_layout, listViewData);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent chosenPlantIntent = new Intent(PlantsActivity.this, ChosenPlantActivity.class);
                chosenPlantIntent.putExtra(FINAL_PLANT_ID, position);
                startActivity(chosenPlantIntent);
            }
        });
    }

    public void addPlant (View view){
        Intent intent = new Intent(this, AddPlantActivity.class);
        startActivity(intent);
    }
}

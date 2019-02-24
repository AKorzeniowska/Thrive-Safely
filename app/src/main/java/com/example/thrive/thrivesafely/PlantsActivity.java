package com.example.thrive.thrivesafely;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thrive.thrivesafely.data.PlantDBHelper;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.thrive.thrivesafely.data.PlantContract.PlantEntry;

import org.w3c.dom.Text;

public class PlantsActivity extends AppCompatActivity {
    private PlantDBHelper mDbHelper;
    private ArrayList<String> listViewData = new ArrayList<>();
    private ArrayList<Integer> idList = new ArrayList<>();
    private final static String FINAL_PLANT_ID = "final_plant_id";
    private EditText searchEditText;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plants);
        mDbHelper = new PlantDBHelper(this);
        searchEditText = (EditText) findViewById(R.id.search_for_plant);
        listView = (ListView) findViewById(R.id.plants_listView_dynamic);

        listGetter();
        setSearchListener();
        chosenPlantIntentCreator();
    }

    @Override
    protected void onStart() {
        super.onStart();
        listGetter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listGetter();
        chosenPlantIntentCreator();
    }

    private void setSearchListener(){
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = searchEditText.getText().toString().trim();
                listGetterByName(name);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void listGetter(){
        listViewData.clear();
        String [] projection = {PlantEntry._ID, PlantEntry.COLUMN_NAME, PlantEntry.COLUMN_SPECIES};
        Cursor cursor = getContentResolver().query(PlantEntry.CONTENT_URI, projection, null, null, null);

        int nameColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_NAME);
        int speciesColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_SPECIES);
        int idColumnIndex = cursor.getColumnIndex(PlantEntry._ID);

        while (cursor.moveToNext()){
            String currentName = cursor.getString(nameColumnIndex);
            Integer currentId = Integer.parseInt(cursor.getString(idColumnIndex));
            if (currentName.equals("")){
                currentName = cursor.getString(speciesColumnIndex);
            }
            listViewData.add(currentName);
            idList.add(currentId);
        }
        cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_row_layout, listViewData);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    private void chosenPlantIntentCreator(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent chosenPlantIntent = new Intent(PlantsActivity.this, ChosenPlantActivity.class);
                chosenPlantIntent.putExtra(FINAL_PLANT_ID, idList.get(position));
                startActivity(chosenPlantIntent);
                finish();
            }
        });
    }

    public void addPlant (View view){
        Intent intent = new Intent(this, AddPlantActivity.class);
        startActivity(intent);
        finish();
    }

    private void listGetterByName(String name){
        listViewData.clear();
        String [] projection = {PlantEntry._ID, PlantEntry.COLUMN_NAME, PlantEntry.COLUMN_SPECIES};
        String selection = PlantEntry.COLUMN_NAME + " LIKE ? COLLATE NOCASE OR " + PlantEntry.COLUMN_SPECIES + " LIKE ? COLLATE NOCASE";
        String [] selectionArgs = {"%"+name+"%", "%"+name+"%"};
        Cursor cursor = getContentResolver().query(PlantEntry.CONTENT_URI, projection, selection, selectionArgs, null);

        int nameColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_NAME);
        int speciesColumnIndex = cursor.getColumnIndex(PlantEntry.COLUMN_SPECIES);
        int idColumnIndex = cursor.getColumnIndex(PlantEntry._ID);

        while (cursor.moveToNext()){
            String currentName = cursor.getString(nameColumnIndex);
            Integer currentId = Integer.parseInt(cursor.getString(idColumnIndex));
            if (currentName.equals("")){
                currentName = cursor.getString(speciesColumnIndex);
            }
            listViewData.add(currentName);
            idList.add(currentId);
        }
        cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_row_layout, listViewData);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }
}

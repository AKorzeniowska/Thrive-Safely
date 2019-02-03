package com.example.thrive.thrivesafely.data;

import android.provider.BaseColumns;

public final class PlantContract {
    private  PlantContract () {}

    public static final class PlantEntry implements BaseColumns {
        public final static String TABLE_NAME = "plant_data";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_SPECIES = "species";
        public final static String COLUMN_WATERING = "watering";
        public final static String COLUMN_FERTILIZING = "fertilizing";
        public final static String COLUMN_MIN_TEMP = "min_temperature";

    }
}

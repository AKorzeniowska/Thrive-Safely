package com.example.thrive.thrivesafely.exceptions;

import android.content.Context;
import android.widget.Toast;

public class IllegalInputData extends Exception{
    public static final int NO_GIVEN_NAME_OR_SPECIES = 1;
    public static final int NO_GIVEN_WATERING = 2;
    public static final int INVALID_WATERING_INPUT = 3;
    public static final int INVALID_FERTILIZING_INPUT = 4;
    public static final int INVALID_MIN_TEMP_INPUT = 5;
    public static final int NO_GIVEN_LAST_WATERING = 6;

    public IllegalInputData(Context context, int code){
        switch (code){
            case 1:
                Toast.makeText(context, "You have to fill name or species field!", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(context, "You have to fill the watering field!", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(context, "Wrong input in watering field. \nCheck again!", Toast.LENGTH_SHORT).show();
                break;
//            case 4:
//                Toast.makeText(context, "Wrong input in fertilizing field. \nCheck again!", Toast.LENGTH_SHORT).show();
//                break;
//            case 5:
//                Toast.makeText(context, "Wrong input in min temperature field. \nCheck again!", Toast.LENGTH_SHORT).show();
//                break;
            case 6:
                Toast.makeText(context, "You have to fill the last watering field!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

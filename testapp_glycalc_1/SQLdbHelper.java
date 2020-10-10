package com.example.testapp_glycalc_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.widget.Toast;

public class SQLdbHelper extends SQLiteOpenHelper {

    public static final String COLUMN0 = "_id";
    public static final String COLUMN1 = "Blood_Sugar";
    public static final String COLUMN2 = "Last_Dose";
    public static final String COLUMN3 = "Meal_Details";
    public static final String TABLE_NAME = "BSTracker";
    public static final String DB_NAME = "BSTracker.db";
    public static final String SQLite_Sequence = "sqlite_sequence";
    public static final int VERSION = 1;

    private static final String CREATE_TABLE = " create table " + TABLE_NAME + " ( " + COLUMN0 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN1 + " INTEGER, "
            + COLUMN2 + " INTEGER, "
            + COLUMN3 + " TEXT, "
            + "CONSTRAINT UC_Values UNIQUE (" + COLUMN1 + "," + COLUMN2 + "," + COLUMN3 + "))";


    public static final String _ID = "_id";
    public Context context;

    public SQLdbHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String get_tableName(){
        return TABLE_NAME;
    }

    public void TriggerToast(String error_type){
        Toast.makeText(context, error_type, Toast.LENGTH_SHORT).show();
    }
}
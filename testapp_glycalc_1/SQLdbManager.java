package com.example.testapp_glycalc_1;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.testapp_glycalc_1.ui.dashboard.TrackerEntry;

import java.util.ArrayList;

public class SQLdbManager {
    private Context context;
    private SQLiteDatabase database;
    private SQLdbHelper dbHelper;
    ArrayList<TrackerEntry> after_last_entry_list = new ArrayList<>();
    ArrayList<TrackerEntry> before_last_entry_list = new ArrayList<>();

    public SQLdbManager(Context c, SQLdbHelper dbHelper) {
        this.context = c;
        this.dbHelper = dbHelper;
    }

    public SQLdbManager(Context c) {
        this.context = c;
    }

    public SQLdbManager open() throws SQLException {
        this.dbHelper = new SQLdbHelper(this.context);
        this.database = this.dbHelper.getWritableDatabase();
//        if(database.isOpen()) TriggerToast("DATABASE IS OPEN");
//        else if (!database.isOpen()) TriggerToast("DATABASE IS *NOT* OPEN");
        return this;
    }

    public void close() {
        this.dbHelper.close();
    }

    public void insert(int bloodSugar, int lastDose, String mealDetails) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(SQLdbHelper.COLUMN1, bloodSugar);
        contentValue.put(SQLdbHelper.COLUMN2, lastDose);
        contentValue.put(SQLdbHelper.COLUMN3, mealDetails);
        this.database.insert(SQLdbHelper.TABLE_NAME, null, contentValue);
    }

    public void insert(TrackerEntry entry) {
        ContentValues contentValue = new ContentValues();
        //Insulin is measured in International Units (units);
        // most insulin is U-100, which means that 100 units of insulin are equal to 1 mL.
        //1 unit = 0.01 ml
        contentValue.put(SQLdbHelper.COLUMN1, entry.getBlood_sugar());
        contentValue.put(SQLdbHelper.COLUMN2, entry.getLast_dose());
        contentValue.put(SQLdbHelper.COLUMN3, entry.getMeal_details());
        this.database.insert(SQLdbHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        Cursor cursor = this.database.query(SQLdbHelper.TABLE_NAME,
                new String[]{SQLdbHelper._ID, SQLdbHelper.COLUMN1, SQLdbHelper.COLUMN2, SQLdbHelper.COLUMN3},
                null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, int bloodSugar, int lastDose, String mealDetails) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLdbHelper.COLUMN1, bloodSugar);
        contentValues.put(SQLdbHelper.COLUMN2, lastDose);
        contentValues.put(SQLdbHelper.COLUMN3, mealDetails);
        return this.database.update(SQLdbHelper.TABLE_NAME, contentValues,
                "_id = " + _id, null);
    }

    public int update(long _id, TrackerEntry entry) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLdbHelper.COLUMN1, entry.getBlood_sugar());
        contentValues.put(SQLdbHelper.COLUMN2, entry.getLast_dose());
        contentValues.put(SQLdbHelper.COLUMN3, entry.getMeal_details());
        return this.database.update(SQLdbHelper.TABLE_NAME, contentValues,
                "_id = " + _id, null);
    }

    public void delete(long _id) {
        this.database.delete(SQLdbHelper.TABLE_NAME, "_id=" + _id, null);
    }

    public void delete_table_contents(String table_name){
        this.database.execSQL("DELETE FROM " + table_name);
    }

    public void clear_seq_col() {
        database.execSQL(" DELETE FROM sqlite_sequence");
    }

    public void TriggerToast(String error_type){
        Toast.makeText(context, error_type, Toast.LENGTH_SHORT).show();
    }
}



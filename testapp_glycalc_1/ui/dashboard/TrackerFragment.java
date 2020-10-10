package com.example.testapp_glycalc_1.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Build;
import android.os.Bundle;
import android.view.*;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testapp_glycalc_1.R;
import com.example.testapp_glycalc_1.SQLdbHelper;
import com.example.testapp_glycalc_1.SQLdbManager;
import com.facebook.stetho.Stetho;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.example.testapp_glycalc_1.SQLdbHelper.COLUMN1;
import static com.example.testapp_glycalc_1.SQLdbHelper.COLUMN2;
import static com.example.testapp_glycalc_1.SQLdbHelper.COLUMN3;
import static java.lang.Integer.parseInt;

public class TrackerFragment extends Fragment{
    private Context mContext;
    private EditText et_blood_sugar, et_last_dose, et_meal_details;
    private ListView listView;
    private SQLdbManager dbManager;
    //Temp copy of entry last deleted for Undo button.
    private int temp_bs, temp_ld, arr_size_b4, arr_size_aftr;
    private String temp_md;
    private TrackerEntry undo_temp;
    private ArrayList<TrackerEntry> entries_array;

    boolean fake_Data_Full = false;


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tracker, container, false);
        listView = root.findViewById(R.id.list_view);
        Stetho.initializeWithDefaults(mContext);
        mContext = getContext();
        SQLdbHelper dbHelper = new SQLdbHelper(mContext);
        dbManager = new SQLdbManager(mContext, dbHelper);
        dbManager.open();
        undo_temp = new TrackerEntry();
        update_listView();
        //ET's--------------------------------------------------
        et_blood_sugar = root.findViewById(R.id.ft_recentBS);
        et_last_dose = root.findViewById(R.id.ft_last_dose);
        et_meal_details = root.findViewById(R.id.ft_recent_meal_details);
        //Button's
        Button btn_Add = root.findViewById(R.id.btn_add);
        Button btn_Undo = root.findViewById(R.id.btn_undo);
        Button btn_Delete = root.findViewById(R.id.btn_delete);
////////////////
        Button btn_Developer = root.findViewById(R.id.btn_developer);
///////////////

        //btn_GetRow = root.findViewById(R.id.btn_get);
        //TO INSERT data into database
        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { add_entry(); }
        });
        //TO UNDO last entry deleted
        btn_Undo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { undo_deleted_entry(); }
        });
        //TO DELETE Last Entry
        btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                delete_entry();
            }
        });

        btn_Developer.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view) {
                //Add fake data
                if(!fake_Data_Full) {
                    TrackerEntry e1 = new TrackerEntry(189, 8, "Fresh veggies, raw and lightly steamed.");
                    TrackerEntry e2 = new TrackerEntry(99, 4, "Kale, Spinach, and Arugula");
                    TrackerEntry e3 = new TrackerEntry(149, 10, "Pulled BBQ Chicken and Asparagus");
                    TrackerEntry e4 = new TrackerEntry(237, 9, "Waffles, toast, and scrambled eggs.");
                    TrackerEntry e5 = new TrackerEntry(211, 8, "Tomato Bisque with Crackers");
                    TrackerEntry e6 = new TrackerEntry(277, 12, "Cafe Rio Pork Sliders and Salad");
                    TrackerEntry e7 = new TrackerEntry(131, 6, "French Toast, regular.");
                    TrackerEntry e8 = new TrackerEntry(177, 8, "Eggies in a basket");
                    TrackerEntry e9 = new TrackerEntry(199, 10, "Chicken with stuffed green peppers.");
                    dbManager.insert(new TrackerEntry(e1));
                    dbManager.insert(new TrackerEntry(e2));
                    dbManager.insert(new TrackerEntry(e3));
                    dbManager.insert(new TrackerEntry(e4));
                    dbManager.insert(new TrackerEntry(e5));
                    dbManager.insert(new TrackerEntry(e6));
                    dbManager.insert(new TrackerEntry(e7));
                    dbManager.insert(new TrackerEntry(e8));
                    dbManager.insert(new TrackerEntry(e9));
                    fake_Data_Full = true;
                } else { TriggerToast("[DEV]: Fake Data Already Exists."); }
            }
        });
        update_listView();
        return root;
    }






    //Attempt at making the contents of the buttons into functions.
    private void add_entry() {
//        Cursor cursor = dbManager.fetch();
        if (et_blood_sugar.getText().toString().trim().length() == 0) {
            TriggerToast("Please provide your last BS result.");
        } else if (et_last_dose.getText().toString().trim().length() == 0) {
            TriggerToast("Please provide your last insulin dose.");
        }
        if (et_meal_details.getText().toString().trim().length() != 0) {
            try {
                //Gets the input from ET's and stores in local variables
                int bs = parseInt(et_blood_sugar.getText().toString().trim());
                int ld = parseInt(et_last_dose.getText().toString().trim());
                String md = et_meal_details.getText().toString().trim();
                dbManager.insert(new TrackerEntry(bs, ld, md));
                TriggerToast("Success");
            } catch (Exception e) { TriggerToast(e.getMessage()); }
        } else TriggerToast("Please provide your most recent meal details.");
        TriggerToast("Success!");
        update_listView();
    }



    private void delete_entry() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Size of array before deletion.
                        arr_size_b4 = entries_array.size();
                        //Size of array with 1 less item.
                        arr_size_aftr = arr_size_b4 - 1;
                        Cursor cursor = dbManager.fetch();
                        if (cursor.moveToLast()) {
                            //Creation of temp TrackerEntry using table before item delete.
                            //Stores the "item to be deleted" in undo_temp.
                            temp_bs = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN1)));
                            temp_ld = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN2)));
                            temp_md = cursor.getString(cursor.getColumnIndex(COLUMN3));
                            try {
                                dbManager.delete((long) (cursor.getInt(cursor.getColumnIndex("_id"))));
                                dbManager.delete_table_contents("sqlite_sequence");
                                TriggerToast("Last Entry Deleted");
                            } catch (Exception e) {
                                TriggerToast("delete_entry() -> " + e.getMessage());
                            }
                        }
                        update_listView();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        //ListView listView = findViewById(R.id.list_view);
        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setMessage("Are you sure to want to delete your last entry?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    //Find a way to prevent duplicate entries and double undo's.
    private void undo_deleted_entry() {
        try {
            if (arr_size_b4 > arr_size_aftr) {
                Cursor cursor = dbManager.fetch();
                undo_temp = new TrackerEntry(temp_bs, temp_ld, temp_md);
                dbManager.insert(undo_temp);
                update_listView();
            } else { TriggerToast("Nothing to Undo."); }
        } catch (Exception e) {
            TriggerToast("undo_deleted_entry() -> " +
                    e.getMessage() + "Likely Due to size mismatch");
        }
        //if triggers when before > after, allowing insert of undo_temp, and then
        //set them equal so the if wont trigger again without another delete.
        arr_size_aftr = arr_size_b4;
    }

    //UPDATE BUTTON --to--> UPDATE FUNCTION (no button press to view updated list.)
    private void update_listView(){
//                dbManager.update(_id, new TrackerEntry(blood_sugar, last_dose, meal_details));
//                TriggerToastLong("Updated Successfully.");
        SQLdbManager dbManager = new SQLdbManager(mContext);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        entries_array = new ArrayList<>();//INIT of entries_array() Now: NonNull :D
        ArrayList<TrackerEntry> entries_array_copy = new ArrayList<>(); //Temp copy for undo.

        cursor.moveToFirst();//Move cursor to the first row/column in table
        //ERR: Index 0 requested with a size of 0.
        if (cursor.getCount() != 0) {
            add_to_entries_array(cursor, entries_array);
        }

        if (cursor.moveToNext()) { //after first values added, move to next until end.
            try {
                do add_to_entries_array(cursor, entries_array);
                while (cursor.moveToNext());
            } catch (Exception e) {
                TriggerToast(e.getMessage());
            }
        }
        entries_array_copy = entries_array;
        TrackerEntryAdapter te_adapter = new TrackerEntryAdapter(getContext(), entries_array);
        listView.setAdapter(te_adapter);
    }
    //USED BY: update_listView : Adds entries from table to array,
    private void add_to_entries_array(Cursor cursor,
                                      ArrayList<TrackerEntry> entries) {
        try {
            entries.add(new TrackerEntry(parseInt(cursor.getString(cursor.getColumnIndex(COLUMN1))),
                    parseInt(cursor.getString(cursor.getColumnIndex(COLUMN2))),
                    cursor.getString(cursor.getColumnIndex(COLUMN3))));
        } catch (CursorIndexOutOfBoundsException e) { TriggerToast("Cursor Out of Bounds: " + e.getMessage()); }
    }
    //Confirmation messages, compact for reuse.
    private void TriggerToast(String error_msg){
        Toast.makeText(getContext(), error_msg, Toast.LENGTH_SHORT).show();
    }

    public void set_Droplet_Color(){

    }
}

//====================================================================================================================
//    private class LongRunningTask extends AsyncTask<Void, Void, Void> {
//        // Runs in UI before background thread is called
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Do something like display a progress bar
//        }
//
//        // This is run in a background thread
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            return null;
//        }
//
//        // This runs in UI when background thread finishes
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            // Do things like hide the progress bar or change a TextView
//
//        }
//    }


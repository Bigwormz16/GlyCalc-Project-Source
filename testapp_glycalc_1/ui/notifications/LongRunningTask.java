package com.example.testapp_glycalc_1.ui.notifications;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

//======================================================================================
@SuppressLint("StaticFieldLeak")
public class LongRunningTask extends AsyncTask<Void,Void,Void> {
    @Override
    protected void onPreExecute() {
        Log.d("LRT:", " [onPreExecute() call]");
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids){
        Log.d("LRT:", " [doInBackground() call]");
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d("LRT:", " [onPostExecute() call]");
        super.onPostExecute(aVoid);
    }
}
//======================================================================================
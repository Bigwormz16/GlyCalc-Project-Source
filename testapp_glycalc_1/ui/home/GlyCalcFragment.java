package com.example.testapp_glycalc_1.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.testapp_glycalc_1.R;

public class GlyCalcFragment extends Fragment implements View.OnClickListener {

     private Button btnCalculate;
     private EditText etWeight, etRecent, etSugar, etCarbs;
     private TextView tvInsulin, tvSugarContrib, tvCarbContrib, tvBothContribs;
     private int ginc, cinc, insulin_rec;
     private String str_sugar, str_carbs, str_weight, str_recent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_glycalc, container, false);
        btnCalculate = root.findViewById(R.id.btn_calculate);
        etWeight = root.findViewById(R.id.weight_id);
        etRecent = root.findViewById(R.id.recentbs_id);
        etSugar = root.findViewById(R.id.sugars_id);
        etCarbs = root.findViewById(R.id.carbs_id);
        tvSugarContrib = root.findViewById(R.id.sugar_contrib_id);
        tvCarbContrib = root.findViewById(R.id.carb_contrib_id);
        tvBothContribs = root.findViewById(R.id.both_contribs_id);
        tvInsulin = root.findViewById(R.id.insulin_result_id);
        btnCalculate.setOnClickListener(this);
        return root;
    }
    //=========================================================================================
    private class LongRunningTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            Log.d("GlyCalcFragment", " [onPreExecute() call]");
            super.onPreExecute();
            str_sugar = etSugar.getText().toString();
            str_carbs = etCarbs.getText().toString();
            str_weight = etWeight.getText().toString();
            str_recent = etRecent.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... voids){
            Log.d("GlyCalcFragment", " [doInBackground() call]");
            ginc = get_glucose_contrib(Integer.parseInt(str_sugar), Integer.parseInt(str_weight));
            cinc = get_carbs_contrib(Integer.parseInt(str_carbs), Integer.parseInt(str_weight));
            insulin_rec = get_insulin_rec(ginc + cinc, Integer.parseInt(str_recent));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("GlyCalcFragment:", " [onPostExecute() call]");
            super.onPostExecute(aVoid);
            tvInsulin.setTextColor(Color.parseColor("#5032a8"));
            tvSugarContrib.setTextColor(Color.parseColor("#7f0bb5"));
            tvCarbContrib.setTextColor(Color.parseColor("#7f0bb5"));
            tvBothContribs.setTextColor(Color.parseColor("#7f0bb5"));
            tvInsulin.setText(getString(R.string.insulin_rec, insulin_rec));
            tvSugarContrib.setText(getString(R.string.g_increase, ginc));
            tvCarbContrib.setText(getString(R.string.c_increase, cinc));
            tvBothContribs.setText(getString(R.string.gc_increase, (ginc+cinc)));
        }
    }
    //=========================================================================================
    @Override
    public void onClick(View v) {
        if (v.getId() == btnCalculate.getId()) {
            if (etWeight.getText().toString().trim().length() == 0) {
                TriggerToast("Please enter a value for Weight.");
            } else if (etRecent.getText().toString().trim().length() == 0) {
                TriggerToast("Please enter a value for Recent BS.");
            } else if (etSugar.getText().toString().trim().length() == 0) {
                TriggerToast("Please enter a value for Blood Sugar.");
            } else if (etCarbs.getText().toString().trim().length() == 0) {
                TriggerToast("Please enter a value for Carbs.");
            } else {
                new LongRunningTask().execute();
            }
        }
    }

    private int get_insulin_rec(int overall_mg_increase, int recent_BS){
    if (recent_BS >= 140)
        if (overall_mg_increase >= 50 && overall_mg_increase <= 70) return 3;
        else if (overall_mg_increase >= 71 && overall_mg_increase <= 90) return 5;
        else if (overall_mg_increase >= 91 && overall_mg_increase <= 110) return 6;
        else if (overall_mg_increase >= 111 && overall_mg_increase <= 130) return 7;
        else if (overall_mg_increase >= 131 && overall_mg_increase <= 150) return 8;
        else if (overall_mg_increase >= 151 && overall_mg_increase <= 180) return 10;
        else if (overall_mg_increase >= 181 && overall_mg_increase <= 210) return 12;
        else if (overall_mg_increase >= 211 && overall_mg_increase <= 240) return 14;
        else if (overall_mg_increase >= 241 && overall_mg_increase <= 280) return 16;
    return -1;
}
    private int get_glucose_contrib ( int glucose_consumed, int weight){
    double result = (glucose_consumed) * (get_g_mg_modifier(weight));
    return (int) result;
}
    private int get_carbs_contrib ( int carbs_consumed, int weight){
    double result = (carbs_consumed) * (get_c_mg_modifier(weight));
    return (int) result;
}
    private double get_g_mg_modifier ( int weight){
    if (weight >= 35 && weight <= 70) return 9.0;
    else if (weight >= 71 && weight <= 105) return 7.0;
    else if (weight >= 106 && weight <= 140) return 5.5;
    else if (weight >= 141 && weight <= 175) return 4.35;
    else if (weight >= 176 && weight <= 210) return 3.7;
    else if (weight >= 211 && weight <= 245) return 3.1;
    return -1;
}
    private double get_c_mg_modifier ( int weight) {
        if (weight >= 45 && weight <= 60) return 8.0;
        else if (weight >= 61 && weight <= 100) return 5.0;
        else if (weight >= 101 && weight <= 170) return 4.0;
        else if (weight >= 171 && weight <= 230) return 3.0;
        else if (weight >= 231 && weight <= 280) return 1.5;
        return -1;
    }

    private void TriggerToast(String error_msg){
        Toast.makeText(getContext(), error_msg, Toast.LENGTH_SHORT).show();
    }

}


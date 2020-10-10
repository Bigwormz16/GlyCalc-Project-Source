package com.example.testapp_glycalc_1.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp_glycalc_1.MainActivity;
import com.example.testapp_glycalc_1.R;

public class CoverPageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Button btn_display;
    Spinner spinner_type;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_page);
        mContext = this.getApplicationContext();

        spinner_type = (Spinner) findViewById(R.id.id_spinner_type);
        //create a list of items for the spinner.
        String[] spinner_selections = new String[]{"Type 1 Diabetes", "Type 2 Diabetes", "Prediabetes", "Gestational Diabetes"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_dropdown_item, spinner_selections);
        //set the spinners adapter to the previously created one.
        spinner_type.setAdapter(adapter);
        spinner_type.setOnItemSelectedListener(this);

        btn_display = findViewById(R.id.id_btnBegin);
        btn_display.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("key", 1);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            if(parent.getChildAt(0)!=null) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView) parent.getChildAt(0)).setTextSize(16);
            }
        }catch (Exception e) {TriggerToast(e.getMessage());}

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void TriggerToast(String error_msg){
        Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_SHORT).show();
    }
}
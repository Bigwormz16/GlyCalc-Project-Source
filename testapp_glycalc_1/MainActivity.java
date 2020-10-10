package com.example.testapp_glycalc_1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testapp_glycalc_1.ui.dashboard.CoverPageActivity;
import com.example.testapp_glycalc_1.ui.notifications.VisualizeFragment;
import com.example.testapp_glycalc_1.ui.dashboard.TrackerEntry;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<TrackerEntry> trackerEntryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_glycalc, R.id.nav_tracker, R.id.nav_visualize)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
//==========Stetho SQL Viewer @ chrome://inspect ===============================================================
        Stetho.initializeWithDefaults(this);
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
//========If activity already started, initial intent received will prevent loop from triggering============
        Intent receivedIntent = getIntent();
        int returned_value = receivedIntent.getIntExtra("key", 0);
        if(returned_value != 1) {
            Intent intent = new Intent(this, CoverPageActivity.class);
            startActivity(intent);
        }
//=======Populating the RecyclerView using Custom Adapter=========??================================================

    }
}










package com.example.testapp_glycalc_1.ui.notifications;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;


import com.anychart.AnyChart;


import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian3d;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Column3d;
import com.anychart.core.ui.LabelsFactory;
import com.anychart.core.ui.Tooltip;
import com.anychart.core.ui.labelsfactory.Label;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.HoverMode;
import com.anychart.enums.ScaleStackDirection;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.SelectionMode;
import com.anychart.enums.TooltipDisplayMode;
import com.example.testapp_glycalc_1.R;
import com.example.testapp_glycalc_1.SQLdbManager;
import com.example.testapp_glycalc_1.ui.dashboard.TrackerEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.example.testapp_glycalc_1.SQLdbHelper.COLUMN1;
import static com.example.testapp_glycalc_1.SQLdbHelper.COLUMN2;
import static com.example.testapp_glycalc_1.SQLdbHelper.COLUMN3;
import static java.lang.Integer.parseInt;

//butterknife? add this to dependencies     implementation 'com.jakewharton:butterknife:10.2.0'
//    implementation 'com.android.support:multidex:1.0.3'
//multi dex issues? //multiDexEnabled true under defaultConfig{} in app gradle file

public class VisualizeFragment extends Fragment {

    private List<DataEntry> data = new ArrayList<>();
    private Locale locale = Locale.ENGLISH;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_visualize,
                container, false);
        com.anychart.AnyChartView chartView = root.findViewById(R.id.chart_view);
        //=================================================================================
        Cartesian3d cartesian3d = AnyChart.cartesian3d();

        SQLdbManager dbManager = new SQLdbManager(getContext());
        dbManager.open();
        Cursor cursor = dbManager.fetch();
        ArrayList<TrackerEntry> trackerEntries = new ArrayList<>();
        cursor.moveToFirst();
        add_to_entries_array(cursor, trackerEntries);
        while(cursor.moveToNext())add_to_entries_array(cursor, trackerEntries);
        int x = 0;
        for (TrackerEntry entry : trackerEntries) {
            x++;
            String month_day = new SimpleDateFormat("MM/dd(hh:0"+x+")", locale).format(Calendar.getInstance().getTime());
            if(x>9){
                month_day = new SimpleDateFormat("MM/dd(hh:"+x+")", locale).format(Calendar.getInstance().getTime());
            }
            add_To_List(data, month_day, entry.getBlood_sugar(), entry.getLast_dose());
        }


        cartesian3d.yScale().stackMode(ScaleStackMode.VALUE);
        cartesian3d.yScale().stackDirection(ScaleStackDirection.DIRECT);

        cartesian3d.animation(true);
        cartesian3d.title("Recent Blood Sugar results and Insulin Ranges");

        Set set = Set.instantiate();
        set.data(data);

        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2', value2: 'value2' }");


        Column3d series1 = cartesian3d.column(series1Data);

        series1.name("Blood Sugar ")
                .labels(true)
            .labels()
                .format("{%value}")
                .fontWeight(600)
                .fontColor("#363131")
                .padding()
                .left(8d);

        series1.normal()
                .fill("#FF124F", 0.3) //red
            .hovered()
                .fill("#FF124F", 0.1)
            .selected()
                .fill("#FF124F", 0.5);

        Column3d series2 = cartesian3d.column(series2Data);

        series2.name("Last Insulin Dose ")
                .labels(true).
            labels()
                .fontWeight(600)
                .fontColor("#363131")
                .format("{%value}{groupsSeparator: }")
                .padding()
                .right(4d);


        series2.normal()
                .fill("#00D7E9", 0.3) //blue
            .hovered()
                .fill("#00D7E9", 0.1)
            .selected()
                .fill("#00D7E9", 0.5);



        cartesian3d.legend()
                .enabled(true)
                .fontSize(10d);

        cartesian3d.xAxis(0)
                .stroke("1 #b09e9d")//Grey
            .title()
                .enabled(true)
                .text("Insulin Dosage Taken");
        cartesian3d.xAxis(0)
            .labels()
                .fontSize(10d)
                .fontColor("#a18b7e");

        cartesian3d.yAxis(0)
                .stroke("1 #b09e9d")
            .labels()
                .fontColor("#a18b7e")
                .format("{%Value}{groupsSeparator: }");

        cartesian3d.yAxis(0)
            .title()
                .enabled(true)
                .text("Blood Sugar Levels")
                .fontColor("#a18b7e");

        cartesian3d.interactivity().hoverMode(HoverMode.BY_X);
        cartesian3d.tooltip()
                .displayMode(TooltipDisplayMode.UNION)
                .format("{%SeriesName}: {%value}");

        cartesian3d.yGrid(0).stroke("#b09e9d", 1d, null, null, null);
        cartesian3d.xGrid(0).stroke("#b09e9d", 1d, null, null, null);

        cartesian3d.padding().top(10d);
        cartesian3d.container("container");
        cartesian3d.draw(true);
        chartView.setChart(cartesian3d);

        //adds a box around labels at bottom.
//        (cartesian3d.xAxis(0).labels().background()).enabled(true);
//        (cartesian3d.xAxis(0).labels().background()).stroke("#cecece");
//        (cartesian3d.xAxis(0).labels().background()).cornerType("round");
//        (cartesian3d.xAxis(0).labels().background()).corners(5);

        return root;
    }


    private void add_To_List(List<DataEntry> list, String x, int value, int value2) {
        list.add(new CustomDataEntry(x, value, value2));

    }

    private void add_to_entries_array(Cursor cursor,
                                      ArrayList<TrackerEntry> entries) {
        try {
            entries.add(new TrackerEntry(parseInt(cursor.getString(cursor.getColumnIndex(COLUMN1))),
                    parseInt(cursor.getString(cursor.getColumnIndex(COLUMN2))),
                    cursor.getString(cursor.getColumnIndex(COLUMN3))));
        } catch (CursorIndexOutOfBoundsException e) { 
            TriggerToast("Cursor Out of Bounds: " + e.getMessage()); }
    }

    private void TriggerToast(String error_msg){
        Toast.makeText(getContext(), error_msg, Toast.LENGTH_SHORT).show();
    }

    private class CustomDataEntry extends ValueDataEntry {
        CustomDataEntry(String x, Number value, Number value2) {
            super(x,value);
            setValue("value2", value2);
        }
    }
}

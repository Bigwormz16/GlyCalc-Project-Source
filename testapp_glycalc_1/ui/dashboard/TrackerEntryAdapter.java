package com.example.testapp_glycalc_1.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.testapp_glycalc_1.R;

import java.util.ArrayList;

public class TrackerEntryAdapter extends ArrayAdapter<TrackerEntry> {

    public TrackerEntryAdapter(Context context, ArrayList<TrackerEntry> users) {
        super(context, 0, users);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        TrackerEntry trackerEntry = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_tracker_entry_layout, parent, false);
        }
        // Lookup view for entry values
        TextView tvBlood = (TextView) convertView.findViewById(R.id.tv_cust_bloodSugar);
        TextView tvDose = (TextView) convertView.findViewById(R.id.tv_cust_lastDose);
        TextView tvMeal = (TextView) convertView.findViewById(R.id.tv_cust_mealDetails);
        ImageView iv_Droplet = (ImageView) convertView.findViewById(R.id.blood_img);
        // Populate the data into the template view using the data object
        assert trackerEntry != null;

        //android:text="@{@string/some_string(user.name)}"/>  <-- alternate method through XML
        //Maybe add a method to make sure all sentences end with a period, but only 1.
        tvBlood.setText(String.format(getContext().getString(R.string.BS_PlaceHolder), trackerEntry.getBlood_sugar()));
        tvDose.setText(String.format(getContext().getString(R.string.LD_PlaceHolder), trackerEntry.getLast_dose()));
        tvMeal.setText(String.format(getContext().getString(R.string.MD_PlaceHolder), trackerEntry.getMeal_details()));

        //Set image color based on blood sugar result.
        int blood_sugar = trackerEntry.getBlood_sugar();
        //70 to 130 mg/dl before meals and less than 180 mg/dl one to two hours after a meal.
        if(blood_sugar > 175) {
            //if BS is ABOVE 175 then set Droplet to Red/HIGH
            iv_Droplet.setRotation(180);
            iv_Droplet.setImageResource(R.drawable.red_tracker_droplet_img);
            //if BS is BETWEEN 120-175 then set Droplet to Orange/MED
        } else if (120 < blood_sugar && blood_sugar < 175) {
            iv_Droplet.setRotation(180);
            iv_Droplet.setImageResource(R.drawable.orng_tracker_droplet_img);
            //if BS is under 120 but greater than 69
        } else if (blood_sugar <= 120 && blood_sugar > 69){
            iv_Droplet.setRotation(180);
            iv_Droplet.setImageResource(R.drawable.blue_tracker_droplet_img);
        } else {
            iv_Droplet.setRotation(0);
            iv_Droplet.setImageResource(R.drawable.red_warning_img);
        }

        //X // Return the completed view to render on screen
        return convertView;
    }
}
//X         To get the string from XML only --->  this.getString(R.string.resource_name) if in activity.
//        --> If not in activity but have access to context:
//        context.getString(R.string.resource_name  OR   application.getString(R.string.resource_name);
//        FORMAT THE PLACEHOLDER STRING from XML -> String.format(getResources().getString(R.string.my_string), stringName);

package com.example.testapp_glycalc_1.ui.dashboard;

import com.anychart.chart.common.dataentry.DataEntry;

public class TrackerEntry extends DataEntry {
    private int blood_sugar;
    private int last_dose;
    private String meal_details;

    public TrackerEntry(int blood_sugar, int last_dose, String meal_details) {
        this.blood_sugar = blood_sugar;
        this.last_dose = last_dose;
        this.meal_details = meal_details;
    }

    public TrackerEntry() { }

    public TrackerEntry(TrackerEntry trackerEntry) {
        this.blood_sugar = trackerEntry.getBlood_sugar();
        this.last_dose = trackerEntry.getLast_dose();
        this.meal_details = trackerEntry.getMeal_details();
    }

    public int getBlood_sugar() {
        return blood_sugar;
    }

    public void setBlood_sugar(int blood_sugar) {
        this.blood_sugar = blood_sugar;
    }

    public int getLast_dose() {
        return last_dose;
    }

    public void setLast_dose(int last_dose) {
        this.last_dose = last_dose;
    }

    public String getMeal_details() {
        return meal_details;
    }

    public void setMeal_details(String meal_details) {
        this.meal_details = meal_details;
    }




}



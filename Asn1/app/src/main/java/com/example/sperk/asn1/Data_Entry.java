package com.example.sperk.asn1;

/**
 * Created by Sperk on 1/24/2016.
 * Data_Entry is used to store each log entry
 * Automatically finds cost of each entry
 */
public class Data_Entry {
    private String station;
    private String date;
    private String flGrade;
    private float flAmount;
    private float flUnit;
    private float odoMeter;
    private float flCost;

    //Constructor
    public Data_Entry(String station, String date, String flGrade, float flAmount, float flUnit, float odoMeter) {
        this.station = station;
        this.date = date;
        this.flGrade = flGrade;
        this.flAmount = flAmount;
        this.flUnit = flUnit;
        this.odoMeter = odoMeter;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFlGrade() {
        return flGrade;
    }

    public void setFlGrade(String flGrade) {
        this.flGrade = flGrade;
    }

    public float getFlAmount() {
        return flAmount;
    }

    // Converts float to int for use in string
    public String showFlAmount() {
        float convert = getFlAmount();
        String decUnit = String.format("%.03f", convert);
        return decUnit;
    }

    public void setFlAmount(float flAmount) {
        this.flAmount = flAmount;
    }

    public float getFlUnit() {
        return flUnit;
    }

    // Converts float to string
    public String showFlUnit() {
        float convert = getFlUnit();
        String decUnit = String.format("%.01f", convert);
        return decUnit;
    }

    public void setFlUnit(float flUnit) {
        this.flUnit = flUnit;
    }

    public float getOdoMeter() {
        return odoMeter;
    }

    public void setOdoMeter(float odoMeter) {
        this.odoMeter = odoMeter;
    }

    // Converts float to string
    public String showOdoM() {
        float convert = getOdoMeter();
        String decUnit = String.format("%.01f", convert);
        return decUnit;
    }

    public float getFlCost() {
        return flCost;
    }

    public void setFlCost(float flCost) {
        this.flCost = flCost;
    }

    // Converts float to string
    public String showFlCost() {
        float convert = getFlCost();
        String decUnit = String.format("%.02f", convert);
        return decUnit;
    }

    // Finds the cost and sets the cost
    public void findCost() {
        float div = getFlUnit();
        float cost = getFlAmount();
        float total = (div / 100) * cost;
        setFlCost(total);
    }

    // toString creates an output to be used in the list
    // calls findCost to update cost everytime toString is called
    @Override
    public String toString() {
        findCost();
        return getDate() + "\n" + getStation() + "\n" + showOdoM() + "Kms\n"
                + "Fuel Grade: " + getFlGrade() + "\nFilled: " + showFlAmount() +
                "L\nCost: " + showFlUnit() + "cents/L\nTotal Cost: " + showFlCost();
    }
}

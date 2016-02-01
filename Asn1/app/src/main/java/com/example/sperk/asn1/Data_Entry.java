package com.example.sperk.asn1;

/**
 * Created by Sperk on 1/24/2016.
 */
public class Data_Entry {
    private String station;
    private String date;
    private String flGrade;
    private float flAmount;
    private float flUnit;
    private float odoMeter;
    private float flCost;

    public Data_Entry(String station, String date, String flGrade, float flAmount, float flUnit, float odoMeter) {
        this.station = station;
        this.date = date;
        this.flGrade = flGrade;
        this.flAmount = flAmount;
        this.flUnit = flUnit;
        this.odoMeter = odoMeter;
        //this.flCost = findCost();
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

    public String showFlCost() {
        float convert = getFlCost();
        String decUnit = String.format("%.02f", convert);
        return decUnit;
    }

    public void findCost() {
        float div = getFlUnit();
        float cost = getFlAmount();
        float total = (div / 100) * cost;
        //div = div/100;
        //float total = cTod * cost;
        setFlCost(total);
        //return total;
    }
    @Override
    public String toString() {
        //return "Working";
        findCost();
        return getDate() + ", " + getStation() + ", " + showOdoM() + "Kms, "
                + "Fuel Grade: " + getFlGrade() + ", " + showFlAmount() +
                "L, " + showFlUnit() + "cents/L, Total Cost: " + showFlCost();
    }
}

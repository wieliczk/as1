package com.example.sperk.asn1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/*
 * Edit Entry is accessed from clicking on a entry in the
 * list view, can remove, edit or cancel anc just go back
 * to viewing
 */
public class EditEntry extends ActionBarActivity {

    private static String FILENAME = "file.sav"; //Taken from lonely twitter
    private ArrayList<Data_Entry> D_logs = new ArrayList<Data_Entry>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Grabs the position passed through and is used to access and edit correct log
        final int ed_log = getIntent().getExtras().getInt("data_logEdit");
        setContentView(R.layout.activity_edit_entry);
        loadFromFile();
        datalogSetup(ed_log);

        // Cancel Button is used directly in xml file for onClick
        Button remove_bttn = (Button) findViewById(R.id.button_rm);
        Button update_bttn = (Button) findViewById(R.id.up_button);
        update_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorHandleEdit(ed_log);
            }
        });
        remove_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeLogEntry(ed_log);
            }
        });
    }

    // Called from onClick of cancel button and goes back to viewing screen
    public void backToView(View view) {
        Intent cancels = new Intent(this, viewingAct.class);
        Toast dataCan = new Toast(getApplicationContext());
        dataCan.makeText(EditEntry.this, "Data Edit Cancelled", dataCan.LENGTH_SHORT).show();
        // Code take from http://stackoverflow.com/questions/11460896/button-to-go-back-to-mainactivity
        cancels.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(cancels);
    }

    // Taken from lonely twitter
    public void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            // Taken from Google
            Type listType = new TypeToken<ArrayList<Data_Entry>>() {}.getType();
            // Read arraylist from json and set list to it
            D_logs = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            D_logs = new ArrayList<Data_Entry>();
        }
    }

    // Sends message (based on int given) and returns to viewing screen
    public void saveToMain(int message) {
        if (message == 1) {
            Toast dataSaved = new Toast(getApplicationContext());
            dataSaved.makeText(EditEntry.this, "Data Entry Edited", dataSaved.LENGTH_SHORT).show();
        }
        else {
            Toast dataSaved = new Toast(getApplicationContext());
            dataSaved.makeText(EditEntry.this, "Data Entry Removed", dataSaved.LENGTH_SHORT).show();
        }
        Intent saved = new Intent(this, viewingAct.class);
        // Code take from http://stackoverflow.com/questions/11460896/button-to-go-back-to-mainactivity
        saved.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(saved);

    }

    // Taken from lonely twitter
    public void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(D_logs, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // Do nothing if no file is found. Right?
            //throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    // Same as previously used in added.java
    public int checkDate(String string) {
        int isWrong = 1;
        try {
            int year = Integer.parseInt(string.substring(0, 4));
            String dash = string.substring(4,5);
            if(dash.equals("-")) {
                int month = Integer.parseInt(string.substring(5,7));
                ArrayList<Integer> monthlist = new ArrayList<Integer>();
                for (int i = 1; i < 13; i++) {
                    monthlist.add(i);
                }
                if (monthlist.contains(month)) {
                    String dash2 = string.substring(7,8);
                    if (dash2.equals("-")) {
                        int day = Integer.parseInt(string.substring(8));
                        ArrayList<Integer> dayList = new ArrayList<Integer>();
                        for (int i = 1; i < 32; i++) {
                            dayList.add(i);
                        }
                        if (dayList.contains(day)) {
                            isWrong = 0;
                        }
                        else {
                            throw new Exception();
                        }
                    }
                }
                else {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            // Do nothing
        }
        return isWrong;
    }

    // Adds current log data above text enter fields
    public void datalogSetup(int ed_log) {
        TextView data_text = (TextView) findViewById(R.id.dateText);
        data_text.setText("Enter Date:  (Currently: " + D_logs.get(ed_log).getDate() + ")");

        TextView station_text = (TextView) findViewById(R.id.stationText);
        station_text.setText("Enter Station: (Currently: " + D_logs.get(ed_log).getStation() + ")");

        TextView grade_text = (TextView) findViewById(R.id.gradeText);
        grade_text.setText("Enter Grade: (Currently: " + D_logs.get(ed_log).getFlGrade() + ")");

        TextView unit_text = (TextView) findViewById(R.id.unitText);
        unit_text.setText("Enter Cost (cents/L): (Currently: " + D_logs.get(ed_log).getFlUnit() + ")");

        TextView odo_text = (TextView) findViewById(R.id.odoText);
        odo_text.setText("Enter Odometer: (Currently: " + D_logs.get(ed_log).getOdoMeter() + ")");

        TextView amount_text = (TextView) findViewById(R.id.amountText);
        amount_text.setText("Enter Amount (In L): (Currently: " + D_logs.get(ed_log).getFlAmount() + ")");
    }

    // Checks each field for edit, if field unchange (left "")
    // Program will move on, otherwise it will error check if correct set the log entry
    // Try and catch to catch any wrong entry
    public void errorHandleEdit(int ed_log) {
        int counter = 0;
        try {
            while (counter == 0) {
                EditText setsStation = (EditText) findViewById(R.id.editTextStation);
                if (setsStation.getText().toString().equals("")) {
                    // Do nothing
                    counter++;
                } else {
                    String station = setsStation.getText().toString();
                    D_logs.get(ed_log).setStation(station);
                    counter++;
                }
            }

            while (counter == 1) {
                EditText setsDate = (EditText) findViewById(R.id.editTextDate);
                if (setsDate.getText().toString().equals("")) {
                    // Do nothing
                    counter++;
                } else {
                    String sDate = setsDate.getText().toString();
                    int checker;
                    checker = checkDate(sDate);
                    if (checker == 1) {
                        throw new RuntimeException();
                    } else {
                        D_logs.get(ed_log).setDate(sDate);
                        counter++;
                    }
                }
            }

            while (counter == 2) {
                EditText setsflGrade = (EditText) findViewById(R.id.editTextGrade);
                if (setsflGrade.getText().toString().equals("")) {
                    // Do nothing
                    counter++;
                } else {
                    String sFlGrade = setsflGrade.getText().toString();
                    D_logs.get(ed_log).setFlGrade(sFlGrade);
                    counter++;
                }
            }

            while (counter == 3) {
                EditText setsUint = (EditText) findViewById(R.id.editTextUnit);
                if (setsUint.getText().toString().equals("")) {
                    // Do nothing
                    counter++;
                } else {
                    float sUnit = Float.valueOf(setsUint.getText().toString());
                    D_logs.get(ed_log).setFlUnit(sUnit);
                    counter++;
                }
            }

            while (counter == 4) {
                EditText setsAmount = (EditText) findViewById(R.id.editTextAmount);
                if (setsAmount.getText().toString().equals("")) {
                    // Do nothing
                    counter++;
                } else {
                    float sAmount = Float.valueOf(setsAmount.getText().toString());
                    D_logs.get(ed_log).setFlAmount(sAmount);
                    counter++;
                }
            }

            while (counter == 5) {
                EditText setsOdo = (EditText) findViewById(R.id.editTextOdo);
                if (setsOdo.getText().toString().equals("")) {
                    // Do nothing
                    counter++;
                } else {
                    float sOdo = Float.valueOf(setsOdo.getText().toString());
                    D_logs.get(ed_log).setOdoMeter(sOdo);
                    counter++;
                }
            }
            // Recalculates the costs incase entry was updated
            D_logs.get(ed_log).findCost();
            saveInFile();
            saveToMain(1);
        } catch (Exception e) {
            Toast dataWrong = new Toast(getApplicationContext());
            dataWrong.makeText(EditEntry.this, "Incorrect Entry", dataWrong.LENGTH_SHORT).show();
        }
    }

    // Removes entire entry log and sends back to viewing screen
    public void removeLogEntry(int ed_log) {
        D_logs.remove(ed_log);
        saveInFile();
        saveToMain(0);
    }
}
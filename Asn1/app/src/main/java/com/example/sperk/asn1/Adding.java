package com.example.sperk.asn1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
 * Adding adds a data entry to the saved list of entries
 * Has checks for the date as well as the floats
 * If all is correct, will save entries to Gson and
 * sends user back to MainActivity screen with a toast message
 * saying entry saved.
 */
public class Adding extends ActionBarActivity {
    private static String FILENAME = "file.sav";
    private ArrayList<Data_Entry> D_logs = new ArrayList<Data_Entry>();
    private int checker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFromFile();
        setContentView(R.layout.activity_adding);
        Button enterData = (Button) findViewById(R.id.enter_button);
        enterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
           }
      });
   }

    // backToMain_C is called from onClick funciton of the xml file
    // Cancel button clickable is applied to backToMain_C
    // Returns to MainActivity and sends Message
    public void backToMain_C(View view) {
        Intent cancels = new Intent(this, MainActivity.class);
        Toast dataCan = new Toast(getApplicationContext());
        dataCan.makeText(Adding.this, "Data Entry Cancelled", dataCan.LENGTH_SHORT).show();
        // Code take from http://stackoverflow.com/questions/11460896/button-to-go-back-to-mainactivity
        cancels.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(cancels);
    }

    // Returns to MainActivity and sends message
    public void saveToMain() {
        Toast dataSaved = new Toast(getApplicationContext());
        dataSaved.makeText(Adding.this, "Data Entry Saved", dataSaved.LENGTH_SHORT).show();
        Intent saved = new Intent(this, MainActivity.class);
        // Code take from http://stackoverflow.com/questions/11460896/button-to-go-back-to-mainactivity
        saved.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(saved);
    }

    // Checks proper date entry
    // If completes every check, only then is date accepted
    public int checkDate(String string) {
        int isWrong = 1;
        try {
            int year = Integer.parseInt(string.substring(0, 4));
            String dash = string.substring(4,5);
            if(dash.equals("-")) {

                // Checks for proper month
                int month = Integer.parseInt(string.substring(5,7));
                ArrayList<Integer> monthlist = new ArrayList<Integer>();
                for (int i = 1; i < 13; i++) {
                    monthlist.add(i);
                }
                if (monthlist.contains(month)) {
                    String dash2 = string.substring(7,8);
                    if (dash2.equals("-")) {

                        // Checks for proper date
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
        // Returns an int either 0 or 1 that sends proper message
        return isWrong;
    }

    // Taken from lonely Twitter
    public void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            // Taken from Google
            Type listType = new TypeToken<ArrayList<Data_Entry>>() {}.getType();
            D_logs = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            D_logs = new ArrayList<Data_Entry>();
//            d_logs.setData_logs(D_logs);
        }
    }

    // Taken from lonely Twitter
    public void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(D_logs, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // Do nothing

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    // Add data sets up collects and checks to make sure each entry is correct
    // If everything is correct data is added to file
    // If entry(s) are incorrect will send a toast message
    public void addData() {
        try {
            EditText setsStation = (EditText) findViewById(R.id.editStation);
            String station = setsStation.getText().toString();

            EditText setsDate = (EditText) findViewById(R.id.editDate);
            String sDate = setsDate.getText().toString();

            // Checks date else where to keep code clean
            checker = checkDate(sDate);
            if (checker == 1) {
                throw new RuntimeException();
            }

            EditText setsflGrade = (EditText) findViewById(R.id.gradeText);
            String sFlGrade = setsflGrade.getText().toString();

            EditText setsUint = (EditText) findViewById(R.id.editUnit);
            float sUnit = Float.valueOf(setsUint.getText().toString());

            EditText setsAmount = (EditText) findViewById(R.id.editAmount);
            float sAmount = Float.valueOf(setsAmount.getText().toString());

            EditText setsOdo = (EditText) findViewById(R.id.editOdo);
            float sOdo = Float.valueOf(setsOdo.getText().toString());

            // Creates data log and goes back MainActivity
            Data_Entry log = new Data_Entry(station, sDate, sFlGrade, sAmount, sUnit, sOdo);
            log.findCost();
            D_logs.add(log);
            saveInFile();
            saveToMain();
        } catch (Exception e) {
            // Do nothing
            Toast dataWrong = new Toast(getApplicationContext());
            dataWrong.makeText(Adding.this, "Incorrect Entry", dataWrong.LENGTH_SHORT).show();
        }
    }
}


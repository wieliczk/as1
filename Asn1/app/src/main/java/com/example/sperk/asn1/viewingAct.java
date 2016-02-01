package com.example.sperk.asn1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.List;

/*
 * ViewingAct creates a listView for all of the entries
 * Each entry can be clicked which sends it to and edit screen
 * Clear button clears all entries and updates list
 * Back button goes back to MainActivity
 */
public class viewingAct extends ActionBarActivity {
    private static String FILENAME = "file.sav";
    private ArrayList<Data_Entry> D_logs = new ArrayList<Data_Entry>();
    private ArrayAdapter<Data_Entry> adapter;
    private ListView dataList;
    protected float totalCost;
    private int arrSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing);

        // Back button implimented in xml file with onClick to backFromView()
        dataList = (ListView) findViewById(R.id.dataList);
        Button removeData = (Button) findViewById(R.id.buttonClears);
        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (viewingAct.this, EditEntry.class);
                intent.putExtra("data_logEdit", position);
                startActivity(intent);
            }
        });

        // Clears entries and updates list
        removeData.setOnClickListener(new View.OnClickListener() {
            @Override
            // Taken From Lonely Twitter
            public void onClick(View v) {
                D_logs.clear();
                try {
                    FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                    BufferedWriter clr = new BufferedWriter(new OutputStreamWriter(fos));
                    Gson gson = new Gson();
                    gson.toJson(D_logs, clr);
                    clr.flush();
                    fos.close();
                    adapter.notifyDataSetChanged();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException();
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Loads file from Gson
        loadFromFile();

        // Gets size and finds total cost of all entries
        arrSize = D_logs.size();
        totalCost = findTotalFuel(arrSize);
        // Sends totalcost to a text view
        TextView totalFuels = (TextView) findViewById(R.id.totalFuelText);
        String totalCostString = String.format("%.02f", totalCost);
        totalFuels.setText("Total Fuel Cost: " + totalCostString);
        adapter = new ArrayAdapter<Data_Entry>(this, R.layout.list_item, R.id.data_list, D_logs);
        dataList.setAdapter(adapter);

    }
    public void backFromView(View view) {
        Intent back = new Intent(this, MainActivity.class);
        // Code take from http://stackoverflow.com/questions/11460896/button-to-go-back-to-mainactivity
        back.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
    }
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

    // Gathers all fuel costs using for loop
    public float findTotalFuel(int size) {
        float total = 0;
        for (int i = 0; i < size; i++) {
            float logCost = D_logs.get(i).getFlCost();
            total = total + logCost;
        }
        return total;
    }
}

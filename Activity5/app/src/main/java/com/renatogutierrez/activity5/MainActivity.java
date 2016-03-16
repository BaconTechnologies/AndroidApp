package com.renatogutierrez.activity5;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements JSONRequest.JSONCallback, JSONArrayRequest.JSONCallback,
        AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private ListView zonesList;
    private Spinner spinner;
    private ArrayList< JSONObject > zones;
    private ArrayList< JSONObject > places;

    private boolean downloadingPlaces = false;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.spinner = (Spinner) findViewById(R.id.spinner);

        getSupportActionBar().setTitle("Parking brain");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196F3")));

        Window w = this.getWindow();
        // finally change the color
        w.setStatusBarColor(Color.parseColor("#1976D2"));

        this.zonesList = (ListView)findViewById(R.id.listView);
        this.spinner = (Spinner)findViewById(R.id.spinner);

        this.zonesList.setOnItemClickListener(this);
        this.spinner.setOnItemSelectedListener(this);

        this.loadZonesAndPlaces();
    }

    @Override
    protected void onResume(){
        super.onResume();

        this.loadZonesAndPlaces();
        Log.d("OnResume", "Volvi!!");
    }

    private void loadZonesAndPlaces(){
        this.zones = new ArrayList< JSONObject >();
        this.places = new ArrayList<JSONObject>();

        JSONObject everywhere = new JSONObject();
        try {
            everywhere.accumulate("place", "Everywhere");
            everywhere.accumulate("category", "general");
            this.places.add(everywhere);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new JSONArrayRequest(this).execute("https://enigmatic-brushlands-35263.herokuapp.com/api/zones");
        new JSONRequest(this).execute("https://enigmatic-brushlands-35263.herokuapp.com/api/places");
    }

    public void requestComplete(JSONObject array) {
        try {
            Iterator<String> keys = array.keys();

            while (keys.hasNext()) {
                String key = keys.next();

                JSONObject current = array.getJSONObject(key);
                current.accumulate("place", key);
                this.places.add(current);
            }

            this.spinner.setAdapter( new PlacesAdapter(this, this.places) );
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    // For the list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            int spacesAvailables = Integer.valueOf(((JSONObject) this.zonesList.getItemAtPosition(position)).getString("availability"));
            Toast.makeText(this, "Available Spaces: " + spacesAvailables, Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // For the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(position == 0){
            this.zonesList.setAdapter(new JSONAdapter(this, this.zones));
        }
        else {
            try {
                String zoneID = this.places.get(position).getString("zoneID");

                ArrayList<JSONObject> tempZones = new ArrayList<>();
                for (int i = 0; i < this.zones.size(); i++) {
                    if (this.zones.get(i).getString("id").equals(zoneID)) {
                        tempZones.add(this.zones.get(i));

                        if(this.zones.get(i).getString("availability").equals("0")){
                            JSONObject recommended = this.zones.get(0);
                            recommended.accumulate("isRecommended", "true");
                            tempZones.add(recommended);

                            Toast.makeText(this, "We recommend you " + this.zones.get(0).getString("name"), Toast.LENGTH_SHORT).show();
                        }

                        this.zonesList.setAdapter(new JSONAdapter(this, tempZones));
                        break;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void requestComplete(JSONArray array) {
        try {

            for(int i = 0; i < array.length(); i++){
                JSONObject current = array.getJSONObject(i);
                this.zones.add(current);
            }

            int max = 0;
            for (int i = 0; i < this.zones.size(); i++) {

                for (int j = 0; j < this.zones.size() - 1 - i; j++) {
                    int availability1 = Integer.valueOf(this.zones.get(j).getString("availability"));

                    int availability2 = Integer.valueOf(this.zones.get(j + 1).getString("availability"));

                    if (availability1 < availability2) {
                        JSONObject tmp = this.zones.get(j);
                        this.zones.set(j, this.zones.get(j + 1));
                        this.zones.set(j + 1, tmp);

                    }
                }

            }

            this.zonesList.setAdapter(new JSONAdapter(this, this.zones));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.renatogutierrez.activity5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements JSONRequest.JSONCallback, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private ListView zonesList;
    private Spinner spinner;
    private ArrayList< JSONObject > zones;
    private ArrayList< JSONObject > places;

    private boolean downloadingPlaces = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.spinner = (Spinner) findViewById(R.id.spinner);

        getSupportActionBar().setTitle("Parking brain");
        getSupportActionBar().hide();

        this.zonesList = (ListView)findViewById(R.id.listView);
        this.spinner = (Spinner)findViewById(R.id.spinner);

        this.zonesList.setOnItemClickListener(this);
        this.spinner.setOnItemSelectedListener(this);

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

        downloadingPlaces = false;
        new JSONRequest(this).execute("https://raw.githubusercontent.com/AlbertoVillagomez/Pruebas/master/zones.json");
    }

    public void requestComplete(JSONObject array) {
        try {
            if(!downloadingPlaces) {
                Iterator<String> keys = array.keys();

                while (keys.hasNext()) {
                    String key = keys.next();

                    JSONObject current = array.getJSONObject(key);
                    current.accumulate("zoneID", key);
                    this.zones.add(current);
                }

                int max = 0;
                for (int i = 0; i < this.zones.size(); i++) {

                    for (int j = 0; j < this.zones.size() - 1 - i; j++) {
                        int cap1 = Integer.valueOf(this.zones.get(j).getString("capacity"));
                        int ocup1 = Integer.valueOf(this.zones.get(j).getString("occupancy"));

                        int cap2 = Integer.valueOf(this.zones.get(j + 1).getString("capacity"));
                        int ocup2 = Integer.valueOf(this.zones.get(j + 1).getString("occupancy"));

                        if ((cap1 - ocup1) < (cap2 - ocup2)) {
                            JSONObject tmp = this.zones.get(j);
                            this.zones.set(j, this.zones.get(j + 1));
                            this.zones.set(j + 1, tmp);

                        }
                    }

                }

                this.zonesList.setAdapter(new JSONAdapter(this, this.zones));

                downloadingPlaces = true;
                new JSONRequest(this).execute("https://raw.githubusercontent.com/AlbertoVillagomez/Pruebas/master/place.json");
            }
            else{
                Iterator<String> keys = array.keys();

                while (keys.hasNext()) {
                    String key = keys.next();

                    JSONObject current = array.getJSONObject(key);
                    current.accumulate("place", key);
                    this.places.add(current);
                }

                this.spinner.setAdapter( new PlacesAdapter(this, this.places) );

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    // For the list
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            int spacesAvailables = Integer.valueOf(this.zones.get(position).getString("capacity")) - Integer.valueOf(this.zones.get(position).getString("occupancy"));
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
            String zone = this.spinner.getSelectedItem().toString();

            try {
                String zoneID = this.places.get(position - 1).getString("zoneID");

                ArrayList<JSONObject> tempZones = new ArrayList<>();
                for (int i = 0; i < this.zones.size(); i++) {
                    if (this.zones.get(i).getString("zoneID").equals(zoneID)) {
                        tempZones.add(this.zones.get(i));
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
}

package com.bacontechnologies.parkingbrain;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by renatosalas on 15/03/16.
 */
public class PlacesAdapter extends BaseAdapter {

    private ArrayList<JSONObject> places;
    private Activity activity;

    private int goodAvailable = 30;

    public PlacesAdapter(Activity activity, ArrayList<JSONObject> array){
        this.places = array;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return this.places.size();
    }

    @Override
    public Object getItem(int position) {
        return this.places.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(R.layout.place,null);
        }

        ImageView iconField = (ImageView)convertView.findViewById(R.id.photo);
        TextView placeField = (TextView)convertView.findViewById(R.id.place);

        try {
            String place = this.places.get(position).getString("place");
            String category = this.places.get(position).getString("category");

            placeField.setText(place);

            int icon;
            switch (category){
                case "academic":
                    icon = R.drawable.academic;
                    break;
                case "food":
                    icon = R.drawable.food;
                    break;
                case "home":
                    icon = R.drawable.home;
                    break;
                case "sports":
                    icon = R.drawable.sports;
                    break;
                case "event":
                    icon = R.drawable.events;
                    break;
                default:
                    icon = R.drawable.pin_location;
                    break;

            }

            Picasso.with(convertView.getContext()).load(icon).into(iconField);

        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}

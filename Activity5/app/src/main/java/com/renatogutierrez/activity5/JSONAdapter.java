package com.renatogutierrez.activity5;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class JSONAdapter extends BaseAdapter {
    private ArrayList< JSONObject > users;
    private Activity activity;

    private int goodAvailable = 24;

    public JSONAdapter(Activity activity, ArrayList<JSONObject> array){
        this.users = array;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return this.users.size();
    }

    @Override
    public Object getItem(int position) {
        return this.users.get( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = activity.getLayoutInflater().inflate(R.layout.row,null);
        }

        TextView z = (TextView)convertView.findViewById(R.id.zone);
        TextView a = (TextView)convertView.findViewById(R.id.available);

        try {
            z.setTextColor(Color.parseColor("#727272"));

            z.setText(this.users.get(position).getString("name"));
            int spacesAvailables = Integer.valueOf(this.users.get(position).getString("capacity")) - Integer.valueOf(this.users.get(position).getString("occupancy"));
            a.setText(spacesAvailables + "");

            a.setTextColor(Color.parseColor("#727272"));


            if(spacesAvailables > goodAvailable) a.setBackground(convertView.getResources().getDrawable(R.drawable.badge_green));
            else a.setBackground(convertView.getResources().getDrawable(R.drawable.badge_yellow));
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}

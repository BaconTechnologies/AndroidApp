package com.bacontechnologies.parkingbrain;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

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
        TextView recommendation = (TextView)convertView.findViewById(R.id.recommendation);

        try {

            if(this.users.get(position).has("isRecommended")){
                recommendation.setVisibility(View.VISIBLE);

                this.users.get(position).remove("isRecommended");
            }else{
                recommendation.setVisibility(View.GONE);
            }

            z.setTextColor(Color.parseColor("#727272"));

            z.setText(this.users.get(position).getString("name"));
            int spacesAvailables = Integer.valueOf(this.users.get(position).getString("availability"));
            a.setText(spacesAvailables + "");

            a.setTextColor(Color.parseColor("#727272"));

            if(spacesAvailables > goodAvailable) a.setBackground(convertView.getResources().getDrawable(R.drawable.badge_green));
            else if(spacesAvailables > 0) a.setBackground(convertView.getResources().getDrawable(R.drawable.badge_yellow));
            else a.setBackground(convertView.getResources().getDrawable(R.drawable.badge_red));
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}

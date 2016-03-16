package com.renatogutierrez.activity5;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class JSONArrayRequest extends AsyncTask<String, Void, JSONArray> {

    private JSONCallback callback;
    public JSONArrayRequest(JSONCallback callback){
        this.callback = callback;
    }

    // the actual task logic
    @Override
    protected JSONArray doInBackground(String... params) {
        URLConnection connection = null;
        BufferedReader br = null;
        JSONArray result = null;

        try{
            URL url = new URL(params[0]);
            connection = (URLConnection) url.openConnection();

            // STREAM (not scream)
            InputStream is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line = "";

            while((line = br.readLine()) != null) {
                builder.append(line);
            }
            result = new JSONArray(builder.toString());
        }

        catch (Exception e) {e.printStackTrace();}

        finally {
            // a block that MUST be run
            try{
                // close the connection!!! free the resources!!
                if(br != null) br.close();
            }
            catch(Exception e) {e.printStackTrace();}
        }
        return result;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);
        callback.requestComplete(jsonArray);
    }

    // we're going to use this interface as a callback
    public interface JSONCallback{
        void requestComplete(JSONArray array);
    }

}


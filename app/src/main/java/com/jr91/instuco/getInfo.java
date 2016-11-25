package com.jr91.instuco;

/**
 * Created by Usuario on 25/11/16.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Async task class to get json by making HTTP call
 */

public class getInfo extends AsyncTask<Void, Void, Void> {

    private String url = "";
    private String root_name = "";
    private Context cxt;
    ArrayList<String> urls = new ArrayList<String>();
    ArrayList<String> usernames = new ArrayList<String>();
    private ProgressDialog progressDialog;


    public getInfo(ProgressDialog pDialog, Context context, String urlToGet, String name) {
        this.url = urlToGet;
        this.root_name = name;
        this.cxt = context;
        this.progressDialog = pDialog;

    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray object = jsonObj.getJSONArray(root_name);

                // looping through All Contacts
                for (int i = 0; i < object.length(); i++) {
                    JSONObject c = object.getJSONObject(i);

                    usernames.add(c.getString("username"));
                    urls.add(c.getString("url"));


                }
            } catch (final JSONException e) {
                Log.e("ERROR -> ", "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e("ERROR -> ", "Couldn't get json from server.");
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        String[] us = usernames.toArray(new String[0]);
        String[] ur = urls.toArray(new String[0]);

        // URL to get contacts JSON
        ListView listview = (ListView) ((Activity) cxt).findViewById(R.id.imageListView);
        listview.setAdapter(new adapterListView(cxt, us, ur));

        progressDialog.dismiss();

    }

}
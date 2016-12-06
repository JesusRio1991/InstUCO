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
    ArrayList<String> userPictures = new ArrayList<String>();
    ArrayList<String> idFoto = new ArrayList<String>();
    ArrayList<String> comentario = new ArrayList<String>();
    ArrayList<String> like = new ArrayList<String>();
    private ProgressDialog progressDialog;
    int sizeX;


    public getInfo(ProgressDialog pDialog, Context context, String urlToGet, String name, int size) {
        this.url = urlToGet;
        this.root_name = name;
        this.cxt = context;
        this.progressDialog = pDialog;
        this.sizeX = size;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        String jsonStr = null;

        HttpURLConnectionE h = new HttpURLConnectionE();
        try {
            jsonStr = h.sendGet(url);


        } catch (Exception e) {
            e.printStackTrace();
        }

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
                    userPictures.add(c.getString("urlfoto"));
                    idFoto.add(c.getString("idfoto"));
                    comentario.add(c.getString("comment"));
                    like.add(c.getString("like"));
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
        String[] up = userPictures.toArray(new String[0]);
        String[] id = idFoto.toArray(new String[0]);
        String[] cm = comentario.toArray(new String[0]);
        String[] li = like.toArray(new String[0]);


        final adapterListView adapter = new adapterListView(li, cxt, us, ur, up, id, idFoto, sizeX, cm);

        // URL to get contacts JSON
        final ListView listview = (ListView) ((Activity) cxt).findViewById(R.id.imageListView);
        listview.setAdapter(adapter);

        progressDialog.dismiss();

    }

}
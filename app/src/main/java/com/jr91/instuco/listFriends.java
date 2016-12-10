package com.jr91.instuco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class listFriends extends AppCompatActivity {

    ArrayList<String> urls = new ArrayList<String>();
    ArrayList<String> names = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_friends);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String q = intent.getExtras().getString("aux");

        String url = null;
        String username = intent.getExtras().getString("username");

        if (q.compareTo("0") == 0) {
            url = "http://ucogram.hol.es/getLinkSeguidores.php?username=" + username;
        } else {
            url = " http://ucogram.hol.es/getLinkSeguidos.php?username=" + username;
        }

        HttpURLConnectionE h = new HttpURLConnectionE();
        try {
            String json = h.sendGet(url);

            JSONObject jsonObj = new JSONObject(json);

            // Getting JSON Array node
            JSONArray object = jsonObj.getJSONArray("mensaje");

            for (int i = 0; i < object.length(); i++) {
                JSONObject c = object.getJSONObject(i);

                names.add(c.getString("username"));
                urls.add(c.getString("urlfoto"));

            }

            String[] us = names.toArray(new String[0]);
            String[] ur = urls.toArray(new String[0]);

            final adapterSearch adapter = new adapterSearch(us, ur, this);

            if (adapter.isEmpty()) {
                Toast.makeText(this, "No hay usuarios encontrados", Toast.LENGTH_LONG).show();
            } else {
                // URL to get contacts JSON
                final ListView listview = (ListView) findViewById(R.id.listViewFriends);
                listview.setAdapter(adapter);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

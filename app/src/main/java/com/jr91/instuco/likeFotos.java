package com.jr91.instuco;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONObject;

public class likeFotos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_fotos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final String q = intent.getExtras().getString("username");

        HttpURLConnectionE h = new HttpURLConnectionE();
        try {
            String json = h.sendGet("http://ucogram.hol.es/getCountLikeU.php?username=" + q);
            JSONObject jsonObj = new JSONObject(json);
            JSONArray object = jsonObj.getJSONArray("mensaje");
            String[] urls_grid = new String[object.length()];
            String[] ids = new String[object.length()];


            for (int i = 0; i < object.length(); i++) {
                JSONObject c = object.getJSONObject(i);
                urls_grid[i] = c.getString("url");
                ids[i] = c.getString("idfoto");

            }

            GridView gridView = (GridView) findViewById(R.id.gridlikefotos);
            gridView.setNumColumns(3);
            gridView.setAdapter(new adapterLikesFotos(urls_grid, ids, likeFotos.this));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
